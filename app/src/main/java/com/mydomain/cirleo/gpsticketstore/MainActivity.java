package com.mydomain.cirleo.gpsticketstore;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements GpsStatus.Listener {
    private double latitude;
    private double longitude;
    private TextView gpsCoord;
    private TextView gpsStatusLabel;
    private String textGps;
    private LocationManager mlocManager;
    private LocationListener mlocListener;
    private String provider;
    private View viewGpsStatus;
    private GpsStatus gpsStatus;
    private  Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        viewGpsStatus = (View) findViewById(R.id.viewGpsStatus);
        gpsCoord = (TextView) findViewById(R.id.textViewGpsCoordRead);
        gpsStatusLabel = (TextView) findViewById(R.id.textViewGpsStatus);

        anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        // Get the location manager
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Get location listener
        mlocListener = new CaptureGpsCoord();
        // Set location provider
        provider = LocationManager.GPS_PROVIDER;
        // Set Gps Listener
        if (checkPermission()) {
            mlocManager.addGpsStatusListener(this);
        }

    }

    private void setGpsCoord() {
        // try to read gps coords
        if (readGpsCoord()) {
            textGps = String.valueOf(String.format("%.5f",latitude)) + "," + String.valueOf(String.format("%.5f",longitude));
            gpsCoord.setText(textGps);
            setOkGpsStatus();
        }
        else {
            setErrGpsStatus();
        }
    }

    private void setErrGpsStatus() {
        AlertMessage("Alert Message", getResources().getString(R.string.alert_gps_location));
        gpsStatusLabel.setText(getResources().getString(R.string.content_main_gpsstatus_err_label));
        gpsStatusLabel.startAnimation(anim);
        viewGpsStatus.setBackgroundResource(R.drawable.circle_red);
        gpsCoord.setText(getResources().getString(R.string.alert_gps_location));
    }

    private void setOkGpsStatus() {
        gpsStatusLabel.setText(getResources().getString(R.string.content_main_gpsstatus_ok_label));
        gpsStatusLabel.clearAnimation();
        viewGpsStatus.setBackgroundResource(R.drawable.circle_green);
    }

    private boolean readGpsCoord() {
        if (checkPermission()) {
            Location location = mlocManager.getLastKnownLocation(provider);

            if (location != null) {
                mlocListener.onLocationChanged(location);

                latitude = CaptureGpsCoord.latitude;
                longitude = CaptureGpsCoord.longitude;

                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkPermission()) {
            mlocManager.requestLocationUpdates(provider, 1000, 0, mlocListener);
            // Set Gps Coordinate
            setGpsCoord();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (checkPermission()) {
            mlocManager.removeUpdates(mlocListener);
        }
    }

    @Override
    public void onGpsStatusChanged(int event) {
        gpsStatus = mlocManager.getGpsStatus(gpsStatus);
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
                Toast.makeText(this, "GPS_STARTED", Toast.LENGTH_SHORT).show();
                // Do Something with mStatus info
                break;

            case GpsStatus.GPS_EVENT_STOPPED:
                Toast.makeText(this, "GPS_STOPPED", Toast.LENGTH_SHORT).show();
                // Do Something with mStatus info
                break;

            case GpsStatus.GPS_EVENT_FIRST_FIX:
                setGpsCoord();
                Toast.makeText(this, "GPS_FIRST_FIX", Toast.LENGTH_SHORT).show();
                // Do Something with mStatus info
                break;

            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                Toast.makeText(this, "GPS_STATUS", Toast.LENGTH_SHORT).show();
                // Do Something with mStatus info
                break;
        }
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return false;
        }
        else {
            return true;
        }
    }

    private void AlertMessage(String tlt, String msg) {
        AlertDialog.Builder builddlg = new AlertDialog.Builder(this);
        builddlg.setMessage(msg);
        builddlg.setTitle(tlt);
        builddlg.setCancelable(true);

        builddlg.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        AlertDialog alertdlg = builddlg.create();
        alertdlg.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch(item.getItemId()) {
            case R.id.menu_upload:
                //Intent intent_upload = new Intent(this, UploadData.class);
                //this.startActivity(intent_upload);
                break;
            case R.id.menu_settings:
                //Intent intent_settings = new Intent(this, SettingsApp.class);
                //this.startActivity(intent_settings);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
