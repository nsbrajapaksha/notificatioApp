package com.example.android.notificationexample;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendActivity extends AppCompatActivity {

    @BindView(R.id.user_name_view)
    TextView user_name_view;
    @BindView(R.id.message_view)
    EditText mMessageView;
    @BindView(R.id.send_btn)
    Button mSendBtn;
    @BindView(R.id.message_progressBar)
    ProgressBar mMessageProgressBar;

    private String mUserId;
    private String mUserName;
    private String mCurrentId;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);

        mFirestore = FirebaseFirestore.getInstance();
        mCurrentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mUserId = getIntent().getStringExtra("user_id");
        mUserName = getIntent().getStringExtra("user_name");

        user_name_view.setText("Send to " + mUserName);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mMessageView.getText().toString().trim();

                if (!TextUtils.isEmpty(message)) {

                    mMessageProgressBar.setVisibility(View.VISIBLE);

                    Map<String, Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message", message);
                    notificationMessage.put("from", mCurrentId);

                    mFirestore.collection("Users").document(mUserId).collection("Notification")
                            .add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(SendActivity.this, "Notification Sent", Toast.LENGTH_LONG).show();
                            mMessageView.setText("");
                            mMessageProgressBar.setVisibility(View.INVISIBLE);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mMessageProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SendActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }
        });
    }
}
