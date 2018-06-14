package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.leona.R;
import com.leona.home.Home;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.services.PlayLocation;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener, View.OnClickListener {

    TextView Fpwd_tv;

    EditText email_et, pwd_et;

    String TAG = "Login";
    Button Login_btn;
    Button fb_login_btn;

    SessionManager sm;

    private LinearLayout mail_ll,yahoo_ll,outlook_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getResources().getString(R.string.title_activity_login));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");

        toolbar.setSubtitle("");
        initData();
    }

    public void initData() {

        sm = new SessionManager(Login.this);
        email_et = (EditText) findViewById(R.id.activity_login_email_et);
        pwd_et = (EditText) findViewById(R.id.activity_login_pwd_et);

        Fpwd_tv = (TextView) findViewById(R.id.activity_login_fpwd);
        Login_btn = (Button) findViewById(R.id.activity_login_btn);
        fb_login_btn = (Button) findViewById(R.id.activity_fb_btn);

        email_et.setFilters(new InputFilter[]{AppMethods.ignoreFirstWhiteSpace()});
        pwd_et.setFilters(new InputFilter[]{AppMethods.ignoreFirstWhiteSpace()});

        mail_ll = (LinearLayout) findViewById(R.id.gmail_ll);
        yahoo_ll = (LinearLayout) findViewById(R.id.yahoo_ll);
        outlook_ll = (LinearLayout) findViewById(R.id.outlook_ll);


        mail_ll.setOnClickListener(this);
        yahoo_ll.setOnClickListener(this);
        outlook_ll.setOnClickListener(this);

        fb_login_btn.setOnClickListener(this);
        Fpwd_tv.setOnClickListener(this);
        Login_btn.setOnClickListener(this);
    }


    public void validation() {

        if(!AppMethods.isConnectingToInternet(this)){

            AppMethods.alertForNoInternet(this);

        }else {

            if (email_et.getText().toString().trim().isEmpty()) {
                AppMethods.showToast(Login.this, getResources().getString(R.string.validate_email));

            } else if (!email_et.getText().toString().trim().matches(AppStrings.EmailValidation.emailPattern)) {

                AppMethods.showToast(Login.this, getResources().getString(R.string.validate_valid_email));

            } else if (pwd_et.getText().toString().trim().isEmpty()) {
                AppMethods.showToast(Login.this, getResources().getString(R.string.validate_password));


            }  else if (AppMethods.isSpace(pwd_et.getText().toString())) {
                AppMethods.showToast(Login.this, getResources().getString(R.string.validate_password_spaces));


            } else {
                String token = FirebaseInstanceId.getInstance().getToken();

                RequestParams params = RestMethods.getLoginParams(Login.this, email_et.getText().toString().trim(), pwd_et.getText().toString().trim(), token, String.valueOf(PlayLocation.playlat), String.valueOf(PlayLocation.playlog));
                AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(Login.this, true);

                asyncHttpResponse.postAsyncHttp(RestApis.logIn, params, RestApis.Authkey,false);
            }

        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.activity_login_fpwd:

                startActivity(new Intent(Login.this, ForgotPassword.class));


                break;

            case R.id.activity_fb_btn:

                if(!AppMethods.isConnectingToInternet(this)){

                    AppMethods.alertForNoInternet(this);

                }else {

                    startActivityForResult(new Intent(this, SocailLogins.class), Appintegers.facebook_act_start);

                }
                break;
            case R.id.activity_login_btn:




                validation();
                break;

            case R.id.gmail_ll:


                setText("@gmail.com");


                break;

            case R.id.outlook_ll:

                setText("@outlook.com");
                break;

            case R.id.yahoo_ll:

                setText("@yahoo.com");
                break;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Appintegers.facebook_act_start) {
            if (data != null) {
                //String fb_api_response = data.getExtras().getString(AppStrings.intentData.fb_api_response);
                //setFbSigninData(fb_api_response);


            }

            if(resultCode==RESULT_OK) {
                FBloginsuccess();
            }

        }
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL,boolean status) throws JSONException {

        Log.e(TAG, "onAsyncHttpResponseGet: " + URL + " Res :" + response);

        setData(response);
    }





    public void setData(String response){

      if( RestMethods.saveuserData(Login.this ,response)){
          sm.createUserLoginSession();
         startActivity(new Intent(Login.this ,Home.class));
      }else{

          email_et.setText("");
          pwd_et.setText("");
      }

    }

    public void FBloginsuccess(){

        sm.createUserLoginSession();
        startActivity(new Intent(Login.this ,Home.class));
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void setText(String message){

        if(email_et.getText().toString().isEmpty()){

            email_et.setText(message);
        }else if(email_et.getText().toString().contains("@")){


            String a[] =   email_et.getText().toString().split("@");
            email_et.setText(a[0]+message);
        }else if(!email_et.getText().toString().contains("@")){

            email_et.setText(email_et.getText().toString()+message);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                //onBackPressed();

                startActivity(new Intent(Login.this,WelcomeScreen.class));


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            startActivity(new Intent(Login.this,WelcomeScreen.class));


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
