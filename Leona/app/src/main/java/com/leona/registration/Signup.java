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
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Signup extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener, View.OnClickListener {

    EditText email_et, fname_et, lname_et, pwd_et;

    Button signup_btn;

    TextView haveac_tv;
    private String TAG ="Signup";

    SessionManager sm;
    private Button fb_btn;
    private LinearLayout mail_ll,yahoo_ll,outlook_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getResources().getString(R.string.title_activity_signup));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.title_activity_signup);

        toolbar.setSubtitle("");

        initData();
    }


    public void initData() {

        sm = new SessionManager(this);

        email_et = (EditText) findViewById(R.id.activity_signup_email_et);
        pwd_et = (EditText) findViewById(R.id.activity_signup_pwd_et);
        fname_et = (EditText) findViewById(R.id.activity_signup_fname_et);
        lname_et = (EditText) findViewById(R.id.activity_signup_lname_et);
        haveac_tv = (TextView) findViewById(R.id.activity_signup_alreadyac);
        mail_ll = (LinearLayout) findViewById(R.id.gmail_ll);
        yahoo_ll = (LinearLayout) findViewById(R.id.yahoo_ll);
        outlook_ll = (LinearLayout) findViewById(R.id.outlook_ll);



        email_et.setFilters(new InputFilter[]{AppMethods.ignoreFirstWhiteSpace()});
        pwd_et.setFilters(new InputFilter[]{AppMethods.ignoreFirstWhiteSpace()});
        lname_et.setFilters(new InputFilter[]{AppMethods.ignoreFirstWhiteSpace()});
       // fname_et.setFilters(new InputFilter[]{AppMethods.ignoreFirstWhiteSpace()});

        signup_btn = (Button) findViewById(R.id.activity_signup_btn);
        fb_btn = (Button) findViewById(R.id.activity_fb_btn);

        signup_btn.setOnClickListener(this);
        haveac_tv.setOnClickListener(this);

        fb_btn.setOnClickListener(this);


        mail_ll.setOnClickListener(this);
        yahoo_ll.setOnClickListener(this);
        outlook_ll.setOnClickListener(this);


    }



    public void validation(){
        if(!AppMethods.isConnectingToInternet(this)){

            AppMethods.alertForNoInternet(this);

        }else {


            if (email_et.getText().toString().trim().isEmpty()) {
                AppMethods.showToast(Signup.this, getResources().getString(R.string.validate_email));

            } else if (!email_et.getText().toString().trim().matches(AppStrings.EmailValidation.emailPattern)) {

                AppMethods.showToast(Signup.this, getResources().getString(R.string.validate_valid_email));

            } else if (fname_et.getText().toString().trim().isEmpty()) {

                AppMethods.showToast(Signup.this, getResources().getString(R.string.validate_fname));


            } else if (lname_et.getText().toString().trim().isEmpty()) {

                AppMethods.showToast(Signup.this, getResources().getString(R.string.validate_lname));


            } else if (pwd_et.getText().toString().trim().isEmpty()) {

                AppMethods.showToast(Signup.this, getResources().getString(R.string.validate_password));


            } else if (pwd_et.getText().toString().trim().length() < 6) {

                AppMethods.showToast(Signup.this, getResources().getString(R.string.validate_pwd_6));

            }else if (AppMethods.isSpace(pwd_et.getText().toString())) {
                AppMethods.showToast(Signup.this, getResources().getString(R.string.validate_password_spaces));


            } else {

                String token = FirebaseInstanceId.getInstance().getToken();

                RequestParams params = RestMethods.getSignupParams(Signup.this, email_et.getText().toString().trim(), pwd_et.getText().toString().trim(), fname_et.getText().toString().trim(), lname_et.getText().toString().trim(), token, String.valueOf(PlayLocation.playlat), String.valueOf(PlayLocation.playlog));
                AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(Signup.this, true);

                asyncHttpResponse.postAsyncHttp(RestApis.signIn, params, RestApis.Authkey,false);
            }

        }

    }



    @Override
    public void onAsyncHttpResponseGet(String response, String URL,boolean status) throws JSONException {


        if(URL.equals(RestApis.signIn)){

            setData(response);
        }

        Log.e(TAG, "onAsyncHttpResponseGet: "+URL + "Res :"+response );
    }

    private void setData(String response) {



        if( RestMethods.saveuserData(Signup.this ,response)){
            sm.createUserLoginSession();
            startActivity(new Intent(Signup.this ,Preferences.class));




        }else{

           /* email_et.setText("");
            pwd_et.setText("");
            fname_et.setText("");
            lname_et.setText("");*/
        }


    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.activity_signup_btn:

                validation();

                break;
            case R.id.activity_signup_alreadyac:

               startActivity(new Intent(Signup.this,Login.class));

                break;

            case R.id.activity_fb_btn:

                if(!AppMethods.isConnectingToInternet(this)){

                    AppMethods.alertForNoInternet(this);

                }else {

                    startActivityForResult(new Intent(this, SocailLogins.class), Appintegers.facebook_act_start);

                }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            startActivity(new Intent(Signup.this,WelcomeScreen.class));


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                //onBackPressed();

                startActivity(new Intent(Signup.this,WelcomeScreen.class));


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Appintegers.facebook_act_start) {
            if (data != null) {
                //String fb_api_response = data.getExtras().getString(AppStrings.intentData.fb_api_response);
                //setFbSigninData(fb_api_response);


            }

            if(resultCode==RESULT_OK) {
                String fb_api_response = data.getExtras().getString(AppStrings.intentData.fb_api_response);
                setFbSigninData(fb_api_response);

            }

        }
    }

    private void setFbSigninData(String fb_api_response) {

        try {
            JSONObject jsonObject = new JSONObject(fb_api_response);
            String   fbid = jsonObject.getString(AppStrings.FacebookTags.fb_id);
            String  fb_name = jsonObject.getString(AppStrings.FacebookTags.fb_name);
            String   fb_email = jsonObject.getString(AppStrings.FacebookTags.fb_email);


        email_et.setText(fb_email);
        fname_et.setText(fb_name);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
