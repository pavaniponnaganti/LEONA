package com.leona.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;



/**
 * Created by krify on 17/1/17.
 */

public class PlayLocation extends Service implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {
    private static final String TAG = PlayLocation.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    Context ctx;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private int UPDATE_INTERVAL = 5000; // 10 sec
    private int FATEST_INTERVAL = 10000; // 5 sec
    private int DISPLACEMENT = 10; // 10 meters
    public static double playlat = 0.0;
    public static double playlog = 0.0;
    LocalBroadcastManager broadcaster;


    //SessionManager session;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("loca", "Play Location");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        broadcaster = LocalBroadcastManager.getInstance(getApplicationContext());
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        Log.i("loca", "Play Location start command");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
            Log.i("loca", "Play Location connections updates");
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        Log.i("loca", "Play Location Changed");

        // Displaying the new location on UI
        displayLocation();
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            playlat = latitude;
            playlog = longitude;

            broadCastLocation(String.valueOf(latitude), String.valueOf(longitude));
        } else {

        }
    }


    public void broadCastLocation(String lat, String lng) {

        broadcaster = LocalBroadcastManager.getInstance(getApplicationContext());
        if (broadcaster != null) {
            Intent ii = new Intent("update");
            ii.putExtra("lat", lat);
            ii.putExtra("lng", lng);
            broadcaster.sendBroadcast(ii);
            Log.e(TAG, "Current Location" + lat + "----" + lng);

        } else {
        }

    }

    // Method to toggle periodic location updates

    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text

            //	mRequestingLocationUpdates = false;

            // Stopping the location updates
            //	stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

   //Creating google api client object

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    // Creating location request object

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    // Method to verify google play services on the device

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // GooglePlayServicesUtil.getErrorDialog(resultCode,getApplicationContext(),
                // PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                // Toast.makeText(getApplicationContext(),
                // "This device is not supported.", Toast.LENGTH_LONG)
                // .show();
                // finish();
            }
            return false;
        }
        return true;
    }

    //Starting the location updates

    protected void startLocationUpdates() {

        if (LocationServices.FusedLocationApi != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.e(TAG, "startLocationUpdates:  service failed" );

                    return;
                }

                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (checkPlayServices()) {

                    // Building the GoogleApi client
                    buildGoogleApiClient();

                    createLocationRequest();
                }

                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                }
            }

        }
        else{
            if (checkPlayServices()) {

                // Building the GoogleApi client
                buildGoogleApiClient();

                createLocationRequest();
            }

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }

    }


     // Stopping location updates

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub
        this.stopSelf();
        return super.stopService(name);
    }

}
