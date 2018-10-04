package com.example.android.notificationexample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.profileLabel)
    TextView mProfileLabel;
    @BindView(R.id.usersLabel)
    TextView mUsersLabel;
    @BindView(R.id.notificationsLabel)
    TextView mNotificationsLabel;
    @BindView(R.id.mainPager)
    ViewPager mMainPager;

    private PagerViewAdapter mPagerViewAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, SelectActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    sendToLogin();

                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        /*FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendToLogin();
        }*/

        mMainPager.setOffscreenPageLimit(2);
        mPagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        mMainPager.setAdapter(mPagerViewAdapter);

        mProfileLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(0);
            }
        });

        mUsersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(1);
            }
        });

        mNotificationsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMainPager.setCurrentItem(2);
            }
        });

        mMainPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeTabs(int position) {
        if (position == 0) {
            mProfileLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            mProfileLabel.setTextSize(24);

            mUsersLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mUsersLabel.setTextSize(18);

            mNotificationsLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mNotificationsLabel.setTextSize(18);
        }

        if (position == 1) {
            mProfileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mProfileLabel.setTextSize(18);

            mUsersLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            mUsersLabel.setTextSize(24);

            mNotificationsLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mNotificationsLabel.setTextSize(18);
        }

        if (position == 2) {
            mProfileLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mProfileLabel.setTextSize(18);

            mUsersLabel.setTextColor(getResources().getColor(R.color.textTabLight));
            mUsersLabel.setTextSize(18);

            mNotificationsLabel.setTextColor(getResources().getColor(R.color.textTabBright));
            mNotificationsLabel.setTextSize(24);
        }
    }
}
