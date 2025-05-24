package com.citygo.user.adapter;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


import com.citygo.user.model.PlaceApi;

import java.util.ArrayList;

public class PlaceAutoSuggestAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> resultList;
    private PlaceApi placeApi;

    public PlaceAutoSuggestAdapter(Context context, int resID) {
        super(context, resID);
        resultList = new ArrayList<>();
        placeApi = new PlaceApi();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int pos) {
        return resultList.get(pos);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = placeApi.autoComplete(constraint.toString());
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}
