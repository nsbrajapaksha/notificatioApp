package com.example.android.notificationexample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;

public class HeadActivity extends AppCompatActivity implements android.widget.AdapterView.OnItemSelectedListener{

    private DatabaseReference mdb;
    private EditText ID;

    private Spinner headfaculty, headdepartment;
    private ArrayList<String> listApplied = new ArrayList<String>(Arrays.asList("Boteny", "Chemistry",
            "Computer Science", "Forestry and Environmental Sciences",
            "Food Science and Technology", "Mathematics", "Physics",
            "Statistics", "Zoology", "Sport Science"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head);

        mdb = FirebaseDatabase.getInstance().getReference();
        ID = (EditText) findViewById(R.id.head_id);

        headfaculty = (Spinner) findViewById(R.id.head_faculty);
        headdepartment = (Spinner) findViewById(R.id.head_department);
        addListenerOnSpinnerItemSelection();
    }

    private void addListenerOnSpinnerItemSelection() {
        headfaculty.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void headnextclicked(View view) {
        String token_id = FirebaseInstanceId.getInstance().getToken();

        mdb.child("faculty").child(headfaculty.getSelectedItem().toString().trim())
                .child(headdepartment.getSelectedItem().toString().trim()).child("token").setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(HeadActivity.this, RegisterActivity.class);
                intent.putExtra("faculty", headfaculty.getSelectedItem().toString().trim());
                intent.putExtra("department", headdepartment.getSelectedItem().toString().trim());
                intent.putExtra("ID", ID.getText().toString().trim());
                intent.putExtra("position", "head");
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<String> list = new ArrayList<String>();
        if (adapterView.getItemAtPosition(i).toString().equals("Applied Sciences")) {
            list = listApplied;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        headdepartment.setAdapter(dataAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
