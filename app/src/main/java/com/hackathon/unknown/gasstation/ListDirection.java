package com.hackathon.unknown.gasstation;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListDirection extends AppCompatActivity {

    ListView lvDirections;
    ArrayList<DirectionStep> arrayList;
    ItemDirections itemDirections;

    DatabaseSQL db;
    ImageButton imgbtnLove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_direction);

        arrayList = new ArrayList<>();
        lvDirections = (ListView) findViewById(R.id.listViewDirection);

        imgbtnLove = (ImageButton) findViewById(R.id.imageButtonLove);
        imgbtnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbtnLove.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.affeact_button));

                db = new DatabaseSQL(getApplicationContext());

                SQLiteDatabase database = db.getWritableDatabase();

        /*String insertLopHoc = "INSERT INTO lophocandroid (tenlop,namhoc)" + "VALUES (\"" + tenlop + "\"," + namhoc + ");";
        database.execSQL(insertLopHoc);*/

                ContentValues values = new ContentValues();
                values.put("Name", getIntent().getExtras().getString("data1"));
                values.put("Address", getIntent().getExtras().getString("data2"));

                database.insert("AndroidK217T26", null, values);

                Toast.makeText(ListDirection.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                
            }
        });

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
