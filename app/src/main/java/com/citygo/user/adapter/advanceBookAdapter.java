package com.citygo.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.citygo.user.Activities.Advancebooktrack;
import com.citygo.user.DataClass.nodeDataClass;
import com.citygo.user.MyApplication;
import com.citygo.user.databinding.RowAdvanceBookBinding;
import com.citygo.user.databinding.RowDriverPrebookBinding;
import com.citygo.user.model.advanceBookRVModel;

import java.util.List;

public class advanceBookAdapter extends RecyclerView.Adapter<advanceBookAdapter.viewHolder> {

    Context context;
     RowAdvanceBookBinding binding;
     List<advanceBookRVModel> list;

    public advanceBookAdapter(Context context, List<advanceBookRVModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowAdvanceBookBinding.inflate(LayoutInflater.from(context),parent,false);
        return new viewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        advanceBookRVModel model = list.get(position);

        String driverName = model.getName();
        String driverPhNo = model.getNumber();
        String email = model.getEmail();
        String driverJoinDate = MyApplication.formatTimestamp(model.getTimeStamp());
        Double rating = model.getRating();
        String formattedNumber = String.format("%.1f", rating);
        String fromWhere = model.getPassengerFromWhere();
        String toWhere = model.getPassengerToWhere();
        String passengerPickupTime = MyApplication.parseTimeStamp(model.getPickupTimestamp())[1]+" "+MyApplication.parseTimeStamp(model.getPickupTimestamp())[2];
        String passengerPickUpDate = MyApplication.parseTimeStamp(model.getPickupTimestamp())[0];
        String node = model.getNode();
        String driverUid = model.getUid();

        holder.driverName.setText(driverName);
        holder.driverPhNo.setText(driverPhNo);
        holder.driverEmail.setText(email);
        holder.driverJoinDate.setText(driverJoinDate);
        holder.driverRating.setText(formattedNumber);
        holder.passengerFromWhere.setText(fromWhere);
        holder.passengerToWhere.setText(toWhere);
        holder.passengerPickupTime.setText(passengerPickupTime);
        holder.passengerPickUpDate.setText(passengerPickUpDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (node.equals(nodeDataClass.APPROVED)){
                    Intent intent =new Intent(context.getApplicationContext(), Advancebooktrack.class);
                    intent.putExtra("driverUid",driverUid);
                    context.startActivity(intent);
                } else if (node.equals(nodeDataClass.REQUESTS)) {
                    Intent intent =new Intent(context.getApplicationContext(), Advancebooktrack.class);
                    intent.putExtra("driverUid",driverUid);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView driverName,driverPhNo,driverEmail,driverJoinDate,driverRating
                ,passengerFromWhere,passengerToWhere,passengerPickupTime,
        passengerPickUpDate;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = binding.nameTv;
            driverPhNo = binding.phNoTv;
            driverEmail = binding.emailTv;
            driverJoinDate = binding.JoiningDateTV;
            driverRating = binding.ratingTv;
            passengerFromWhere = binding.passengerFromWhere;
            passengerToWhere = binding.passengerToWhere;
            passengerPickupTime = binding.pickUpTime;
            passengerPickUpDate = binding.pickUpDate;
        }
    }
}
