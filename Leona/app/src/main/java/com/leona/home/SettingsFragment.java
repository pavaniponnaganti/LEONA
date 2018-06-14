package com.leona.home;


import android.content.Intent;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.leona.R;
import com.leona.registration.Preferences;
import com.leona.registration.PreferencesUpdate;
import com.leona.registration.RadiusUpdate;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener ,CompoundButton.OnCheckedChangeListener {


    RelativeLayout fragment_settings_logout,fragment_settings_tandc,fragment_settings_help,
            fragment_settings_share,fragment_settings_notifications,fragment_settings_pref,fragment_settings_profile,fragment_settings_radius;
    private SwitchCompat notify_switch;

    SessionManager sm;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment

        initData(view);

        return view;
    }

    public void initData(View view){

        sm = new SessionManager(getActivity());

        fragment_settings_profile = (RelativeLayout) view.findViewById(R.id.fragment_settings_profile);
        fragment_settings_radius = (RelativeLayout) view.findViewById(R.id.fragment_settings_radius);
        fragment_settings_notifications = (RelativeLayout) view.findViewById(R.id.fragment_settings_notifications);
        fragment_settings_share = (RelativeLayout) view.findViewById(R.id.fragment_settings_share);
        fragment_settings_share = (RelativeLayout) view.findViewById(R.id.fragment_settings_share);
        fragment_settings_tandc = (RelativeLayout) view.findViewById(R.id.fragment_settings_tandc);
        fragment_settings_help = (RelativeLayout) view.findViewById(R.id.fragment_settings_help);
        fragment_settings_logout = (RelativeLayout) view.findViewById(R.id.fragment_settings_logout);
          fragment_settings_pref = (RelativeLayout) view.findViewById(R.id.fragment_settings_pref);
        notify_switch = (SwitchCompat) view.findViewById(R.id.content_toggle_notify_switch);

        fragment_settings_logout.setOnClickListener(this);
        fragment_settings_share.setOnClickListener(this);
        fragment_settings_notifications.setOnClickListener(this);
        fragment_settings_pref.setOnClickListener(this);
        fragment_settings_profile.setOnClickListener(this);
        fragment_settings_radius.setOnClickListener(this);
        fragment_settings_tandc.setOnClickListener(this);
        fragment_settings_help.setOnClickListener(this);


        notify_switch.setOnCheckedChangeListener(this);


        setNofication();
    }

    public void setNofication(){

        if(sm.getBooleanData(AppStrings.Responsedata.notification)){

            notify_switch.setChecked(true);

        }else{

            notify_switch.setChecked(false);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {

            case R.id.content_toggle_notify_switch:

                if(!b){


                    sm.setBooleanData(AppStrings.Responsedata.notification,false);
                    // AppMethods.setLocale(SignUp.this,AppStrings.language.telugu);

                    }else{


                    //  Toast.makeText (Settings.this," Switch is on!!",Toast.LENGTH_SHORT).show ();
                    sm.setBooleanData(AppStrings.Responsedata.notification,true);
                    //AppMethods.setLocale(SignUp.this,AppStrings.language.english);

                }


                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.fragment_settings_profile:

               startActivity(new Intent(getActivity(), ProfileUpdate.class));

                break;
          case R.id.fragment_settings_pref:

              startActivity(new Intent(getActivity(), PreferencesUpdate.class));


                break;
          case R.id.fragment_settings_radius:

              startActivity(new Intent(getActivity(), RadiusUpdate.class));

                break;
          case R.id.fragment_settings_notifications:


                break;
        case R.id.fragment_settings_tandc:

            startActivity(new Intent(getActivity(), TermsConditions.class));

                break;
        case R.id.fragment_settings_help:

            startActivity(new Intent(getActivity(), InfoHelpActivity.class));

                break;
          case R.id.fragment_settings_share:

              AppMethods.shareViaIntent(getActivity(),getString(R.string.applink));

                break;
          case R.id.fragment_settings_logout:

           logOut();

                break;

        }


    }

    private void logOut() {

        AppMethods.showLogoutDialog(getActivity());

    }
}
