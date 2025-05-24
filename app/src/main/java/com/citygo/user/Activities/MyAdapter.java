package com.citygo.user.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.citygo.user.R;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {

    public MyAdapter(Context context, ArrayList<String> dataList) {
        super(context, R.layout.layout_file, dataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_file, parent, false);
        }

        TextView textViewItem = convertView.findViewById(R.id.textViewItem);
        String item = getItem(position);
        if (item != null) {
            textViewItem.setText(item);
        }

        return convertView;
    }
}
