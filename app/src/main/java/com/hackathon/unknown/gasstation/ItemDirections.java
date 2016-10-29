package com.hackathon.unknown.gasstation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemDirections extends ArrayAdapter {
    Activity activity;
    int layoutId;
    ArrayList<DirectionStep> arrayList;

    public ItemDirections(Activity activity, int layoutId, ArrayList<DirectionStep> arrayList) {
        super(activity, layoutId, arrayList);
        this.activity = activity;
        this.layoutId = layoutId;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = View.inflate(activity, R.layout.activity_item_directions, null);

        TextView textViewInstruction = (TextView) view.findViewById(R.id.textViewInstruction);
        TextView textViewTravelMode = (TextView) view.findViewById(R.id.textViewTravelMode);
        TextView textViewDistance = (TextView) view.findViewById(R.id.textViewDistance);
        TextView textViewDuration = (TextView) view.findViewById(R.id.textViewDuration);

        textViewInstruction.setText(arrayList.get(position).getInstruction().trim());
        textViewTravelMode.setText(arrayList.get(position).getTravelMode().trim());
        textViewDistance.setText(arrayList.get(position).getDistance().trim());
        textViewDuration.setText(arrayList.get(position).getDuration().trim());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, ""+arrayList.get(position).getLatLngStart(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
