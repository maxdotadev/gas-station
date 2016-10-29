package com.hackathon.unknown.gasstation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseSQL extends SQLiteOpenHelper {

    private static final String DATABASENAME = "QLHS4";
    private static int DATABASEVERSION = 1;

    public DatabaseSQL(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate");
        String SQliteCreateTable = "CREATE TABLE [AndroidK217T26] (Name text NOT NULL,Address text NOT NULL)";
        db.execSQL(SQliteCreateTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("onUpgrade " + oldVersion + " va " + newVersion);

    }
}
