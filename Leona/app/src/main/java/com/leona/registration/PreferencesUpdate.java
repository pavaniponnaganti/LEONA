package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.leona.widget.CollectionPicker;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PreferencesUpdate extends AppCompatActivity  implements AsyncHttpResponse.AsyncHttpResponseListener,View.OnClickListener{


    private RecyclerView pref_rv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<HashMap<String ,Object>> list;
    CollectionPicker mPicker;

    SessionManager sm;
    Button next_btn;
    private String TAG = "Preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);


        toolbar.setTitle("");

        toolbar.setSubtitle("");

        initData();
    }

    public void initData(){

        sm = new SessionManager(PreferencesUpdate.this);

        list = new ArrayList<HashMap<String, Object>>();

        next_btn = (Button)findViewById(R.id.content_pref_next_btn); 

        next_btn.setOnClickListener(this);

        next_btn.setText(getString(R.string.update));
        getList();
    }

    public void getList(){

        if(!AppMethods.isConnectingToInternet(this)){

            AppMethods.alertForNoInternet(this);

        }else {


            RequestParams params = RestMethods.getPrefParams(PreferencesUpdate.this, sm.getStringData(AppStrings.Responsedata.userID));

            AsyncHttpResponse response = new AsyncHttpResponse(PreferencesUpdate.this, true);

            response.postAsyncHttp(RestApis.preferenceList, params, sm.getStringData(AppStrings.Responsedata.token),false);

        }
    }




    public void populateList(String response){


        try {
            JSONObject jo = new JSONObject(response);
            JSONArray ja = jo.getJSONArray(AppStrings.Responsedata.preferences);


            for (int i = 0; i < ja.length(); i++) {


                JSONObject prefobj = ja.getJSONObject(i);
                HashMap<String ,Object> map = new HashMap<String ,Object>();
                // map.put()

                   map.put(AppStrings.Responsedata.preferenceID,prefobj.getString(AppStrings.Responsedata.preferenceID));

                    map.put(AppStrings.Responsedata.preferenceName, prefobj.getString(AppStrings.Responsedata.preferenceName));
                    map.put(AppStrings.Responsedata.userPreference,prefobj.getString(AppStrings.Responsedata.userPreference));


                list.add(map);


            }




        } catch (Exception e) {
            e.printStackTrace();
        }





        mPicker = (CollectionPicker) findViewById(R.id.collection_item_picker);

        mPicker.setItems(list);

        mPicker.drawItemView();

        mPicker.invalidate();
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL,boolean status) throws JSONException {

        if(URL.equals(RestApis.preferenceList)) {
            populateList(response);
        }else  if(URL.equals(RestApis.updatePreference)) {


            checkStatus(response);


        }

    }

    private void checkStatus(String response) {


        if(RestMethods.checkStatus(PreferencesUpdate.this,response)){

            finish();

        }else{

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
        
            case R.id.content_pref_next_btn:


                vaildate();


                break;
        }
    }


    public void vaildate(){

        if(getprefSize()< Appintegers.Minpref){
            AppMethods.showToast(PreferencesUpdate.this,getResources().getString(R.string.validate_pref_min));

        }else{

            saveList();
         }
    }
    public  int getprefSize(){



        return mPicker.getPreflist().size();
    }



    public void saveList(){

        RequestParams params = RestMethods.savePrefParams(PreferencesUpdate.this,sm.getStringData(AppStrings.Responsedata.userID),RestMethods.prefJSon(mPicker.getPreflist()).toString());

        AsyncHttpResponse response = new AsyncHttpResponse(PreferencesUpdate.this,true);

        response.postAsyncHttp(RestApis.updatePreference,params,sm.getStringData(AppStrings.Responsedata.token),false);

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
}
