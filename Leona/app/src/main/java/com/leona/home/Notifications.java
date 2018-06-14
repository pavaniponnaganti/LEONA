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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.leona.R;
import com.leona.handlers.NearbyAdapter;
import com.leona.handlers.NotifyAdapter;
import com.leona.models.InfoHelpModel;
import com.leona.models.NearbyModel;
import com.leona.models.NotifyModel;
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

public class Notifications extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener {


    RecyclerView notify_rv;
    SwipeRefreshLayout mSwipeRefreshLayout;

    HashMap<String,Object> map;

    SessionManager sm;

    ArrayList<NotifyModel> arrayList;
    NotifyAdapter adapter;


    LinearLayoutManager mLayoutManager;


    LinearLayoutManager manager;

    int min_f = 0, max_f = Appintegers.Count;
    int min = 0, max = Appintegers.Count;
    int pro_count = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private boolean loading = true;
    private TextView fragment_nearby_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);


        initData();


    }

    private void initData() {

        sm = new SessionManager(this);
        fragment_nearby_tv = (TextView) findViewById(R.id.fragment_nearby_tv);
        notify_rv =(RecyclerView)findViewById(R.id.content_notify_rv);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.content_notify_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.MAGENTA, Color.YELLOW, Color.BLUE);
        arrayList = new ArrayList<>();
        adapter = new NotifyAdapter( arrayList,Notifications.this);
        mLayoutManager = new LinearLayoutManager(Notifications.this);
        // mLayoutManager = AppMethods.getLinearLayoutWithOutScroll(getActivity());
        notify_rv.setAdapter(adapter);
        notify_rv.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items

                if(!AppMethods.isConnectingToInternet(Notifications.this)){

                    AppMethods.alertForNoInternet(Notifications.this);

                }else {
                    getNofiications(min_f, max_f,true);
                }
            }
        });

        if(!AppMethods.isConnectingToInternet(Notifications.this)){

            AppMethods.alertForNoInternet(Notifications.this);

        }else {

            // setDatatolist();

            getNofiications(min,max, true);

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void getNofiications(int min ,int max ,boolean status) {
        if (!AppMethods.isConnectingToInternet(Notifications.this)) {
            AppMethods.alertForNoInternet(Notifications.this);
        } else {


            RequestParams params = RestMethods.getNotifyparams(this,sm.getStringData(AppStrings.Responsedata.userID),min,max);

            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.notification, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL, boolean status) throws JSONException {
        if(URL.equals(RestApis.notification)){
            insertData(response);
        }
    }


    public void setStatus() {

        if (arrayList.isEmpty()) {
            fragment_nearby_tv.setVisibility(View.VISIBLE);
        } else {
            fragment_nearby_tv.setVisibility(View.GONE);
        }
    }

        public void insertData(String response){
            //    String [] ques_array = getResources().getStringArray(R.array.info_ques_array);
            //    String [] answ_array = getResources().getStringArray(R.array.info_answer_array);


            arrayList.clear();
            try {
                JSONObject jo = new JSONObject(response);
                String message = jo.optString(AppStrings.Responsedata.message);


                if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {

                    String notification = jo.optString(AppStrings.Responsedata.notification);

                    setData(notification);
//                  home_srl.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    
                    setStatus();

                } else {

                    setStatus();
//                home_srl.setRefreshing(false);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }



        }


    public void setData(String arrayData){

        try {
            JSONArray jsonArray = new JSONArray(arrayData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);


                NotifyModel model = new NotifyModel();
                model.setCreatDatetime(job.getString(AppStrings.Responsedata.creatDatetime));
                model.setMessage(job.getString(AppStrings.Responsedata.message));


                arrayList.add(model);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();

    }



}
