package com.leona.home;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.leona.R;
import com.leona.handlers.FavAdapter;
import com.leona.handlers.HomeAdapter;
import com.leona.handlers.HomeViewpagerAdapter;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyFavourites extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener{

    private static final String TAG = "MyFavourites";
    SessionManager sm;
    RecyclerView home_rv;
    ArrayList<HomeModel> addList, discountList;
    FavAdapter adapter;
    LinearLayoutManager manager;

    int min_f = 0, max_f = Appintegers.Count;
    int min = 0, max = Appintegers.Count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favourites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);

       intiData();


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


    public void intiData() {
        sm = new SessionManager(MyFavourites.this);
        home_rv = (RecyclerView) findViewById(R.id.content_fav_rv);
//        home_srl = (SwipeRefreshLayout) view.findViewById(R.id.home_srl);
//        home_srl.setOnRefreshListener(HomeFragment.this);
        addList = new ArrayList<>();
        discountList = new ArrayList<>();
        adapter = new FavAdapter(discountList, MyFavourites.this);
        manager = new LinearLayoutManager(MyFavourites.this);
        //manager = AppMethods.getLinearLayoutWithOutScroll(MyFavourites.this);
        home_rv.setAdapter(adapter);
        home_rv.setLayoutManager(manager);

//        home_srl.post(new Runnable() {
//            @Override
//            public void run() {
//                onRefresh();
//                home_srl.setRefreshing(true);
//
//            }
//        });


        discountApi(min,max,false);
    }


    public void discountApi(int min ,int max ,boolean status) {
        if (!AppMethods.isConnectingToInternet(MyFavourites.this)) {
            AppMethods.alertForNoInternet(MyFavourites.this);
        } else {
            RequestParams params = RestMethods.userfavApiParms(sm.getStringData(AppStrings.Responsedata.userID),
                   min, max);
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.userFavourites, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }
    }



    public void onAsyncHttpResponseGet(String response, String URL ,boolean status) throws JSONException {
        if (URL.equals(RestApis.userFavourites)) {
            discountApiJson(response);
        }
    }

    public void apidiscountJsonData(String imageBaseUrl, String arrayData) {
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
                String storeName = job.optString(AppStrings.Responsedata.storeName);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void discountApiJson(String response) {
        discountList.clear();
        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String discounts = jo.optString(AppStrings.Responsedata.FavouriteStatus);

                apidiscountJsonData(imageBaseURL, discounts);
//                home_srl.setRefreshing(false);
                adapter.notifyDataSetChanged();
            } else {
//                home_srl.setRefreshing(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }




}
