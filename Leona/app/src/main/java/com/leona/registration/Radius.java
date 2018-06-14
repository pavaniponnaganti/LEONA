package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.leona.R;
import com.leona.home.Home;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.leona.widget.DiscreteSeekBar;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Radius extends AppCompatActivity implements View.OnClickListener, AsyncHttpResponse.AsyncHttpResponseListener {
    DiscreteSeekBar seekBar;
    String TAG = "Radius";
    Button save_btn;

    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radius);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        toolbar.setSubtitle("");

        initData();
    }

    public void initData() {

        sm = new SessionManager(Radius.this);
        seekBar = (DiscreteSeekBar) findViewById(R.id.distance_seekbar);

        save_btn = (Button) findViewById(R.id.content_radious_save_btn);
        save_btn.setOnClickListener(this);



        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

                seekBar.setIndicatorFormatter(AppMethods.getRange(value));


            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.content_radious_save_btn:

                validat();

                break;


        }

    }

    public void validat() {

        if(!AppMethods.isConnectingToInternet(this)){

            AppMethods.alertForNoInternet(this);

        }else {


            if (seekBar.getProgress() <= Appintegers.Minradius) {

                AppMethods.showToast(Radius.this, getResources().getString(R.string.validate_radius_min));

            } else {
                saveList();
            }
        }
    }

    public void saveList() {

        RequestParams params = RestMethods.saveRadiusParams(Radius.this, sm.getStringData(AppStrings.Responsedata.userID), AppMethods.getDistanceinKM(seekBar.getProgress()));

        AsyncHttpResponse response = new AsyncHttpResponse(Radius.this, true);

        response.postAsyncHttp(RestApis.DistanceRange, params, sm.getStringData(AppStrings.Responsedata.token) ,false);

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL,boolean status) throws JSONException {


        if (URL.equals(RestApis.DistanceRange)) {

            checkStatus(response);
        }
    }


    private void checkStatus(String response) {


        if (RestMethods.checkStatus(Radius.this, response)) {

            startActivity(new Intent(Radius.this, Home.class));

        } else {

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {




            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
