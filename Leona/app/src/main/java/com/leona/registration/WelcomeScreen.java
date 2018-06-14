package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.leona.R;
import com.leona.utils.AppMethods;

import org.w3c.dom.Text;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WelcomeScreen extends AppCompatActivity  implements  View.OnClickListener{

    Button singup_btn ,login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        initData();
    }

    public void initData(){


        singup_btn = (Button)findViewById(R.id.content_welcome_signup_btn);
        login_btn = (Button)findViewById(R.id.content_welcome_login_btn);

        singup_btn.setOnClickListener(this );
        login_btn.setOnClickListener(this );

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){


            case  R.id.content_welcome_signup_btn:

                startActivity(new Intent(WelcomeScreen.this ,Signup.class));

                break;

            case  R.id.content_welcome_login_btn:

                startActivity(new Intent(WelcomeScreen.this ,Login.class));




                break;

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {



           // startActivity(new Intent(WelcomeScreen.this ,LanguageScreen.class));
            AppMethods.onBackPressed(WelcomeScreen.this);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
