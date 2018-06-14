package com.leona.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
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

import com.bumptech.glide.Glide;
import com.leona.R;
import com.leona.handlers.BranchsAdapter;
import com.leona.handlers.HomeAdapter;
import com.leona.handlers.HomeViewpagerAdapter;
import com.leona.handlers.ResdetailsViewpagerAdapter;
import com.leona.models.HomeModel;
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

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RestaruntDetails extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener{
    
    String merchantID;

    SessionManager sm;


    ImageView rest_det_iv;
    TextView rest_det_title_name_tv, rest_det_descrption_tv, rest_det_time_tv,
            rest_det_address_tv;
    RelativeLayout rest_det_share_rl ,row_content_rl;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;
    HomeAdapter adapter;
    ViewPager rest_det_vpager;

    ArrayList<HomeModel>  discountList;

    String latlng="";
    String resnam="";

    ResdetailsViewpagerAdapter resdetailsViewpagerAdapter;
    RecyclerView home_rv;
    LinearLayoutManager manager;

    LinearLayoutManager mLayoutManager;
    ArrayList<HashMap<String,Object>> address_list;
    RecyclerView dis_det_rv;

    BranchsAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restarunt_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);
        getSupportActionBar().setTitle("");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_res_det_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirct2Maps();
            }
        });

         initData();

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


    
    
    public void initData(){

        address_list = new ArrayList<HashMap<String,Object>>();

        w = getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        discountList = new ArrayList<>();

       sm = new SessionManager(RestaruntDetails.this);
        merchantID =getIntent().getStringExtra(AppStrings.Responsedata.merchantID);

        rest_det_iv = (ImageView) findViewById(R.id.rest_det_iv);
        rest_det_title_name_tv = (TextView) findViewById(R.id.rest_det_title_name_tv);

        rest_det_descrption_tv = (TextView) findViewById(R.id.rest_det_descrption_tv);
        rest_det_time_tv = (TextView) findViewById(R.id.rest_det_time_tv);
        rest_det_address_tv = (TextView) findViewById(R.id.rest_det_address_tv);
        rest_det_share_rl = (RelativeLayout) findViewById(R.id.rest_det_share_rl);
        row_content_rl = (RelativeLayout) findViewById(R.id.rest_det_content_rl);

        home_rv = (RecyclerView) findViewById(R.id.rest_det_rv);
        home_rv.setNestedScrollingEnabled(false);
        rest_det_vpager = (ViewPager) findViewById(R.id.rest_det_vpager);

        adapter = new HomeAdapter(discountList, this);
        manager = new LinearLayoutManager(this);
        manager = AppMethods.getLinearLayoutWithOutScroll(this);
        home_rv.setAdapter(adapter);
        home_rv.setLayoutManager(manager);


        dis_det_rv = (RecyclerView) findViewById(R.id.content_dis_det_rv);

        adapter1 = new BranchsAdapter(address_list, this);
        mLayoutManager = new LinearLayoutManager(this);
         mLayoutManager = AppMethods.getLinearLayoutWithOutScroll(this);
        dis_det_rv.setAdapter(adapter1);
        dis_det_rv.setLayoutManager(mLayoutManager);


        params = row_content_rl.getLayoutParams();
        params.height =    d.getHeight() / 3;
        params.width =   d.getWidth() ;
        getData(false);
    }

    private void getData(boolean status) {

        if (!AppMethods.isConnectingToInternet(RestaruntDetails.this)) {
            AppMethods.alertForNoInternet(RestaruntDetails.this);
        } else {
            RequestParams params = RestMethods.getrestaurentdetailsParms(sm.getStringData(AppStrings.Responsedata.userID),merchantID);
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.restaurantDetailsDiscounts, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL ,boolean status) throws JSONException {
        if(URL.equals(RestApis.restaurantDetailsDiscounts)){

            updateUI(response);
        }
    }

    private void updateUI(String response) {
        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String Restaurants = jo.optString(AppStrings.Responsedata.Restaurants);

                setRestData(imageBaseURL, Restaurants);

//

            } else {
//                home_srl.setRefreshing(false);



            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setRestData(String imageBaseURL, String restaurants) {



        try {
            JSONArray res_ary = new JSONArray(restaurants);
            JSONObject res_obj = res_ary.getJSONObject(0);

            String discounts = res_obj.optString(AppStrings.Responsedata.discounts);
            String address = res_obj.optString(AppStrings.Responsedata.address);

            Glide.with(RestaruntDetails.this)

                    .load(imageBaseURL+res_obj.getString(AppStrings.Responsedata.storeImage)).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                    .into(rest_det_iv);


            getSupportActionBar().setTitle(res_obj.getString(AppStrings.Responsedata.storeName));
           // rest_det_descrption_tv.setText(res_obj.getString(AppStrings.Responsedata.storeName));
            rest_det_descrption_tv.setText(res_obj.getString(AppStrings.Responsedata.description));
           /* rest_det_time_tv.setText(res_obj.getString(AppStrings.Responsedata.storeTime));
            rest_det_address_tv.setText(res_obj.getString(AppStrings.Responsedata.address));
*/


           // latlng = res_obj.getString(AppStrings.Responsedata.latitude)+","+res_obj.getString(AppStrings.Responsedata.longitude);

            resnam =res_obj.optString(AppStrings.Responsedata.storeName);


            apidiscountJsonData(imageBaseURL, discounts ,resnam);


            setRest(res_obj.getJSONArray(AppStrings.Responsedata.address) ,imageBaseURL);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setRest(JSONArray address ,String url){

        address_list.clear();
        for (int i = 0; i < address.length(); i++) {

            HashMap<String,Object> map = new HashMap<String,Object>();

            try {
                JSONObject add_obj = address.getJSONObject(i);



                map.put(AppStrings.Responsedata.storeTime, add_obj.optString(AppStrings.Responsedata.branchTime));
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

                Log.e("TAG", "setRest: "+address_list);
            } catch (Exception e) {

                e.printStackTrace();
            }

            adapter1.notifyDataSetChanged();
        }


    }

    private void calldata() {

        View rootView =getWindow().getDecorView().findViewById(R.id.container_ll);


        AppMethods.store(AppMethods.getScreenShot(rootView), "Leona.png");
    }



    public void apidiscountJsonData(String imageBaseUrl, String arrayData, String storeName) {
        try {
            JSONArray jsonArray = new JSONArray(arrayData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);

                String discountID = job.optString(AppStrings.Responsedata.discountID);
                String image = job.optString(AppStrings.Responsedata.image);
                String discountName = job.optString(AppStrings.Responsedata.discountName);
                String originalPrice = job.optString(AppStrings.Responsedata.originalPrice);
                String discountPrice = job.optString(AppStrings.Responsedata.discountPrice);
                String startTimeDate = job.optString(AppStrings.Responsedata.startTimeDate);
                String endTimeDate = job.optString(AppStrings.Responsedata.endTimeDate);
                String description = job.getString(AppStrings.Responsedata.description);
                String featured = job.optString(AppStrings.Responsedata.featured);
                String timeDateStamp = job.optString(AppStrings.Responsedata.timeDateStamp);
                String merchantID = job.optString(AppStrings.Responsedata.merchantID);
                HomeModel model = new HomeModel();
                model.setDiscountID(discountID);
                model.setImage(imageBaseUrl + image);
                model.setDiscountName(discountName);
                model.setDiscountPrice(discountPrice);
                model.setOriginalPrice(originalPrice);
                model.setStartTimeDate(startTimeDate);
                model.setEndTimeDate(endTimeDate);
                model.setDescription(description);
                model.setFeatured(featured);
                model.setTimeDateStamp(timeDateStamp);
                model.setMerchantID(merchantID);
                model.setStoreName(storeName);
                discountList.add(model);
            }

            adapter.notifyDataSetChanged();


           /* resdetailsViewpagerAdapter = new ResdetailsViewpagerAdapter(discountList ,RestaruntDetails.this);
            rest_det_vpager.setOffscreenPageLimit(resdetailsViewpagerAdapter.getCount());
            rest_det_vpager.setAdapter(resdetailsViewpagerAdapter);
*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void redirct2Maps(){

        String geoUriString="geo:"+latlng+"?q=("+resnam+")@"+latlng;
        Uri geoUri = Uri.parse(geoUriString);

        Intent mapCall  = new Intent(Intent.ACTION_VIEW, geoUri);
        startActivity(mapCall);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
