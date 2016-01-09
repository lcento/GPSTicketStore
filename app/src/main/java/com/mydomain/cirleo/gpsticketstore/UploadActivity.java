package com.mydomain.cirleo.gpsticketstore;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by lcento on 09/01/2016.
 */
public class UploadActivity extends AppCompatActivity {
    private TextView textName;
    private TextView textAddress;
    private TextView textDescr;
    private TextView latitude;
    private TextView longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textName = new TextView(this);
        textName.setTextColor(Color.parseColor("#FFFFFF"));
        textName.setPadding(5, 0, 5, 0);

        textAddress = new TextView(this);
        textAddress.setTextColor(Color.parseColor("#FFFFFF"));
        textAddress.setPadding(5, 0, 5, 0);

        textDescr = new TextView(this);
        textDescr.setTextColor(Color.parseColor("#FFFFFF"));
        textDescr.setPadding(5, 0, 5, 0);

        latitude = new TextView(this);
        latitude.setTextColor(Color.parseColor("#FFFFFF"));
        latitude.setPadding(5, 0, 5, 0);

        longitude = new TextView(this);
        longitude.setTextColor(Color.parseColor("#FFFFFF"));
        longitude.setPadding(5, 0, 5, 0);

        listGpsData();
    }

    private void listGpsData() {
        TableLayout tl = (TableLayout) findViewById(R.id.gpsDataLines);

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        textName.setText("Nome");
        textAddress.setText("Indirizzo");
        textDescr.setText("Descrizione");
        latitude.setText("12.345");
        longitude.setText("13.450");

        tr.addView(textName, 0);
        tr.addView(textAddress, 1);
        tr.addView(textDescr, 2);
        tr.addView(latitude, 3);
        tr.addView(longitude, 4);

        tl.addView(tr);
    }
}
