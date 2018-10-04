package com.example.android.notificationexample;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Button mLogoutBtn, mEditBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CircleImageView mProfileImage;
    private TextView mProfileName;
    private ProgressBar mProgressBar;

    private String mUserId;

    private DatabaseReference mdb;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mLogoutBtn = view.findViewById(R.id.logout_btn);
        mEditBtn = view.findViewById(R.id.editprofile_button);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference();

        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProfileImage = view.findViewById(R.id.profile_image);
        mProfileName = view.findViewById(R.id.profile_name);
        mProgressBar = view.findViewById(R.id.profile_progressBar);

        mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String user_name = documentSnapshot.getString("name");
                String user_image = documentSnapshot.getString("image");

                mProfileName.setText(user_name);
                RequestOptions placeholderOption = new RequestOptions();
                placeholderOption.placeholder(R.drawable.profile);
                Glide.with(container.getContext()).setDefaultRequestOptions(placeholderOption).load(user_image).into(mProfileImage);

            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                /*Map<String, Object> tokenMapRemove = new HashMap<>();
                tokenMapRemove.put("token_id", FieldValue.delete());
                mFirestore.collection("Users").document(mUserId).update(tokenMapRemove).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mAuth.signOut();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        *//*Intent loginIntent = new Intent(container.getContext(), LoginActivity.class);
                        startActivity(loginIntent);*//*

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(container.getContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });*/

                String current_id = mAuth.getCurrentUser().getUid();
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
                            mdb.child("faculty").child(faculty[0]).child(department[0]).child("token").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mAuth.signOut();
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });

                        } else {
                            mdb.child("faculty").child(faculty[0]).child("head").child("token").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mAuth.signOut();
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                    }
                });

            }
        });

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
}
