package com.example.android.notificationexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment {

    private RecyclerView mNotificationList;
    private NotificationsAdapter notificationsAdapter;

    private List<Notifications> mNotifList;
    private FirebaseFirestore mFirestore;
    private DatabaseReference mdb;
    private FirebaseAuth mAuth;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mNotifList = new ArrayList<>();

        mNotificationList = (RecyclerView) view.findViewById(R.id.notification_list);
        notificationsAdapter = new NotificationsAdapter(getContext(), mNotifList);

        mNotificationList.setHasFixedSize(true);
        mNotificationList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mNotificationList.setAdapter(notificationsAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

       /* mFirestore.collection("Users").document(current_user_id).collection("Notification")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                Notifications notifications = doc.getDocument().toObject(Notifications.class);
                                mNotifList.add(notifications);

                                notificationsAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });*/



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mNotifList.clear();

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
                    mdb.child("faculty").child(faculty[0]).child(department[0]).child("respondnotifications").orderByChild("respond").equalTo("true")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mNotifList.clear();
                                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                        //String name = (String) messageSnapshot.child("name").getValue();
                                        //String message = (String) messageSnapshot.child("message").getValue();
                                        String user_id = messageSnapshot.getKey();
                                        Notifications notifications = messageSnapshot.getValue(Notifications.class).withId(user_id);
                                        mNotifList.add(notifications);

                                        notificationsAdapter.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                } else {
                    mdb.child("faculty").child(faculty[0]).child("head").child("respondnotifications").orderByChild("respond").equalTo("true")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mNotifList.clear();
                                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                        //String name = (String) messageSnapshot.child("name").getValue();
                                        //String message = (String) messageSnapshot.child("message").getValue();
                                        String user_id = messageSnapshot.getKey();
                                        Notifications notifications = messageSnapshot.getValue(Notifications.class).withId(user_id);
                                        mNotifList.add(notifications);

                                        notificationsAdapter.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }

            }
        });
    }
}
