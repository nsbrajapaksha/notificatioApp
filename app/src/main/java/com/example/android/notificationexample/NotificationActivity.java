package com.example.android.notificationexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.notif_text)
    TextView mNotifData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        String dataMessage = getIntent().getStringExtra( "message");
        String dataFrom = getIntent().getStringExtra("from_user_id");
        mNotifData.setText("FROM : " + dataFrom + " | MESSAGE : " + dataMessage);
    }
}
