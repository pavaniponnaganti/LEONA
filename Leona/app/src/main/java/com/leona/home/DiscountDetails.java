package com.leona.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.leona.R;
import com.leona.handlers.BranchsAdapter;
import com.leona.handlers.ResturantAdapter;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.leona.widget.RoundedCornersTransformation;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DiscountDetails extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener ,View.OnClickListener{

    ImageView content_discount_iv, content_discount_fav_iv;
    TextView content_discount_title_name_tv, content_discount_des_name_tv, content_discount_price_tv,
            content_discount_time_tv, content_discount_orginal_price_tv,content_discount_des;

 String discountID;
    SessionManager sm;

    String latlng="";
    String resnam="";

    private CountDownTimer timer;

    RelativeLayout container_rl ,discount_share_rl;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;
    private String TAG ="DIscount";

    int favouriteStatus=0;

    BranchsAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ArrayList<HashMap<String,Object>> address_list;
    RecyclerView dis_det_rv;

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

        address_list = new ArrayList<HashMap<String,Object>>();
        sm = new SessionManager(this);

        w = getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        discountID =   getIntent().getStringExtra(AppStrings.Responsedata.discountID);

        container_rl = (RelativeLayout) findViewById(R.id.content_discount_det_rl);
        discount_share_rl = (RelativeLayout) findViewById(R.id.content_discount_share_rl);
        content_discount_iv = (ImageView) findViewById(R.id.content_discount_iv);
        content_discount_fav_iv = (ImageView) findViewById(R.id.content_discount_fav_iv);
        content_discount_title_name_tv = (TextView) findViewById(R.id.content_discount_title_name_tv);
        content_discount_des_name_tv = (TextView) findViewById(R.id.content_discount_des_name_tv);
        content_discount_price_tv = (TextView) findViewById(R.id.content_discount_price_tv);
        content_discount_time_tv = (TextView) findViewById(R.id.content_discount_time_tv);
        content_discount_orginal_price_tv = (TextView) findViewById(R.id.content_discount_orginal_price_tv);
        content_discount_des = (TextView) findViewById(R.id.content_discount_des);

        content_discount_fav_iv.setOnClickListener(this);
        discount_share_rl.setOnClickListener(this);
        params = container_rl.getLayoutParams();
        params.height =    d.getHeight() / 3;
        params.width =   d.getWidth() ;


        dis_det_rv = (RecyclerView) findViewById(R.id.content_dis_det_rv);

        adapter = new BranchsAdapter(address_list, this);
        mLayoutManager = new LinearLayoutManager(this);
        // mLayoutManager = AppMethods.getLinearLayoutWithOutScroll(getActivity());
        dis_det_rv.setAdapter(adapter);
        dis_det_rv.setLayoutManager(mLayoutManager);


        getDiscountDetails();
    }


    public void getDiscountDetails(){

        if (!AppMethods.isConnectingToInternet(DiscountDetails.this)) {
            AppMethods.alertForNoInternet(DiscountDetails.this);
        } else {
            RequestParams params = RestMethods.getdiscountdetailsParms(sm.getStringData(AppStrings.Responsedata.userID),
                    discountID);
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.singleDiscount, params,
                    sm.getStringData(AppStrings.Responsedata.token),false);

        }

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL ,boolean status) throws JSONException {


        if(URL.equals(RestApis.singleDiscount)){

            updateUI(response);
        }else  if(URL.equals(RestApis.favouriteUnfavourite)){

            updateFav(response);
        }
    }




    public void updateUI(String response){

        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String discounts = jo.optString(AppStrings.Responsedata.discounts);

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




            Log.e(TAG, "updateinfo: "+imageBaseURL+disc_obj.getString(AppStrings.Responsedata.image));

            content_discount_title_name_tv.setText(disc_obj.getString(AppStrings.Responsedata.discountName));
            content_discount_orginal_price_tv.setText(AppStrings.symbols.dollar+disc_obj.getString(AppStrings.Responsedata.originalPrice));
            content_discount_price_tv.setText(AppStrings.symbols.dollar+disc_obj.getString(AppStrings.Responsedata.discountPrice));
            content_discount_time_tv.setText(AppMethods.DateToAgoFormat(disc_obj.getString(AppStrings.Responsedata.endTimeDate)));
            content_discount_des_name_tv.setText(disc_obj.optString(AppStrings.Responsedata.storeName));
            content_discount_des.setText(disc_obj.getString(AppStrings.Responsedata.description));
            favouriteStatus =  disc_obj.getInt(AppStrings.Responsedata.favouriteStatus);

            resnam =disc_obj.optString(AppStrings.Responsedata.storeName);


            Log.e(TAG, "updateinfo: "+favouriteStatus );
            setFav();


            setRest(disc_obj.getJSONArray(AppStrings.Responsedata.address) ,imageBaseURL);
            Glide.with(DiscountDetails.this)

                    .load(imageBaseURL+disc_obj.getString(AppStrings.Responsedata.image)).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
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

    public void setRest(JSONArray address ,String url){

        address_list.clear();
        for (int i = 0; i < address.length(); i++) {

            HashMap<String,Object> map = new HashMap<String,Object>();

            try {
                JSONObject add_obj = address.getJSONObject(i);

                map.put(AppStrings.Responsedata.storeTime, add_obj.optString(AppStrings.Responsedata.storeTime));
                map.put(AppStrings.Responsedata.distance, add_obj.optString(AppStrings.Responsedata.distance));
                map.put(AppStrings.Responsedata.phoneNum, add_obj.optString(AppStrings.Responsedata.phoneNum));
                map.put(AppStrings.Responsedata.storeName, add_obj.optString(AppStrings.Responsedata.storeName));
                map.put(AppStrings.Responsedata.HomeAddress, add_obj.optString(AppStrings.Responsedata.HomeAddress));
                map.put(AppStrings.Responsedata.address, add_obj.optString(AppStrings.Responsedata.address));
                map.put(AppStrings.Responsedata.adressID, add_obj.optString(AppStrings.Responsedata.adressID));
                map.put(AppStrings.Responsedata.latitude, add_obj.optString(AppStrings.Responsedata.latitude));
                map.put(AppStrings.Responsedata.longitude, add_obj.optString(AppStrings.Responsedata.longitude));
                map.put(AppStrings.Responsedata.pic, url+add_obj.optString(AppStrings.Responsedata.pic));
                map.put(AppStrings.Responsedata.latlng, add_obj.optString(AppStrings.Responsedata.latitude)+","+add_obj.optString(AppStrings.Responsedata.longitude));

                address_list.add(map);
            } catch (Exception e) {

                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }

       /* "address": [
        {
            "startTime": "09:00 AM",
                "distance": 62.1,
                "phoneNum": "",
                "storeName": "store1",
                "HomeAddress": "",
                "address": "rajahmundry eastgodavari",
                "longitude": "81.8040345",
                "adressID": "1",
                "latitude": "17.0005383",
                "pic": "1497933197image.png",
                "endTime": " 09:00 PM"
        }*/
    }


    public void redirct2Maps(){


        String geoUriString="geo:"+latlng+"?q=("+resnam+")@"+latlng;
        Uri geoUri = Uri.parse(geoUriString);
        Log.e(TAG, "String: "+geoUriString);
        Intent mapCall  = new Intent(Intent.ACTION_VIEW, geoUri);
        startActivity(mapCall);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void fav_unfav(int status){

        if (!AppMethods.isConnectingToInternet(DiscountDetails.this)) {
            AppMethods.alertForNoInternet(DiscountDetails.this);
        } else {
            RequestParams params = RestMethods.getFavUnfav(sm.getStringData(AppStrings.Responsedata.userID),
                    discountID,status);
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
     case R.id.content_discount_share_rl:

         String share = "Product name : "+content_discount_title_name_tv.getText().toString() +", Price: "+content_discount_orginal_price_tv.getText().toString()+", Discount: "+content_discount_price_tv.getText().toString()+" ,App link : "+getResources().getString(R.string.link_app);

         AppMethods.shareViaIntent(this, share);



         break;
        }



    }
}
