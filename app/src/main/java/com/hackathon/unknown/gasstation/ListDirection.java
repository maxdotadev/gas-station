package com.hackathon.unknown.gasstation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListDirection extends AppCompatActivity {

    ListView lvDirections;
    ArrayList<DirectionStep> arrayList;
    ItemDirections itemDirections;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_direction);

        arrayList = new ArrayList<>();
        lvDirections = (ListView) findViewById(R.id.listViewDirection);

        Intent intent = getIntent();
        parseJSon(intent.getExtras().getString("data"));

        itemDirections = new ItemDirections(this, R.layout.activity_item_directions, arrayList);
        lvDirections.setAdapter(itemDirections);
        itemDirections.notifyDataSetChanged();
    }

    private void parseJSon(String s) {
        try {

            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            for (int i = 0; i < array.length(); i++) {

                DirectionStep step = new DirectionStep();
                step.setInstruction(Html.fromHtml(array.getJSONObject(i).getString("html_instructions")).toString());
                step.setDistance(array.getJSONObject(i).getJSONObject("distance").getString("text"));
                step.setDuration(array.getJSONObject(i).getJSONObject("duration").getString("text"));
                step.setTravelMode(array.getJSONObject(i).getString("travel_mode"));
                step.setLatLngStart(new LatLng(array.getJSONObject(i).getJSONObject("start_location").getDouble("lat"),
                        array.getJSONObject(i).getJSONObject("start_location").getDouble("lng")));

                arrayList.add(step);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
