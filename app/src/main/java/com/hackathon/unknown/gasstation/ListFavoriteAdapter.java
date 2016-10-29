package com.hackathon.unknown.gasstation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListFavoriteAdapter extends ArrayAdapter {

    Activity activity;
    int layoutId;
    ArrayList<FavoriteObject> arrayList;

    public ListFavoriteAdapter(Activity activity, int layoutId, ArrayList<FavoriteObject> arrayList) {
        super(activity, layoutId, arrayList);
        this.activity = activity;
        this.layoutId = layoutId;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {

        v = View.inflate(activity, R.layout.activity_list_favorite_adapter, null);

        FavoriteObject object = arrayList.get(position);

        TextView txtName = (TextView) v.findViewById(R.id.textViewNameFav);
        TextView txtAddress = (TextView) v.findViewById(R.id.textViewAddressFav);

        txtName.setText(object.getName());
        txtAddress.setText(object.getAddress());

        return v;
    }
}
