package com.citygo.user.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.citygo.user.MyApplication;
import com.citygo.user.databinding.RowAdvanceBookBinding;
import com.citygo.user.model.userCurrentBookHistoryRVModel;

import java.util.List;

public class userCurrentBookHistoryAdapter extends RecyclerView.Adapter<userCurrentBookHistoryAdapter.viewHolder> {
    Context context;
    RowAdvanceBookBinding binding;
    List<userCurrentBookHistoryRVModel> list;


    public userCurrentBookHistoryAdapter(Context context, List<userCurrentBookHistoryRVModel> list) {
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
        userCurrentBookHistoryRVModel model = list.get(position);
        Long TimeStamp = model.getTimeStamp();
        String FromWhere = model.getFromWhere();
        String ToWhere = model.getToWhere();
        String DriverUid = model.getDriverUid();
        String passengerPickupTime = MyApplication.parseTimeStamp(TimeStamp)[1]+" "+MyApplication.parseTimeStamp(TimeStamp)[2];
        String passengerPickUpDate = MyApplication.parseTimeStamp(TimeStamp)[0];
        String DriverName = model.getDriverName();
        String DriverEmail = model.getDriverEmail();
        String DriverPhNo= model.getDriverNumber();
        String DriverRating = model.getDriverRating();
        String joinDate = model.getDriverJoinDate();




        holder.driverName.setText(DriverName);
        holder.driverEmail.setText(DriverEmail);
        holder.driverPhNo.setText(DriverPhNo);
        holder.driverRating.setText(DriverRating);
        holder.passengerFromWhere.setText(FromWhere);
        holder.passengerToWhere.setText(ToWhere);
        holder.passengerPickupTime.setText(passengerPickupTime);
        holder.passengerPickUpDate.setText(passengerPickUpDate);
        holder.driverJoinDate.setText(joinDate);
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
