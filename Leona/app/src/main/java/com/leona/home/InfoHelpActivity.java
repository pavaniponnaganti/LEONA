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


import com.leona.R;
import com.leona.handlers.InfoHelpAdapter;
import com.leona.models.InfoHelpModel;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
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

public class InfoHelpActivity extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener{

    InfoHelpAdapter adapter;
    ArrayList<InfoHelpModel> arrayList;
    LinearLayoutManager layoutManager;
    RecyclerView info_help_rv;
    SessionManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.help_toolbar);
        setSupportActionBar(toolbar);
       // setTitle(getString(R.string.info_help_support));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);



        initUI();
    }
    public void initUI(){
        sm = new SessionManager(this);
        info_help_rv = (RecyclerView)findViewById(R.id.info_help_rv);
        layoutManager = new LinearLayoutManager(this);
        arrayList = new ArrayList<>();
        adapter = new InfoHelpAdapter(this,arrayList);
        info_help_rv.setLayoutManager(layoutManager);
//        info_help_rv.setLayoutManager(AppUtils.getLinearLayoutWithOutScroll(this));
        info_help_rv.setAdapter(adapter);

        callhelpApi(true);
    }
    public void insertData(){
       String [] ques_array = getResources().getStringArray(R.array.info_ques_array);
        String [] answ_array = getResources().getStringArray(R.array.info_answer_array);
        for (int i = 0; i < ques_array.length; i++) {
            InfoHelpModel model = new InfoHelpModel();
            model.setAnswer(answ_array[i]);
            model.setQuestion(ques_array[i]);
            model.setQuesHead(true);


            arrayList.add(model);
        }
        adapter.notifyDataSetChanged();
    }
  public void insertData(String response){
    //    String [] ques_array = getResources().getStringArray(R.array.info_ques_array);
    //    String [] answ_array = getResources().getStringArray(R.array.info_answer_array);


      arrayList.clear();
      try {
          JSONObject jo = new JSONObject(response);
          String message = jo.optString(AppStrings.Responsedata.message);
          if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {

              String description = jo.optString(AppStrings.Responsedata.description);

              setData(description);
//                home_srl.setRefreshing(false);
              adapter.notifyDataSetChanged();
          } else {
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

                InfoHelpModel model = new InfoHelpModel();
                model.setAnswer(job.getString(AppStrings.Responsedata.answer));
                model.setQuestion(job.getString(AppStrings.Responsedata.question));
                model.setQuesHead(true);


                arrayList.add(model);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();

    }
    public void updateExpandStatus(int pos,boolean status){
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList.get(i).setExpandStatus(false);
        }
        if(status)
        arrayList.get(pos).setExpandStatus(false);
        else
            arrayList.get(pos).setExpandStatus(true);
        adapter.notifyDataSetChanged();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void callhelpApi(boolean status) {
        if (!AppMethods.isConnectingToInternet(InfoHelpActivity.this)) {
            AppMethods.alertForNoInternet(InfoHelpActivity.this);
        } else {


            RequestParams params =  new RequestParams();

            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.help, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL, boolean status) throws JSONException {
        if(URL.equals(RestApis.help)){
            insertData(response);
        }
    }
}
