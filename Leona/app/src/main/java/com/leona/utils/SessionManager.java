package com.leona.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by krify on 25/4/17.
 */

public class SessionManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static final String PREFER_NAME = "Leona";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String IS_TutorialsSeen = "IsTutorialSeen";

    int PRIVATE_MODE = 0;
    Context _context;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        this._context = context;
        editor = preferences.edit();
        editor.commit();
    }
    public void createUserLoginSession() {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.commit();
    }
    public boolean isUserLoggedIn() {
        return preferences.getBoolean(IS_USER_LOGIN, false);
    }
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
    public void setStringData(String store_key, String store_id){
        editor.putString(store_key, store_id);
        editor.commit();
    }
    public String  getStringData(String get_key){
        return preferences.getString(get_key, "");
    }
    public void clearStringData(String get_key){
        editor.putString(get_key, "");
        editor.commit();
    }
 public void setIntData(String store_key, int store_id){
        editor.putInt(store_key, store_id);
        editor.commit();
    }
    public int  getIntData(String get_key){
        return preferences.getInt(get_key, 0);
    }
    public void clearIntData(String get_key){
        editor.putInt(get_key, 0);
        editor.commit();
    }
    public void setBooleanData(String store_key, boolean state){
        editor.putBoolean(store_key, state);
        editor.commit();
    }
    public boolean  getBooleanData(String state){
        return preferences.getBoolean(state, true);
    }
    public boolean  getBooleanData1(String state){
        return preferences.getBoolean(state, false);
    }



    public void clearBooleanData(String state){
        editor.putString(state, "");
        editor.commit();
    }


    public void settutorialsStatus(){

        editor.putBoolean(IS_TutorialsSeen, true);
        editor.commit();

    }



    public boolean isTutorialseen() {
        return preferences.getBoolean(IS_TutorialsSeen, false);
    }

}
