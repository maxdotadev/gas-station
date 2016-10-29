package com.hackathon.unknown.gasstation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class ListFavorite extends AppCompatActivity {

    ListView listFavorite;
    ArrayList<FavoriteObject> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite);
        arrayList = new ArrayList<>();

        listFavorite = (ListView) findViewById(R.id.listViewFavorite);

        DatabaseSQL db = new DatabaseSQL(getApplicationContext());
        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery("Select * from AndroidK217T26", null);

        if (cursor.getCount() != 0) {
            if (!cursor.isFirst()) {
                cursor.moveToFirst();
            }

            while (!cursor.isLast()) {
//            System.out.println(cursor.getString(0) + " va " + cursor.getString(1));
                FavoriteObject favoriteObject = new FavoriteObject(cursor.getString(0), cursor.getString(1));
                arrayList.add(favoriteObject);
                cursor.moveToNext();
            }
            if (cursor.isLast()) {
                FavoriteObject favoriteObject = new FavoriteObject(cursor.getString(0), cursor.getString(1));
                arrayList.add(favoriteObject);
            }

            ListFavoriteAdapter adapter = new ListFavoriteAdapter(this, R.layout.activity_list_favorite_adapter, arrayList);
            listFavorite.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }


    }
}
