package com.example.android.notificationexample;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_email)
    EditText mEmailField;
    @BindView(R.id.register_password)
    EditText mPasswordField;
    @BindView(R.id.register_name)
    EditText mNameField;
    @BindView(R.id.register_button)
    Button mRegBtn;
    @BindView(R.id.register_login_button)
    Button mLoginPageBtn;
    @BindView(R.id.register_image_btn)
    CircleImageView mImageBtn;
    @BindView(R.id.registerProgressBar)
    ProgressBar mRegisterProgressBar;

    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore;

    private String faculty;
    private String ID, position;
    private String department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        imageUri = null;
        mLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        faculty = getIntent().getStringExtra("faculty");
        ID = getIntent().getStringExtra("ID");
        position = getIntent().getStringExtra("position");
        if (position.equals("head")) {
            department = getIntent().getStringExtra("department");
        }

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    mRegisterProgressBar.setVisibility(View.VISIBLE);

                    final String name = mNameField.getText().toString().trim();
                    String email = mEmailField.getText().toString().trim();
                    String password = mPasswordField.getText().toString();

                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    final String user_id = mAuth.getCurrentUser().getUid();
                                    final StorageReference user_profile = mStorage.child(user_id + ".jpg");

                                    user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                                            if (uploadTask.isSuccessful()) {

                                                //final String download_url = uploadTask.getResult().toString();
                                                user_profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String download_url = uri.toString();

                                                        String token_id = FirebaseInstanceId.getInstance().getToken();

                                                        Map<String, Object> userMap = new HashMap<>();
                                                        userMap.put("name", name);
                                                        userMap.put("image", download_url);
                                                        userMap.put("token_id", token_id);
                                                        userMap.put("position", position);
                                                        userMap.put("faculty", faculty);
                                                        userMap.put("ID", ID);
                                                        if (position.equals("head")) {
                                                            userMap.put("department", department);
                                                        }

                                                        mFireStore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                                                sendToMain();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                                            }
                                                        });
                                                    }
                                                });






                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Error : " + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            mImageBtn.setImageURI(imageUri);
        }

    }
}
