package com.leona.home;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.leona.R;
import com.leona.handlers.HomeAdapter;
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

public class SearchActivity extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener ,View.OnClickListener {

    EditText search_et;

    LinearLayoutManager manager;

    int min_f = 0, max_f = Appintegers.Count;
    int min = 0, max = Appintegers.Count;
    int pro_count = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private boolean loading = true;
    private RecyclerView search_rv;
    private LinearLayout search_ll;

    ArrayList<HomeModel> addList, discountList;
    HomeAdapter adapter;

    SessionManager sm;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);


        initData();
    }

    private void initData() {
        sm = new SessionManager(this);
        search_rv = (RecyclerView) findViewById(R.id.content_search_home_rv);



        search_et = (EditText) findViewById(R.id.newssearch_et);
        search_ll = (LinearLayout) findViewById(R.id.newssearch_ll);
        search_ll.setOnClickListener(this);

        addList = new ArrayList<>();
        discountList = new ArrayList<>();
        adapter = new HomeAdapter(discountList, SearchActivity.this);
        manager = new LinearLayoutManager(SearchActivity.this);

        search_rv.setAdapter(adapter);
        search_rv.setLayoutManager(manager);

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

            AppMethods.showToast(SearchActivity.this,getResources().getString(R.string.hint_search));

        }else{
            query =search_et.getText().toString().trim();
            discountApi(query,min ,max,true);
        }
    }

    public void discountApi(String query,int min ,int  max ,boolean status) {
        if (!AppMethods.isConnectingToInternet(SearchActivity.this)) {
            AppMethods.alertForNoInternet(SearchActivity.this);
        } else {
            RequestParams params = RestMethods.searchApiparamsHome(sm.getStringData(AppStrings.Responsedata.userID),
                    min,max,query);
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.homeSearch, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL,boolean status) throws JSONException {
        if (URL.equals(RestApis.homeSearch)) {
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
                String discounts = jo.optString(AppStrings.Responsedata.discounts);

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
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.newssearch_ll:

                validate();

                break;
        }

    }
}
