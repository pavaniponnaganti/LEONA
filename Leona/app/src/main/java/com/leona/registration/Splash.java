package com.leona.registration;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.leona.R;
import com.leona.home.Home;
import com.leona.services.PlayLocation;
import com.leona.utils.AppMethods;
import com.leona.utils.Appintegers;
import com.leona.utils.PermessionUtil;
import com.leona.utils.SessionManager;

public class Splash extends AppCompatActivity {

    private TextView version_code;
    SessionManager session;
    private LocationManager locationManager;
    private String TAG = "Spalsh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManager(this);
        AppMethods.getKeyHash(Splash.this);
        showGpsAlert();




        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "FCM Registration Token: " + token);
        Log.i(TAG, "FCM Registration Time: " + AppMethods.getcurrentDateUTC());

    }




    public void showGpsAlert(){

        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            alertMessageNoGps(Splash.this);

        }else{
            locationfetch();
            //startLocationService();
            initData();

        }
    }

    private void initData() {

        version_code=(TextView) findViewById(R.id.activity_splash_version);
        version_code.setText(AppMethods.getVersionCode(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(session.isUserLoggedIn()){

                    Intent intent = new Intent(Splash.this, Home.class);
                    startActivity(intent);

                }else{

                    if(session.isTutorialseen()){

                        Intent intent = new Intent(Splash.this, WelcomeScreen.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Splash.this, LanguageScreen.class);
                        startActivity(intent);
                    }

                }




            }
        }, Appintegers.SPLASH_TIME_OUT);





    }


    public void locationfetch() {

        if (PermessionUtil.locationInitialCheck(Splash.this)) {
            PermessionUtil.locationPermisson(Splash.this);
        } else {
            if (!isLocationServiceRunning()) {
                startLocationService();
            }
        }

    }

    public void startLocationService(){
        //AppLog.LOGV("Location Service");
        startService(new Intent(Splash.this , PlayLocation.class));
    }
    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(".services.PlayLocation".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    public  void alertMessageNoGps(final Activity activity) {

        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Splash.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Splash.this);
        }
          builder.setMessage(activity.getString(R.string.alert_no_gps))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        try {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();

                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onRestart() {

        showGpsAlert();
        super.onRestart();
    }
}
