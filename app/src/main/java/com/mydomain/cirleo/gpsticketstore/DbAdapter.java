package com.mydomain.cirleo.gpsticketstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by lcento on 06/01/2016.
 */
public class DbAdapter {
    @SuppressWarnings("unused")
    private static final String LOG_TAG = DbAdapter.class.getSimpleName();

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE = "gps_main";

    public static final String KEY_GPSMAINID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    public DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(String name, String address, String description, int latitude, int longitude) {
        ContentValues values = new ContentValues();
        values.put( KEY_NAME, name );
        values.put( KEY_ADDRESS, address );
        values.put( KEY_DESCRIPTION, description );
        values.put( KEY_LATITUDE, latitude );
        values.put(KEY_LONGITUDE, longitude);

        return values;
    }

    // create a gps_main
    public long createGpsMain(String name, String address, String description, int latitude, int longitude) {
        ContentValues initialValues = createContentValues(name, address, description, latitude, longitude);
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    //update a gps_main1
    public boolean updateGpsMain( long gpsmainID, String name, String address, String description, int latitude, int longitude) {
        ContentValues updateValues = createContentValues(name, address, description, latitude, longitude);
        return database.update(DATABASE_TABLE, updateValues, KEY_GPSMAINID + "=" + gpsmainID, null) > 0;
    }

    //delete a gps_main
    public boolean deleteGpsMain(long gpsmainID) {
        return database.delete(DATABASE_TABLE, KEY_GPSMAINID + "=" + gpsmainID, null) > 0;
    }

    //fetch all contacts
    public Cursor fetchAllContacts() {
        return database.query(DATABASE_TABLE, new String[] { KEY_GPSMAINID, KEY_NAME, KEY_ADDRESS, KEY_DESCRIPTION, KEY_LATITUDE, KEY_LONGITUDE}, null, null, null, null, null, null);
    }

    //fetch gps_main filter by a string
    public Cursor fetchContactsByFilter(String filter) {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                        KEY_GPSMAINID, KEY_NAME, KEY_ADDRESS, KEY_DESCRIPTION, KEY_LATITUDE, KEY_LONGITUDE },
                KEY_NAME + " like '%"+ filter + "%'", null, null, null, null, null, null);

        return mCursor;
    }
}
