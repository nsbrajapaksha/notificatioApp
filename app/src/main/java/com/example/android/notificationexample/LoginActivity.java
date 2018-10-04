package com.example.android.notificationexample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_email)
    EditText mEmailField;
    @BindView(R.id.login_password)
    EditText mPasswordField;
    @BindView(R.id.login_button)
    Button mLoginBtn;
    @BindView(R.id.login_register_button)
    Button mRegPageBtn;
    @BindView(R.id.login_progressBar)
    ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private DatabaseReference mdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference();

        mRegPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this, SelectActivity.class);
                startActivity(regIntent);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailField.getText().toString().trim();
                String pass = mPasswordField.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                final String token_id = FirebaseInstanceId.getInstance().getToken();
                                String current_id = mAuth.getCurrentUser().getUid();

                                Map<String, Object> tokenMap = new HashMap<>();
                                tokenMap.put("token_id", token_id);

                                final String[] position = new String[1];
                                final String[] faculty = new String[1];
                                final String[] ID = new String[1];
                                final String[] department = new String[1];
                                mFirestore.collection("Users").document(current_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        position[0] = (String) documentSnapshot.get("position");
                                        faculty[0] = (String) documentSnapshot.get("faculty");
                                        ID[0] = (String) documentSnapshot.get("ID");
                                        department[0] = (String) documentSnapshot.get("department");
                                        if (position[0].equals("head")) {
                                            mdb.child("faculty").child(faculty[0]).child(department[0]).child("token").setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                }
                                            });
                                        } else {
                                            mdb.child("faculty").child(faculty[0]).child("head").child("token").setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                                    mProgressBar.setVisibility(View.INVISIBLE);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);
                                                }
                                            });
                                        }



                                    }
                                });

                            } else {
                                Toast.makeText(LoginActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
