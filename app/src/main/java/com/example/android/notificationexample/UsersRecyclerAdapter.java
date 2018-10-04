package com.example.android.notificationexample;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>{

    private List<Users> usersList;
    private Context context;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public UsersRecyclerAdapter(Context context, List<Users> usersList, DatabaseReference db, FirebaseAuth auth, FirebaseFirestore firebaseFirestore) {
        this.usersList = usersList;
        this.context = context;
        this.db = db;
        this.mAuth = auth;
        this.mFirestore = firebaseFirestore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //holder.user_name_view.setText(usersList.get(position).getName());

        //CircleImageView user_image_view = holder.user_image_view;
        //Glide.with(context).load(usersList.get(position).getImage()).into(user_image_view);

        final String user_id = usersList.get(position).getFromuserid();
        String user_name = usersList.get(position).getName();
        final String message = usersList.get(position).getMessage();
        final String date = usersList.get(position).getDate();
        final String key = usersList.get(position).userId;
        final String vehicleNo = usersList.get(position).getVehicleNo();
        String sd = usersList.get(position).getSdate();
        String ed = usersList.get(position).getEdate();
        final String startd = sd.substring(0, 2) + sd.substring(3, 5) + sd.substring(6);
        final String endd = ed.substring(0, 2) + ed.substring(3, 5) + ed.substring(6);

        holder.user_name_view.setText("Name: " + user_name + "(" + usersList.get(position).getDepartment() +")");
        holder.vehicles_view.setText("Vehicle: " + usersList.get(position).getVehicle());
        holder.start_date_view.setText("From: " + usersList.get(position).getSdate() + " " + usersList.get(position).getStime());
        holder.end_date_view.setText("To: " + usersList.get(position).getEdate() + " " + usersList.get(position).getEtime());
        holder.passenger_view.setText("No of Passengers: " + usersList.get(position).getPassengers());
        holder.reason_view.setText("Reason: " + message);

        holder.confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.child("UserIdentities").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String indexNo = (String) dataSnapshot.getValue();

                        final DatabaseReference ref = db.child("Users").child(indexNo).child("notifications");
                        final Map notification = new HashMap<>();
                        final Map notificationtodean = new HashMap<>();

                        notification.put("fromuserid", usersList.get(position).getFromuserid());
                        notification.put("message", message);
                        notification.put("date", date);
                        notification.put("stime", usersList.get(position).getStime());
                        notification.put("etime", usersList.get(position).getEtime());
                        notification.put("respond", "true");
                        notification.put("passengers", usersList.get(position).getPassengers());
                        notification.put("status", "confirmed");
                        notification.put("name", usersList.get(position).getName());
                        notification.put("faculty", usersList.get(position).getFaculty());
                        notification.put("department", usersList.get(position).getDepartment());
                        notification.put("indexNo", usersList.get(position).getIndexNo());
                        notification.put("vehicle", usersList.get(position).getVehicle());
                        notification.put("vehicleNo", usersList.get(position).getVehicleNo());
                        notification.put("sdate", usersList.get(position).getSdate());
                        notification.put("edate", usersList.get(position).getEdate());

                        notificationtodean.put("fromuserid", usersList.get(position).getFromuserid());
                        notificationtodean.put("message", message);
                        notificationtodean.put("date", date);
                        notificationtodean.put("stime", usersList.get(position).getStime());
                        notificationtodean.put("etime", usersList.get(position).getEtime());
                        notificationtodean.put("respond", "false");
                        notificationtodean.put("passengers", usersList.get(position).getPassengers());
                        notificationtodean.put("name", usersList.get(position).getName());
                        notificationtodean.put("faculty", usersList.get(position).getFaculty());
                        notificationtodean.put("department", usersList.get(position).getDepartment());
                        notificationtodean.put("indexNo", usersList.get(position).getIndexNo());
                        notificationtodean.put("vehicle", usersList.get(position).getVehicle());
                        notificationtodean.put("vehicleNo", usersList.get(position).getVehicleNo());
                        notificationtodean.put("sdate", usersList.get(position).getSdate());
                        notificationtodean.put("edate", usersList.get(position).getEdate());

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
                                    db.child("faculty").child(faculty[0]).child(department[0]).child("notifications").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.child("faculty").child(faculty[0]).child(department[0]).child("respondnotifications").child(key).updateChildren(notification);

                                            db.child("faculty").child(faculty[0]).child("head").child("notifications").child(key).updateChildren(notificationtodean);
                                            usersList.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    });

                                } else {
                                    db.child("faculty").child(faculty[0]).child("head").child("notifications").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.child("faculty").child(faculty[0]).child("head").child("respondnotifications").child(key).updateChildren(notification);
                                            usersList.remove(position);
                                            notifyDataSetChanged();
                                            ref.child(key).updateChildren(notification).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    db.child("Vehicles").child(vehicleNo).child("bookdates").child(startd).setValue(endd).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            DatabaseReference dd = FirebaseDatabase.getInstance().getReference();
                                                            dd.child("Users").child(indexNo).child("pendingnotifications").child(key).removeValue();
                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    });
                                }

                            }
                        });

                        /*ref.push().setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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
                                            db.child("faculty").child(faculty[0]).child(department[0]).child("notifications").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.child("faculty").child(faculty[0]).child(department[0]).child("respondnotifications").push().setValue(notification);
                                                    usersList.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                            });

                                        } else {
                                            db.child("faculty").child(faculty[0]).child("head").child("notifications").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    db.child("faculty").child(faculty[0]).child("head").child("respondnotifications").push().setValue(notification);
                                                    usersList.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                });
                            }
                        });*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        holder.reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child("UserIdentities").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String indexNo = (String) dataSnapshot.getValue();

                        DatabaseReference ref = db.child("Users").child(indexNo).child("notifications");
                        final Map notification = new HashMap<>();

                        notification.put("fromuserid", usersList.get(position).getFromuserid());
                        notification.put("message", message);
                        notification.put("date", date);
                        notification.put("stime", usersList.get(position).getStime());
                        notification.put("etime", usersList.get(position).getEtime());
                        notification.put("respond", "true");
                        notification.put("passengers", usersList.get(position).getPassengers());
                        notification.put("status", "rejected");
                        notification.put("name", usersList.get(position).getName());
                        notification.put("faculty", usersList.get(position).getFaculty());
                        notification.put("department", usersList.get(position).getDepartment());
                        notification.put("indexNo", usersList.get(position).getIndexNo());
                        notification.put("vehicle", usersList.get(position).getVehicle());
                        notification.put("vehicleNo", usersList.get(position).getVehicleNo());
                        notification.put("sdate", usersList.get(position).getSdate());
                        notification.put("edate", usersList.get(position).getEdate());


                        ref.child(key).updateChildren(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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
                                            db.child("faculty").child(faculty[0]).child(department[0]).child("notifications").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    usersList.remove(position);
                                                    notifyDataSetChanged();
                                                    db.child("faculty").child(faculty[0]).child(department[0]).child("respondnotifications").child(key).updateChildren(notification).addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            DatabaseReference dd = FirebaseDatabase.getInstance().getReference();
                                                            dd.child("Users").child(indexNo).child("pendingnotifications").child(key).removeValue();
                                                        }
                                                    });

                                                }
                                            });

                                        } else {
                                            db.child("faculty").child(faculty[0]).child("head").child("notifications").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    usersList.remove(position);
                                                    notifyDataSetChanged();
                                                    db.child("faculty").child(faculty[0]).child("head").child("respondnotifications").child(key).updateChildren(notification).addOnSuccessListener(new OnSuccessListener() {
                                                        @Override
                                                        public void onSuccess(Object o) {
                                                            DatabaseReference dd = FirebaseDatabase.getInstance().getReference();
                                                            dd.child("Users").child(indexNo).child("pendingnotifications").child(key).removeValue();
                                                        }
                                                    });
                                                }
                                            });
                                        }

                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(context, SendActivity.class);
                sendIntent.putExtra("user_id", user_id);
                sendIntent.putExtra("user_name", user_name);
                context.startActivity(sendIntent);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private CircleImageView user_image_view;
        private TextView user_name_view, vehicles_view, start_date_view, end_date_view, reason_view, passenger_view;
        private Button confirm_btn, reject_btn;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            user_name_view = mView.findViewById(R.id.user_list_name);
            vehicles_view = mView.findViewById(R.id.user_list_vehicles);
            start_date_view = mView.findViewById(R.id.user_list_start_date);
            end_date_view = mView.findViewById(R.id.user_list_end_date);
            reason_view = mView.findViewById(R.id.user_list_reason);
            passenger_view = mView.findViewById(R.id.user_list_passengers);
            confirm_btn = mView.findViewById(R.id.user_list_confirm_btn);
            reject_btn = mView.findViewById(R.id.user_list_reject_btn);

        }
    }
}
