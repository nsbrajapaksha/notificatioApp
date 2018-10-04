package com.example.android.notificationexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
    }

    public void dheadclicked(View view) {
        Intent intent = new Intent(SelectActivity.this, HeadActivity.class);
        startActivity(intent);
    }

    public void deanclicked(View view) {
        Intent intent = new Intent(SelectActivity.this, DeanActivity.class);
        startActivity(intent);
    }

    public void loginclicked(View view) {
        Intent intent = new Intent(SelectActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
