package com.leona.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.leona.R;
import com.leona.home.Home;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.services.PlayLocation;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

public class ForgotPassword extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener, View.OnClickListener {


    EditText email_et;
    Button fpwd_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        toolbar.setSubtitle("");
        initData();
    }

    public void initData(){

        email_et = (EditText) findViewById(R.id.activity_fpwd_email_et);

        fpwd_btn = (Button) findViewById(R.id.activity_fpwd_btn);
        email_et.setFilters(new InputFilter[]{AppMethods.ignoreFirstWhiteSpace()});

        fpwd_btn.setOnClickListener(this);

    }

    public void validation() {

        if(!AppMethods.isConnectingToInternet(this)){

            AppMethods.alertForNoInternet(this);

        }else {


            if (email_et.getText().toString().trim().isEmpty()) {
                AppMethods.showToast(ForgotPassword.this, getResources().getString(R.string.validate_email));

            } else if (!email_et.getText().toString().trim().matches(AppStrings.EmailValidation.emailPattern)) {

                AppMethods.showToast(ForgotPassword.this, getResources().getString(R.string.validate_valid_email));

            } else {


                RequestParams params = RestMethods.getfpwdParams(ForgotPassword.this, email_et.getText().toString().trim());
                AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(ForgotPassword.this, true);

                asyncHttpResponse.postAsyncHttp(RestApis.forgotPassword, params, RestApis.Authkey, false);
            }

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.activity_fpwd_btn:

                validation();
                break;
        }

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL ,boolean status) throws JSONException {

        if (URL.equals(RestApis.forgotPassword)) {

            checkStatus(response);
        }
    }


    private void checkStatus(String response) {


        if (RestMethods.checkStatus(ForgotPassword.this, response)) {

           finish();

        } else {

        }
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
}
