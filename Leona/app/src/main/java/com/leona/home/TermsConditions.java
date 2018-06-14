package com.leona.home;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.leona.R;
import com.leona.models.InfoHelpModel;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TermsConditions extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener{

    SessionManager sm;
    TextView tc_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);


        initData();

    }

    private void initData() {

        sm = new SessionManager(TermsConditions.this);
        tc_tv = (TextView)findViewById(R.id.termscond_tv);


        callTCApi(true);
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

    public void callTCApi(boolean status) {
        if (!AppMethods.isConnectingToInternet(TermsConditions.this)) {
            AppMethods.alertForNoInternet(TermsConditions.this);
        } else {


            RequestParams params =  new RequestParams();

            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.termAndConditions, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }
    }


    @Override
    public void onAsyncHttpResponseGet(String response, String URL, boolean status) throws JSONException {
        if(URL.equals(RestApis.termAndConditions
        )){
            insertData(response);
        }
    }

    public void setData(String data){

        try {
            JSONObject jsonobj = new JSONObject(data);
            tc_tv.setText(jsonobj.getString(AppStrings.Responsedata.description));

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void insertData(String response) {



        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {

                String description = jo.optString(AppStrings.Responsedata.description);

                setData(description);
//                home_srl.setRefreshing(false);

            } else {
//                home_srl.setRefreshing(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
