package com.example.android.notificationexample;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<Notifications> mNotifList;

    private FirebaseFirestore firebaseFirestore;
    private Context context;

    public NotificationsAdapter(Context context, List<Notifications> mNotifList) {
        this.mNotifList = mNotifList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notification, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        firebaseFirestore = FirebaseFirestore.getInstance();

        String from_id = mNotifList.get(position).getFrom();

        holder.mNotifVehicle.setText("Vehicle: " + mNotifList.get(position).getVehicle());
        holder.mNotifName.setText("Name: " + mNotifList.get(position).getName() + "(" + mNotifList.get(position).getDepartment() + ")");
        holder.mNotifStatus.setText(mNotifList.get(position).getStatus());
        holder.mNotifStartDate.setText("From: " + mNotifList.get(position).getSdate() + " " + mNotifList.get(position).getStime());
        holder.mNotifEndDate.setText("To: " + mNotifList.get(position).getEdate() + " " + mNotifList.get(position).getEtime());
        holder.mNotifPassenger.setText("No of Passengers: " + mNotifList.get(position).getPassengers());
        holder.mNotifReason.setText("Reason: " + mNotifList.get(position).getMessage());
        if (mNotifList.get(position).getStatus().equals("rejected")) {
            holder.mNotifStatus.setTextColor(Color.RED);
        } else {
            holder.mNotifStatus.setTextColor(Color.BLUE);
        }
        /*firebaseFirestore.collection("Users").document(from_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String name = documentSnapshot.getString("name");
                String image = documentSnapshot.getString("image");

                holder.mNotifName.setText(name);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profile);

                Glide.with(context).setDefaultRequestOptions(requestOptions).load(image).into(holder.mNotifImage);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return mNotifList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView mNotifName, mNotifReason, mNotifStartDate, mNotifEndDate, mNotifStatus,
                mNotifPassenger, mNotifVehicle;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mNotifName = mView.findViewById(R.id.notif_list_name);
            mNotifStartDate = mView.findViewById(R.id.notif_list_start_date);
            mNotifEndDate = mView.findViewById(R.id.notif_list_end_date);
            mNotifReason = mView.findViewById(R.id.notif_list_reason);
            mNotifVehicle = mView.findViewById(R.id.notif_list_vehicles);
            mNotifStatus = mView.findViewById(R.id.notif_list_status);
            mNotifPassenger = mView.findViewById(R.id.notif_list_passengers);

        }
    }

}
