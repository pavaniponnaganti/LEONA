package com.leona.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.leona.R;
import com.leona.handlers.HomeAdapter;
import com.leona.handlers.ResturantAdapter;
import com.leona.models.NearbyModel;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.services.PlayLocation;
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

public class RestaurentSearch extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener ,View.OnClickListener {

    SessionManager sm;
    EditText search_et;

    RecyclerView rest_rv;
    SwipeRefreshLayout mSwipeRefreshLayout;

    HashMap<String,Object> map;



    ArrayList<NearbyModel> arrayList;
    ResturantAdapter adapter;


    LinearLayoutManager mLayoutManager;

    int min_f = 0, max_f = Appintegers.Count;
    int min = 0, max = Appintegers.Count;
    int pro_count = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private boolean loading = true;

    private RecyclerView search_rv;
    private LinearLayout search_ll;
    private String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurent_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);

        initData();
    }

    private void initData() {
        sm = new SessionManager(this);
        rest_rv = (RecyclerView) findViewById(R.id.content_search_res_rv);



        search_et = (EditText) findViewById(R.id.newssearch_et);
        search_ll = (LinearLayout) findViewById(R.id.newssearch_ll);
        search_ll.setOnClickListener(this);


        arrayList = new ArrayList<>();
        adapter = new ResturantAdapter( arrayList, RestaurentSearch.this);
        mLayoutManager = new LinearLayoutManager(RestaurentSearch.this);
        // mLayoutManager = AppMethods.getLinearLayoutWithOutScroll(getActivity());
        rest_rv.setAdapter(adapter);
        rest_rv.setLayoutManager(mLayoutManager);


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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void validate() {

        if(search_et.getText().toString().isEmpty()){

            AppMethods.showToast(RestaurentSearch.this,getResources().getString(R.string.hint_search));

        }else{
            query =search_et.getText().toString().trim();
            getRestarunts(query ,min ,max,true);
        }
    }

    private void getRestarunts(String query ,int min ,int  max ,boolean status) {




        RequestParams params = RestMethods.getRestaurentSearchparms( sm.getStringData(AppStrings.RequestedData.userID) ,query, String.valueOf(PlayLocation.playlat),String.valueOf(PlayLocation.playlog),min ,max);

        AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(RestaurentSearch.this, true);

        asyncHttpResponse.postAsyncHttp(RestApis.restaurantSearch, params, sm.getStringData(AppStrings.Responsedata.token),status);

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.newssearch_ll:

                validate();

                break;
        }

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL ,boolean status) throws JSONException {

        if(URL.equals(RestApis.restaurantSearch)){
            nearbyApiJson(response);
        }
    }


    public void nearbyApiJson(String response) {


        arrayList.clear();
        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String Restaurants = jo.optString(AppStrings.Responsedata.Restaurants);

                apiJsonData(imageBaseURL, Restaurants);
//                home_srl.setRefreshing(false);
                adapter.notifyDataSetChanged();
            } else {


//                home_srl.setRefreshing(false);
            }



        } catch (Exception e) {
            e.printStackTrace();


        }
    }


    public void apiJsonData(String imageBaseUrl, String arrayData) {
        try {
            JSONArray jsonArray = new JSONArray(arrayData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);




                String merchantID = job.optString(AppStrings.Responsedata.merchantID);
                String storeImage = job.optString(AppStrings.Responsedata.storeImage);
                String storeName = job.optString(AppStrings.Responsedata.storeName);
                String distance = job.optString(AppStrings.Responsedata.distance);
                String offersCount = job.optString(AppStrings.Responsedata.offersCount);
                // String discription = job.getString(AppStrings.Responsedata.discription);
                String latitude = job.getString(AppStrings.Responsedata.latitude);
                String longitude = job.getString(AppStrings.Responsedata.longitude);

                NearbyModel model = new NearbyModel();

                model.setStoreImage(imageBaseUrl + storeImage);
                model.setStoreName(storeName);
                model.setDistance(distance);
                model.setOffersCount(offersCount);
                model.setLatitude(latitude);
                model.setLogitude(longitude);
                //  model.setDescription(discription);

                model.setMerchantID(merchantID);
                arrayList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
