package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.citygo.user.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Userbooking extends AppCompatActivity {

    ImageView imageView;

    private SharedPreferences sharedPreferences;
    private ArrayList<String> dataList;
    private ListView listView;
    private BookingListAdapter bookingListAdapter;
    private Button clearButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userbooking2);

        imageView = findViewById(R.id.arrowback3);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(Userbooking.this, MainActivity.class);
                startActivity(myintent);
            }
        });

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String savedData = sharedPreferences.getString("data", "");
        if (!savedData.isEmpty()) {
            String[] entries = savedData.split("\t");
            dataList = new ArrayList<>(Arrays.asList(entries));
        } else {
            dataList = new ArrayList<>();
        }


        listView = findViewById(R.id.listview);
        bookingListAdapter = new BookingListAdapter(dataList);
        listView.setAdapter(bookingListAdapter);

        clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the shared preferences data
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("data");
                editor.apply();

                // Clear the data list and update the adapter
                dataList.clear();
                bookingListAdapter.notifyDataSetChanged();
            }
        });
    }

    private static class ViewHolder {
        TextView textView;
    }

    private class BookingListAdapter extends BaseAdapter {
        private ArrayList<String> dataList;

        public BookingListAdapter(ArrayList<String> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("MissingInflatedId")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.mylists, parent, false);

                holder = new ViewHolder();
                holder.textView = convertView.findViewById(R.id.textview1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String entry = dataList.get(position);
            holder.textView.setText(entry);

            return convertView;
        }
    }
}
