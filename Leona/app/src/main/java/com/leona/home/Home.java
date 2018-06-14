package com.leona.home;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.leona.R;
import com.leona.registration.IntroActivity;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home extends AppCompatActivity {

    Toolbar toolbar;
    TextView home_toolbar_title_tv;
    ImageView home_toolbar_title_iv;
    FrameLayout home_fl;
    MenuItem notify_menu, search_menu, sort_menu, fav_menu;
    MenuItem all, low2high, high2low, fdis, experied;
    SessionManager session;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    home_toolbar_title_iv.setVisibility(View.VISIBLE);
                    home_toolbar_title_tv.setVisibility(View.GONE);
                    home();
                    showMenu();

                    return true;

                case R.id.navigation_pref:

                    home_toolbar_title_tv.setText(R.string.title_prefe);
                    home_toolbar_title_iv.setVisibility(View.GONE);
                    home_toolbar_title_tv.setVisibility(View.VISIBLE);
                    Preferences();

                    showMenu();

                    return true;

                case R.id.navigation_nearby:

                    home_toolbar_title_tv.setText(R.string.title_nearby);
                    home_toolbar_title_iv.setVisibility(View.GONE);
                    home_toolbar_title_tv.setVisibility(View.VISIBLE);
                    nearby();
                    hideMenu();
                    return true;

                case R.id.navigation_restaurant:

                    home_toolbar_title_tv.setText(R.string.title_resturent);
                    home_toolbar_title_iv.setVisibility(View.GONE);
                    home_toolbar_title_tv.setVisibility(View.VISIBLE);
                    restaurent();

                    hideMenu();
                    return true;

                case R.id.navigation_settings:


                    settings();

                    //  AppMethods.showLogoutDialog(Home.this);

                    home_toolbar_title_tv.setText(R.string.title_settings);
                    home_toolbar_title_iv.setVisibility(View.GONE);
                    home_toolbar_title_tv.setVisibility(View.VISIBLE);

                    hideAllMenu();
                    return true;
            }
            return false;
        }

    };


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(Home.this);

        intiUI();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);

        notify_menu = menu.findItem(R.id.notify);
        search_menu = menu.findItem(R.id.search);
        sort_menu = menu.findItem(R.id.sort);
        fav_menu = menu.findItem(R.id.fav);
        low2high = menu.findItem(R.id.low2high);
        high2low = menu.findItem(R.id.high2low);
        fdis = menu.findItem(R.id.fdis);
        experied = menu.findItem(R.id.experied);
        all = menu.findItem(R.id.all);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notify) {


            startActivity(new Intent(Home.this, Notifications.class));


            return true;
        }

        if (id == R.id.fav) {


            startActivity(new Intent(Home.this, MyFavourites.class));


            return true;

        }

        if (id == R.id.sort) {


            setcheckedState();


            return true;

        }
       if (id == R.id.low2high) {

           session.setIntData(AppStrings.RequestedData.type , Appintegers.sortTypes.low_hign);

            callData();
            return true;

        }
       if (id == R.id.high2low) {


           session.setIntData(AppStrings.RequestedData.type , Appintegers.sortTypes.high_low);

           callData();

           return true;

        }
       if (id == R.id.fdis) {


           session.setIntData(AppStrings.RequestedData.type , Appintegers.sortTypes.featured);
           callData();


           return true;

        }
       if (id == R.id.experied) {


           session.setIntData(AppStrings.RequestedData.type , Appintegers.sortTypes.expired);
           callData();


           return true;

        }
       if (id == R.id.all) {


           session.setIntData(AppStrings.RequestedData.type , Appintegers.sortTypes.all);
           callData();


           return true;

        }








        if (id == R.id.search) {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_fl);
            if (fragment instanceof HomeFragment) {

                Intent i = new Intent(Home.this, SearchActivity.class);
                i.putExtra(AppStrings.RequestedData.From, AppStrings.constants.home);
                startActivity(i);

            } else if (fragment instanceof PreferencesFragment) {

                Intent i = new Intent(Home.this, SearchActivity.class);
                i.putExtra(AppStrings.RequestedData.From, AppStrings.constants.pref);
                startActivity(i);

            } else if (fragment instanceof NearByFragment) {

                Intent i = new Intent(Home.this, RestaurentSearch.class);
                i.putExtra(AppStrings.RequestedData.From, AppStrings.constants.nearby);
                startActivity(i);

            } else if (fragment instanceof RestaurantFragment) {

                Intent i = new Intent(Home.this, RestaurentSearch.class);
                i.putExtra(AppStrings.RequestedData.From, AppStrings.constants.restaurents);
                startActivity(i);

            }


        }
        return super.onOptionsItemSelected(item);
    }


    public void callData() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_fl);
        if (fragment instanceof HomeFragment) {

            ((HomeFragment) fragment).discountApi(0,Appintegers.Count,true);

        }else if (fragment instanceof PreferencesFragment) {

            ((PreferencesFragment) fragment).discountApi(0,Appintegers.Count,true);

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void intiUI() {


        home_toolbar_title_tv = (TextView) findViewById(R.id.home_toolbar_title_tv);
        home_toolbar_title_iv = (ImageView) findViewById(R.id.home_toolbar_title_iv);
        home_toolbar_title_iv.setVisibility(View.VISIBLE);


        home();
        home_fl = (FrameLayout) findViewById(R.id.home_fl);
    }

    public void home() {


        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        ft.replace(R.id.home_fl, homeFragment, "HomeFragment");
        ft.commit();
    }

    public void Preferences() {

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        ft.replace(R.id.home_fl, preferencesFragment, "PreferencesFragment");
        ft.commit();
    }

    public void nearby() {


        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        NearByFragment nearByFragment = new NearByFragment();
        ft.replace(R.id.home_fl, nearByFragment, "NearByFragment");
        ft.commit();
    }

    public void restaurent() {


        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RestaurantFragment restaurantFragment = new RestaurantFragment();
        ft.replace(R.id.home_fl, restaurantFragment, "NearByFragment");
        ft.commit();
    }

    public void settings() {


        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        ft.replace(R.id.home_fl, settingsFragment, "SettingsFragment");
        ft.commit();
    }

    public void getFragment() {

    }

    public void hideMenu() {
        notify_menu.setVisible(true);
        search_menu.setVisible(true);
        sort_menu.setVisible(false);
        fav_menu.setVisible(false);

    }

    public void hideAllMenu() {
        notify_menu.setVisible(false);
        search_menu.setVisible(false);
        sort_menu.setVisible(false);
        fav_menu.setVisible(false);

    }

    public void showMenu() {
        notify_menu.setVisible(true);
        search_menu.setVisible(true);
        sort_menu.setVisible(true);
        fav_menu.setVisible(true);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void setcheckedState() {

        int i = session.getIntData(AppStrings.RequestedData.type);

        switch (i) {

            case  Appintegers.sortTypes.all:

                all.setChecked(true);

                break;
            case  Appintegers.sortTypes.expired:

                experied.setChecked(true);

                break;
            case  Appintegers.sortTypes.featured:

                fdis.setChecked(true);

                break;
            case  Appintegers.sortTypes.high_low:

                high2low.setChecked(true);
                break;
            case  Appintegers.sortTypes.low_hign:

                low2high.setChecked(true);
                break;
        }


    }


}
