package com.leona.app;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.leona.R;
import com.leona.widget.TextField;


import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by krify on 13/4/17.
 */

public class Appcontroler extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        FirebaseApp.initializeApp(getApplicationContext());


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Dosis-Medium.ttf")
                //.setDefaultFontPath("fonts/NTR.ttf")
                .setFontAttrId(R.attr.fontPath)
                .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                .build());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);




    }
}
