package com.mydomain.cirleo.gpsticketstore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lcento on 06/01/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gpsdatabase.db";
    private static final int DATABASE_VERSION = 1;

    // SQL create database
    private static final String DATABASE_CREATE = "create table gps_main (_id integer primary key autoincrement, name text not null, " +
            "address text not null, description text not null, latitude integer not null, longitude integer not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS gps_main");
        onCreate(database);
    }
}
