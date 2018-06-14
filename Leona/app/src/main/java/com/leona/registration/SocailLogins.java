package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.leona.home.Home;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.services.PlayLocation;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SocailLogins extends AppCompatActivity implements AsyncHttpResponse.AsyncHttpResponseListener{

    private static final String TAG = "Social" ;
    LoginButton loginFbButton;
    String fbid = "", fb_email = "", fb_profile_pic = "", fb_name = "", fb_gender = "";
    CallbackManager callbackManagerfb;
    String device_id = "1222222", select_location = "";
    SessionManager sm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_socail_logins);

        sm = new SessionManager(this);


        logoutFacebook();
//        loginFbButton = (LoginButton) findViewById(R.id.login_fb_button);
        loginFbButton = new LoginButton(this);
        callbackManagerfb = CallbackManager.Factory.create();
//        loginFbButton.performClick();
        facebookLogin();

    }

    /**
     * Facebook Login
     */
    public void facebookLogin() {
        loginFbButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginFbButton.performClick();
        loginFbButton.registerCallback(callbackManagerfb,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final AccessToken accessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest
                                (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                         try {

                                             Log.e(TAG, "onCompleted: "+jsonObject );
                                            fbid = jsonObject.getString(AppStrings.FacebookTags.fb_id);
                                            fb_name = jsonObject.getString(AppStrings.FacebookTags.fb_name);
                                            fb_email = jsonObject.getString(AppStrings.FacebookTags.fb_email);
                                            //fb_gender = jsonObject.getString(AppStrings.FacebookTags.fb_gender);

                                            fb_profile_pic = AppStrings.FacebookTags.fb_profilepic_link
                                                    + accessToken.getUserId()
                                                    + AppStrings.FacebookTags.fb_profilepic_type.replace(" ", "%20");

                                            Log.i("fb_email", "fb_email :" + fb_email);


                                             setFbData(jsonObject.toString());

                                          /*  signinSocialCall(
                                                    fbid,fb_name,"",
                                                    fb_email,
                                                    String.valueOf(PlayLocation.playlat),
                                                    String.valueOf(PlayLocation.playlog)
                                            );
*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        //parameters.putString("fields", "id,name,email,gender,birthday,is_verified");
                        parameters.putString("fields", "id,name,email,gender,birthday,is_verified");
                        request.setParameters(parameters);
                        request.executeAsync();
//                        batch.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i("FacebookException", "" + exception.toString());
                        finish();
                    }
                });
    }

    public void signinSocialCall(

            String socialId,
            String f_name,
            String l_name,
            String email,

            String latitude,
            String longitude) {
        AsyncHttpResponse response = new AsyncHttpResponse(this, true);
        RequestParams params = RestMethods.getFBLoginParams(SocailLogins.this,socialId,f_name,l_name,email,"",latitude,longitude);

        response.postAsyncHttp(RestApis.fbLogin ,params, RestApis.Authkey,false);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Appintegers.FACEBOOK_RESULT_CODE) {
            callbackManagerfb.onActivityResult(requestCode, resultCode, data);
           // AppLog.LOGE("OnActivity-->222code:" + data);
        }else {
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL,boolean status) throws JSONException {

        Log.e(TAG, "onAsyncHttpResponseGet: " +URL +" - "+response );
        setSocialLoginData(response);
    }
    public void setSocialLoginData(String response){

        if( RestMethods.saveuserData(SocailLogins.this ,response)){
            sm.createUserLoginSession();
            setFbData("");
        }else{

            finish();

        }


    }

    public void setFbData(String fb_api_response) {

        Intent intent = new Intent();
        intent.putExtra(AppStrings.intentData.fb_api_response, fb_api_response);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void logoutFacebook(){
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
