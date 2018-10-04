package com.example.android.notificationexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DeanActivity extends AppCompatActivity {

    private DatabaseReference mdb;

    private Spinner deanfaculty;
    private EditText ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dean);

        deanfaculty = (Spinner) findViewById(R.id.dean_faculty);
        mdb = FirebaseDatabase.getInstance().getReference();
        ID = (EditText) findViewById(R.id.dean_id);
    }

    public void deannextclicked(View view) {
        String token_id = FirebaseInstanceId.getInstance().getToken();

        mdb.child("faculty").child(deanfaculty.getSelectedItem().toString().trim())
                .child("head").child("token").setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(DeanActivity.this, RegisterActivity.class);
                intent.putExtra("faculty", deanfaculty.getSelectedItem().toString().trim());
                intent.putExtra("ID", ID.getText().toString().trim());
                intent.putExtra("position", "dean");
                startActivity(intent);
            }
        });
    }
}
