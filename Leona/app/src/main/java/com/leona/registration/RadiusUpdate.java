package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RadiusUpdate extends AppCompatActivity implements View.OnClickListener, AsyncHttpResponse.AsyncHttpResponseListener {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);

        toolbar.setSubtitle("");

        initData();
    }

    public void initData() {

        sm = new SessionManager(RadiusUpdate.this);
        seekBar = (DiscreteSeekBar) findViewById(R.id.distance_seekbar);

        save_btn = (Button) findViewById(R.id.content_radious_save_btn);
        save_btn.setOnClickListener(this);

        save_btn.setText(getString(R.string.update));

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

        getuserRange();

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

                AppMethods.showToast(RadiusUpdate.this, getResources().getString(R.string.validate_radius_min));

            } else {
                saveList();
            }
        }
    }

    public void saveList() {

        RequestParams params = RestMethods.saveRadiusParams(RadiusUpdate.this, sm.getStringData(AppStrings.Responsedata.userID), AppMethods.getDistanceinKM(seekBar.getProgress()));

        AsyncHttpResponse response = new AsyncHttpResponse(RadiusUpdate.this, true);

        response.postAsyncHttp(RestApis.DistanceRange, params, sm.getStringData(AppStrings.Responsedata.token),false);

    }
public void getuserRange() {

        RequestParams params = RestMethods.getRadiusParams(RadiusUpdate.this, sm.getStringData(AppStrings.Responsedata.userID));

        AsyncHttpResponse response = new AsyncHttpResponse(RadiusUpdate.this, true);

        response.postAsyncHttp(RestApis.userRange, params, sm.getStringData(AppStrings.Responsedata.token),false);

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL,boolean status) throws JSONException {


        if (URL.equals(RestApis.DistanceRange)) {

            checkStatus(response);
        }else if (URL.equals(RestApis.userRange)) {

            updateRadius(response);
        }
    }

    private void updateRadius(String response) {

        Log.e(TAG, "updateRadius: " + response);

        JSONObject jo = null;
        try {
            jo = new JSONObject(response);



        if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {


            seekBar.setProgress((int) (Double.parseDouble(jo.getString(AppStrings.Responsedata.distance) )*1000));

        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkStatus(String response) {


        if (RestMethods.checkStatus(RadiusUpdate.this, response)) {

            startActivity(new Intent(RadiusUpdate.this, Home.class));

        } else {

        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                //relaseAudio();

                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
