package com.mydomain.cirleo.gpsticketstore;


import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

/**
 * Created by lcento on 09/01/2016.
 */
public class UploadActivity extends AppCompatActivity {
    private DbAdapter dbHelper;
    private Cursor cursor;
    private Button buttonSendGpsData;
    private HttpURLConnection urlConnection = null;
    private DataOutputStream printout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dbHelper = new DbAdapter(this);
        // Lista data form sqlite
        listGpsData();

        buttonSendGpsData = (Button)findViewById(R.id.buttonSendGpsData);
        buttonSendGpsData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncT asyncT = new AsyncT();
                asyncT.execute();
            }
        });
    }

    /* Inner class to get response */
    class AsyncT extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://10.0.2.2/jsonget.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();
                // Create JSONObject
                JSONObject jsonobj = new JSONObject();
                jsonobj.put("name", "Aneh");
                jsonobj.put("age", "22");
                // Send POST output
                printout = new DataOutputStream(urlConnection.getOutputStream());
                printout.write(Integer.parseInt(URLEncoder.encode(jsonobj.toString(), "UTF-8")));
                printout.flush ();
                printout.close ();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void listGpsData() {
        TableLayout tl = (TableLayout) findViewById(R.id.gpsDataLines);
        TableRow tr = new TableRow(this);

        try {
            dbHelper.open();
            cursor = dbHelper.fetchAllGpsData();

            int rows = cursor.getCount();
            int cols = cursor.getColumnCount();

            while (cursor.moveToNext()) {
                for (int i = 0; i < rows; i++) {
                    tr = new TableRow(this);
                    tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tr.setBackgroundResource(R.drawable.row_border);

                    for(int j = 1; j < cols; j++) {
                        TextView tv = new TextView(this);
                        tv.setGravity(Gravity.LEFT);
                        tv.setTextSize(16);
                        tv.setPadding(15, 5, 15, 5);
                        tv.setTextColor(Color.BLUE);
                        tv.setTypeface(null, Typeface.BOLD);

                        tv.setText(cursor.getString(j));

                        tr.addView(tv);
                    }
                }

                tl.addView(tr);

                /*String name = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_NAME));
                String address = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_ADDRESS));
                String description = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_DESCRIPTION));
                String latitude = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_LATITUDE));
                String longitude = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_LONGITUDE));*/
            }

            dbHelper.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
