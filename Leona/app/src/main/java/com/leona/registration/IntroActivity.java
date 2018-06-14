package com.leona.registration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.leona.R;
import com.leona.utils.SessionManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by krify on 19/4/17.
 */

public class IntroActivity extends AppIntro {

    SessionManager session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
       /* // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(firstFragment);
        addSlide(secondFragment);
        addSlide(thirdFragment);
        addSlide(fourthFragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.

       IntroFragment fragment = new IntroFragment();

        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.home_tuto_title), getResources().getString(R.string.home_tuto_des), R.mipmap.tuto1, getResources().getColor(R.color.bg_clor)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.nearby_tuto_title), getResources().getString(R.string.nearby_tuto_des), R.mipmap.tuto2, getResources().getColor(R.color.bg_clor)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.discount_tuto_title), getResources().getString(R.string.discount_tuto_des), R.mipmap.tuto3, getResources().getColor(R.color.bg_clor)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.restaurant_tuto_title), getResources().getString(R.string.restaurant_tuto_des), R.mipmap.tuto4, getResources().getColor(R.color.bg_clor)));
        ///addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.footer_tuto_title), getResources().getString(R.string.footer_tuto_des), R.mipmap.tut_footer, getResources().getColor(R.color.bg_clor)));
        addSlide(fragment);



        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(getResources().getColor(R.color.bg_clor));
        setSeparatorColor(getResources().getColor(R.color.white));
        setTitle(getResources().getString(R.string.app_name));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);


        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }


    public void setStatus(){

        session.settutorialsStatus();
    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        setStatus();
        startActivity(new Intent(IntroActivity.this, WelcomeScreen.class));

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        setStatus();
        startActivity(new Intent(IntroActivity.this, WelcomeScreen.class));

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
