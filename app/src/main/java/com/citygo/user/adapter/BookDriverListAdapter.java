package com.citygo.user.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.citygo.user.databinding.RowDriverPrebookBinding;
import com.citygo.user.Fragment.PrebookDriverDetailFragment;
import com.citygo.user.MyApplication;
import com.citygo.user.R;
import com.citygo.user.model.DriverModel;

import java.sql.Timestamp;
import java.util.List;

public class BookDriverListAdapter extends RecyclerView.Adapter<BookDriverListAdapter.BookDriverListViewHolder> {

    private RowDriverPrebookBinding binding;
    private Context context;
    private List<DriverModel> BookDriverList;
    private String passengerToWhere,passengerFromWhere,passengerUid,passengerLatLang,passengerName;
    Timestamp pickupTimestamp;

    public BookDriverListAdapter(Context context, List<DriverModel> bookDriverList, Timestamp pickupTimestamp, String passengerToWhere, String passengerFromWhere, String passengerUid, String passengerLatLang,String passengerName) {
        this.context = context;
        this.BookDriverList = bookDriverList;
        this.pickupTimestamp = pickupTimestamp;
        this.passengerToWhere = passengerToWhere;
        this.passengerFromWhere = passengerFromWhere;
        this.passengerUid=passengerUid;
        this.passengerLatLang=passengerLatLang;
        this.passengerName=passengerName;
    }

    @NonNull
    @Override
    public BookDriverListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowDriverPrebookBinding.inflate(LayoutInflater.from(context),parent,false);
        return new BookDriverListViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BookDriverListViewHolder holder, int position) {

        DriverModel model = BookDriverList.get(position);
        String name =model.getName();
        String number = model.getNumber();

        String isVerified = model.getIsVerified();
        String uid = model.getUid();
        String email = model.getEmail();
        String profileImage = model.getProfileImage();
        String DriverFcmToken = model.getFcmToken();
        String formattedDate = MyApplication.formatTimestamp(model.getTimeStamp());

        holder.tvName.setText(name);
        holder.Email.setText(email);
        holder.joiningDate.setText(formattedDate);
        holder.tvPhoneNumber.setText(number);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Load prebookDriverDetailFragment with addToBackStack(null)
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                Fragment view2 = new PrebookDriverDetailFragment();
                ft.replace(R.id.ActivityPreBookDriverListFL,view2);
                ft.addToBackStack(null);
                Bundle bundle = new Bundle();
                view2.setArguments(bundle);
                bundle.putString("DriverName",name);
                bundle.putString("DriverNumber",number);
                bundle.putString("isVerified",isVerified);
                bundle.putString("DriverUid",uid);
                bundle.putString("DriverEmail",email);
                bundle.putString("DriverImage",profileImage);
                bundle.putString("DriverFcmToken",DriverFcmToken);
                bundle.putString("formattedDate",formattedDate);
                bundle.putString("passengerUid",passengerUid);
                bundle.putSerializable("pickupTimestamp", pickupTimestamp);
                bundle.putString("passengerFromWhere",passengerFromWhere);
                bundle.putString("passengerToWhere",passengerToWhere);
                bundle.putString("passengerLatLang",passengerLatLang);
                bundle.putString("passengerName",passengerName);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return BookDriverList.size();
    }

    class BookDriverListViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPhoneNumber, joiningDate,Email;
        ImageView profileImage;
        public BookDriverListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = binding.nameTv;
            tvPhoneNumber = binding.phNoTv;
            joiningDate= binding.JoiningDateTV;
            profileImage= binding.profileImage;
            Email = binding.emailTv;
        }
    }
}
