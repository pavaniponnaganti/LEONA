package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.leona.R;
import com.leona.utils.AppMethods;
import com.leona.utils.Appintegers;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LanguageScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_screen);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.act_lan_screen_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LanguageScreen.this, IntroActivity.class);
                startActivity(intent);


            }
        });




    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


             AppMethods.onBackPressed(LanguageScreen.this);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }




}
