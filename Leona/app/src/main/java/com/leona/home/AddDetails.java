package com.leona.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leona.R;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddDetails extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener ,View.OnClickListener{

    ImageView content_discount_iv, content_discount_fav_iv;
    TextView content_discount_title_name_tv, content_discount_des_name_tv, content_discount_price_tv,
            content_discount_time_tv, content_discount_orginal_price_tv,content_discount_des;

 String adID;
    SessionManager sm;

    String latlng="";
    private CountDownTimer timer;

    RelativeLayout container_rl;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;
    private String TAG ="DIscount";

    int favouriteStatus=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);
        getSupportActionBar().setLogo(R.mipmap.logo_small);
        getSupportActionBar().setTitle("");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_discount_det_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirct2Maps();
            }
        });

        initData();
    }

    public void initData(){
        sm = new SessionManager(this);

        w = getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        adID =   getIntent().getStringExtra(AppStrings.Responsedata.adID);

        container_rl = (RelativeLayout) findViewById(R.id.content_discount_det_rl);
        content_discount_iv = (ImageView) findViewById(R.id.content_discount_iv);
        content_discount_fav_iv = (ImageView) findViewById(R.id.content_discount_fav_iv);

        content_discount_fav_iv.setVisibility(View.INVISIBLE);
        content_discount_title_name_tv = (TextView) findViewById(R.id.content_discount_title_name_tv);
        content_discount_des_name_tv = (TextView) findViewById(R.id.content_discount_des_name_tv);
        content_discount_price_tv = (TextView) findViewById(R.id.content_discount_price_tv);
        content_discount_time_tv = (TextView) findViewById(R.id.content_discount_time_tv);
        content_discount_orginal_price_tv = (TextView) findViewById(R.id.content_discount_orginal_price_tv);
        content_discount_des = (TextView) findViewById(R.id.content_discount_des);

       // content_discount_fav_iv.setOnClickListener(this);
        params = container_rl.getLayoutParams();
        params.height =    d.getHeight() / 3;
        params.width =   d.getWidth() ;

        getDiscountDetails();
    }


    public void getDiscountDetails(){

        if (!AppMethods.isConnectingToInternet(AddDetails.this)) {
            AppMethods.alertForNoInternet(AddDetails.this);
        } else {
            RequestParams params = RestMethods.getAdddetailsParms(sm.getStringData(AppStrings.Responsedata.userID),
                    adID);
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.singleAdvertisement, params,
                    sm.getStringData(AppStrings.Responsedata.token),false);

        }

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL ,boolean status) throws JSONException {


        if(URL.equals(RestApis.singleAdvertisement)){

            updateUI(response);
        }
    }




    public void updateUI(String response){

        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String discounts = jo.optString(AppStrings.Responsedata.Advertisements);

                updateinfo(imageBaseURL , discounts );
            } else {
//                home_srl.setRefreshing(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void updateFav(String response){

        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {

                favouriteStatus =  jo.getInt(AppStrings.Responsedata.FavouriteStatus);

                setFav();

            } else {
//                home_srl.setRefreshing(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



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

    public void updateinfo(String imageBaseURL ,String discounts ){

        try {
           /* final JSONArray jsonArray = new JSONArray(discounts);
           final JSONObject disc_obj = jsonArray.getJSONObject(0);*/
           final JSONObject disc_obj = new JSONObject(discounts);




            Log.e(TAG, "updateinfo: "+imageBaseURL+disc_obj.getString(AppStrings.Responsedata.adID));

            content_discount_title_name_tv.setText(disc_obj.getString(AppStrings.Responsedata.discountName));
            content_discount_orginal_price_tv.setText(AppStrings.symbols.dollar+disc_obj.getString(AppStrings.Responsedata.originalPrice));
            content_discount_price_tv.setText(AppStrings.symbols.dollar+disc_obj.getString(AppStrings.Responsedata.discountPrice));
            content_discount_time_tv.setText(AppMethods.DateToAgoFormat(disc_obj.getString(AppStrings.Responsedata.endTimeDate)));
            //content_discount_des_name_tv.setText(disc_obj.getString(AppStrings.Responsedata.description));
            content_discount_des.setText(disc_obj.getString(AppStrings.Responsedata.description));

            Log.e(TAG, "updateinfo: "+favouriteStatus );
            setFav();

            Glide.with(AddDetails.this)

                    .load(imageBaseURL+disc_obj.getString(AppStrings.Responsedata.adimage)).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                    .into(content_discount_iv);

            //.bitmapTransform(new RoundedCornersTransformation(activity, 30, 0))



            long timer1 = AppMethods.getMilliseconds(disc_obj.getString(AppStrings.Responsedata.endTimeDate));;

            // timer = timer*1000;




            timer = new CountDownTimer(timer1, 1000) {
                public void onTick(long millisUntilFinished) {
                    try {
                        content_discount_time_tv.setText(AppMethods.DateToAgoFormat(disc_obj.getString(AppStrings.Responsedata.endTimeDate)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    content_discount_time_tv.setText("00:00:00");
                }
            }.start();

            latlng =disc_obj.getString(AppStrings.Responsedata.latitude)+","+disc_obj.getString(AppStrings.Responsedata.longitude);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setFav() {

        if(favouriteStatus==0){

            content_discount_fav_iv.setImageResource(R.mipmap.unfavorite);
        }else{
            content_discount_fav_iv.setImageResource(R.mipmap.favorite);

        }
    }


    public void redirct2Maps(){

       // Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+latlng);
        Uri gmmIntentUri = Uri.parse("geo:"+latlng);

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void fav_unfav(int status){

        if (!AppMethods.isConnectingToInternet(AddDetails.this)) {
            AppMethods.alertForNoInternet(AddDetails.this);
        } else {
            RequestParams params = RestMethods.getFavUnfav(sm.getStringData(AppStrings.Responsedata.userID),
                    adID,status);
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.favouriteUnfavourite, params,
                    sm.getStringData(AppStrings.Responsedata.token),false);

        }


    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.content_discount_fav_iv:

                if(favouriteStatus ==0){

                    fav_unfav(1);

                }else{
                    fav_unfav(0);
                }



                break;
        }



    }
}
