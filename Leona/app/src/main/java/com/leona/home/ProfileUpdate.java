package com.leona.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leona.R;
import com.leona.registration.Preferences;
import com.leona.registration.Signup;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.services.PlayLocation;
import com.leona.utils.AppLog;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.PicSelectActivity;
import com.leona.utils.SessionManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileUpdate extends AppCompatActivity implements View.OnClickListener ,AsyncHttpResponse.AsyncHttpResponseListener{

    SessionManager session;
    RelativeLayout container_rl;
    ImageView profile_pic;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;
    String select_pic ="";
    EditText email_et1, fname_et, lname_et, pwd_et,loc_et ;

    TextView email_et;
    String image_path;
    private String TAG = "UpdateProfile";

    Button prof_update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.backkkk);

        initData();

    }


    public void initData() {

        session = new SessionManager(this);

        w = getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        container_rl = (RelativeLayout) findViewById(R.id.content_profile_container_rl);
        profile_pic = (ImageView) findViewById(R.id.content_profile_update_pic_iv);
       // email_et = (EditText) findViewById(R.id.content_prof_update_email_et);
        email_et = (TextView) findViewById(R.id.content_prof_update_email_et);
        fname_et = (EditText) findViewById(R.id.content_prof_update_fname_et);
        lname_et = (EditText) findViewById(R.id.content_prof_update_lname_et);
        pwd_et = (EditText) findViewById(R.id.content_prof_update_pwd_et);
        loc_et = (EditText) findViewById(R.id.content_prof_update_loc_et);

        prof_update_btn = (Button) findViewById(R.id.content_prof_update_btn);


        profile_pic.setOnClickListener(this);
        prof_update_btn.setOnClickListener(this);

        params = container_rl.getLayoutParams();
        params.height = d.getHeight() / 3;
        params.width = d.getWidth();

        params = profile_pic.getLayoutParams();
        params.height = d.getWidth() / 3;
        params.width = d.getWidth()/3;

        setdata();

    }

    private void setdata() {

        email_et.setText(session.getStringData(AppStrings.Responsedata.email));
        fname_et.setText(session.getStringData(AppStrings.Responsedata.firstName));
        lname_et.setText(session.getStringData(AppStrings.Responsedata.lastName));
        pwd_et.setText(session.getStringData(AppStrings.Responsedata.password));
        loc_et.setText(session.getStringData(AppStrings.Responsedata.location));
        AppMethods.setRoundImage(this, session.getStringData(AppStrings.Responsedata.profilePic), profile_pic);

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
                //relaseAudio();

                onBackPressed();


                return true;


            default:

                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Appintegers.PROFILE_PIC_SELECTION_RESULT) {
            if (data != null) {
                image_path = data.getExtras().getString(AppStrings.uploadPic.selected_image_path);
                select_pic = data.getExtras().getString(AppStrings.uploadPic.selected_image_path);
                AppMethods.setRoundImage(this, select_pic, profile_pic);

            }
        }
    }

    public void photoTakeOption() {
        final CharSequence[] items = {"Camera", "Select from Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUpdate.this);
// builder.setTitle("");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
// dispatchTakePictureIntent();
                        selectPic(AppStrings.uploadPic.camera);
                        return;
                    case 1:
// getImageFromGallery();
                        selectPic(AppStrings.uploadPic.gallery);
                        break;
                    case 2:
                        dialog.dismiss();
                        break;
                    default:
                }
            }
        });
        builder.show();
        builder.create();

    }
    public void selectPic(String pic_type) {
        Intent intent = new Intent(ProfileUpdate.this, PicSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(AppStrings.uploadPic.type_from, pic_type);
        startActivityForResult(intent, Appintegers.PROFILE_PIC_SELECTION_RESULT);
    }


    public void validate(){

        if(!AppMethods.isConnectingToInternet(this)){

            AppMethods.alertForNoInternet(this);

        }else {


           if (fname_et.getText().toString().trim().isEmpty()) {

                AppMethods.showToast(ProfileUpdate.this, getResources().getString(R.string.validate_fname));


            } else if (lname_et.getText().toString().trim().isEmpty()) {

                AppMethods.showToast(ProfileUpdate.this, getResources().getString(R.string.validate_lname));


            } else if (pwd_et.getText().toString().trim().isEmpty()) {

                AppMethods.showToast(ProfileUpdate.this, getResources().getString(R.string.validate_password));


            } else if (pwd_et.getText().toString().trim().length() < 6) {

                AppMethods.showToast(ProfileUpdate.this, getResources().getString(R.string.validate_pwd_6));

            }else if (AppMethods.isSpace(pwd_et.getText().toString())) {
                AppMethods.showToast(ProfileUpdate.this, getResources().getString(R.string.validate_password_spaces));


            } else if (loc_et.getText().toString().trim().isEmpty()) {

               AppMethods.showToast(ProfileUpdate.this, getResources().getString(R.string.validate_location));


           } else {



                RequestParams params = RestMethods.getProfielupdateParams(ProfileUpdate.this, select_pic, pwd_et.getText().toString().trim(), fname_et.getText().toString().trim(), lname_et.getText().toString().trim(), loc_et.getText().toString().trim(), String.valueOf(PlayLocation.playlat), String.valueOf(PlayLocation.playlog));
                AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(ProfileUpdate.this, true);

                asyncHttpResponse.postAsyncHttp(RestApis.updateProfile, params, session.getStringData(AppStrings.Responsedata.token),false);
            }

        }





    }

    private void setData(String response) {


        Log.e(TAG, "setData: "+response );
        if( RestMethods.saveuserData(ProfileUpdate.this ,response)){

            try {
                JSONObject jo = new JSONObject(response);

                if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {

                    AppMethods.showToast(ProfileUpdate.this, jo.getString(AppStrings.Responsedata.message) );


                }
            }catch (Exception e){


            }
              finish();



        }else{

           /* email_et.setText("");
            pwd_et.setText("");
            fname_et.setText("");
            lname_et.setText("");*/
        }


    }


   /* public void updateProfile(){

        RequestParams params = RestMethods.getProfielupdateParams(ProfileUpdate.this,)
    }
*/

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case  R.id.content_prof_update_btn:

                validate();

                break;
      case  R.id.content_profile_update_pic_iv:

          photoTakeOption();

                break;
        }

    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL, boolean status) throws JSONException {
        if(URL.equals(RestApis.updateProfile)){

            setData(response);
            Log.e(TAG, "onAsyncHttpResponseGet: "+response );
        }
    }
}
