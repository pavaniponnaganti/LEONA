package com.leona.restservices;

import android.content.Context;

import com.leona.registration.Signup;
import com.leona.services.PlayLocation;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by krify on 18/4/17.
 */

public class RestMethods {


    /**
     * @param context
     * @param email
     * @param password
     * @param fname
     * @param lastname
     * @param deviceid
     * @param latitude
     * @param longitude
     * @return
     */


    public static RequestParams getSignupParams(Context context, String email,
                                                String password, String fname, String lastname,
                                                String deviceid, String latitude, String longitude


    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.lastName, lastname);
        params.put(AppStrings.RequestedData.email, email);
        params.put(AppStrings.RequestedData.password, password);
        params.put(AppStrings.RequestedData.firstName, fname);
        params.put(AppStrings.RequestedData.deviceType, AppStrings.constants.devicetype);
        params.put(AppStrings.RequestedData.deviceID, deviceid);
        params.put(AppStrings.RequestedData.latitude, latitude);
        params.put(AppStrings.RequestedData.longitude, longitude);
        params.put(AppStrings.RequestedData.language, AppStrings.constants.language_enlish);


        return params;

    }

    /**
     *
     * @param context
     * @param pic
     * @param password
     * @param fname
     * @param lastname
     * @param location
     * @param latitude
     * @param longitude
     * @return
     */
  public static RequestParams getProfielupdateParams(Context context, String pic,
                                                String password, String fname, String lastname,
                                                String location, String latitude, String longitude


    ) {

/*      firstName
              lastNamel
      password
              registrationI
      language
              facebookID
      deviceType
              latitude
      longitude
              userID
      Location
              profilePic*/



        SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.lastName, lastname);
        params.put(AppStrings.RequestedData.userID, session.getStringData(AppStrings.Responsedata.userID));

        params.put(AppStrings.RequestedData.password, password);
        params.put(AppStrings.RequestedData.firstName, fname);
        params.put(AppStrings.RequestedData.deviceType, AppStrings.constants.devicetype);

        params.put(AppStrings.RequestedData.latitude, latitude);
        params.put(AppStrings.RequestedData.longitude, longitude);
        params.put(AppStrings.RequestedData.language, AppStrings.constants.language_enlish);

       if(!pic.isEmpty()){
           try {
               params.put(AppStrings.RequestedData.profilePic, new File(pic));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

        params.put(AppStrings.RequestedData.location, location);


        return params;

    }

    /***
     *
     * @param context
     * @param email
     * @param password
     * @param deviceid
     * @param latitude
     * @param longitude
     * @return
     */


    public static RequestParams getLoginParams(Context context, String email,
                                               String password,
                                               String deviceid, String latitude, String longitude


    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.email, email);
        params.put(AppStrings.RequestedData.password, password);

        params.put(AppStrings.RequestedData.deviceType, AppStrings.constants.devicetype);
        params.put(AppStrings.RequestedData.deviceID, deviceid);
        params.put(AppStrings.RequestedData.latitude, latitude);
        params.put(AppStrings.RequestedData.longitude, longitude);
        params.put(AppStrings.RequestedData.language, AppStrings.constants.language_enlish);


        return params;

    }

    /****
     *
     * @param context
     * @param fbid
     * @param fname
     * @param lastname
     * @param email
     * @param deviceid
     * @param latitude
     * @param longitude
     * @return
     */


    public static RequestParams getFBLoginParams(Context context,
                                                 String fbid, String fname, String lastname, String email,
                                                 String deviceid, String latitude, String longitude


    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.lastName, lastname);
        params.put(AppStrings.RequestedData.facebookID, fbid);
        params.put(AppStrings.RequestedData.email, email);

        params.put(AppStrings.RequestedData.firstName, fname);
        params.put(AppStrings.RequestedData.deviceType, AppStrings.constants.devicetype);
        params.put(AppStrings.RequestedData.deviceID, deviceid);
        params.put(AppStrings.RequestedData.latitude, latitude);
        params.put(AppStrings.RequestedData.longitude, longitude);
        params.put(AppStrings.RequestedData.language, AppStrings.constants.language_enlish);


        return params;

    }

    /**
     * @param context
     * @param uid
     * @return
     */

    public static RequestParams getPrefParams(Context context,
                                              String uid

    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.userID, uid);
        params.put(AppStrings.RequestedData.categoryID, AppStrings.constants.categoryID);


        return params;

    }

    /**
     * @param context
     * @param uid
     * @param preflistJSon
     * @return
     */
    public static RequestParams savePrefParams(Context context,
                                               String uid, String preflistJSon

    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.userID, uid);
        params.put(AppStrings.RequestedData.preferenceID, preflistJSon);


        return params;

    }

    /**
     * @param context
     * @param email
     * @return
     */
    public static RequestParams getfpwdParams(Context context,
                                              String email

    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.email, email);


        return params;

    }

    /***
     *
     * @param context
     * @param uid
     * @param distance
     * @return
     */

    public static RequestParams saveRadiusParams(Context context,
                                                 String uid, String distance

    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.userID, uid);
        params.put(AppStrings.RequestedData.distance, distance);


        return params;

    }
 public static RequestParams getRadiusParams(Context context,
                                                 String uid

    ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.userID, uid);


        return params;

    }
  public static RequestParams getNearbyparams(Context context,
                                                 String uid,  String latitude,  String longitude, int min ,int max

  ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.userID, uid);
        params.put(AppStrings.RequestedData.latitude, latitude);
        params.put(AppStrings.RequestedData.longitude, longitude);
        params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);


        return params;

    } public static RequestParams getNotifyparams(Context context, String uid,  int min ,int max) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.userID, uid);
         params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);


        return params;

    }
public static RequestParams getRestaurentSearchparms(
                                                 String uid, String keyword, String latitude,  String longitude, int min ,int max

  ) {


        //  SessionManager session = new SessionManager(context);

        RequestParams params = new RequestParams();

        params.put(AppStrings.RequestedData.userID, uid);
        params.put(AppStrings.RequestedData.keyword, keyword);
        params.put(AppStrings.RequestedData.latitude, latitude);
        params.put(AppStrings.RequestedData.longitude, longitude);
        params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);


        return params;

    }


    /**
     * @param context
     * @param response
     * @return
     */

    public static boolean saveuserData(Context context, String response) {

        SessionManager sm = new SessionManager(context);


        try {
            JSONObject jo = new JSONObject(response);

            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                JSONObject userobj = jo.getJSONObject(AppStrings.Responsedata.userDetails);


                sm.setStringData(AppStrings.Responsedata.userID, userobj.getString(AppStrings.Responsedata.userID));
                sm.setStringData(AppStrings.Responsedata.firstName, userobj.getString(AppStrings.Responsedata.firstName));
                sm.setStringData(AppStrings.Responsedata.lastName, userobj.getString(AppStrings.Responsedata.lastName));
                sm.setStringData(AppStrings.Responsedata.email, userobj.getString(AppStrings.Responsedata.email));
                sm.setIntData(AppStrings.Responsedata.loginType, userobj.getInt(AppStrings.Responsedata.loginType));
                sm.setStringData(AppStrings.Responsedata.token, userobj.getString(AppStrings.Responsedata.token));
                sm.setStringData(AppStrings.Responsedata.facebookID, userobj.getString(AppStrings.Responsedata.facebookID));
                sm.setStringData(AppStrings.Responsedata.password, userobj.getString(AppStrings.Responsedata.password));
                sm.setStringData(AppStrings.Responsedata.profilePic, jo.getString(AppStrings.Responsedata.imageBaseURL) +userobj.getString(AppStrings.Responsedata.profilePic));
                sm.setStringData(AppStrings.Responsedata.location, userobj.getString(AppStrings.Responsedata.location));

                return true;
            } else {

                AppMethods.showToast(context, jo.getString(AppStrings.Responsedata.message));

                return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }


    /**
     * @param preflist
     * @return JSONArray
     */

    public static JSONArray prefJSon(ArrayList<HashMap<String, Object>> preflist) {

        JSONArray prefary = new JSONArray();

        for (int i = 0; i < preflist.size(); i++) {
            JSONObject prefobj = new JSONObject();

            try {
                prefobj.put(AppStrings.Responsedata.preferenceID, preflist.get(i).get(AppStrings.Responsedata.preferenceID).toString());

                prefary.put(prefobj);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return prefary;
    }


    public static boolean checkStatus(Context context, String response) {


        try {
            JSONObject jo = new JSONObject(response);


            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {

                AppMethods.showToast(context, jo.getString(AppStrings.Responsedata.message));

                return true;
            } else {

                AppMethods.showToast(context, jo.getString(AppStrings.Responsedata.message));

                return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;


    }

    /**
     *
     * @param userID
     * @param min
     * @param max
     * @param featureType
     * @return
     */
    public static RequestParams homeDiscountApi(String userID, int min, int max, int featureType ,int type) {
        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);
        params.put(AppStrings.RequestedData.featureType, featureType);
        params.put(AppStrings.RequestedData.type, type);



        return params;
    }

 public static RequestParams allAdvertisementsparams(String userID, int min, int max, int featureType ,int type) {
        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);
        params.put(AppStrings.RequestedData.featureType, featureType);
        params.put(AppStrings.RequestedData.type, type);
        params.put(AppStrings.RequestedData.datetime, AppMethods.getcurrentDateUTC());


        return params;
    }

    /**
     *
     * @param userID
     * @param min
     * @param max
     * @param keyword
     * @return
     */
    public static RequestParams searchApiparamsHome(String userID, int min, int max, String  keyword) {
        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);
        params.put(AppStrings.RequestedData.keyword, keyword);
        return params;
    }




public static RequestParams userfavApiParms(String userID, int min, int max) {
        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);

        return params;
    }





    public static RequestParams PrefernceDiscountApi(String userID, int min, int max, int type) {
        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.min, min);
        params.put(AppStrings.RequestedData.max, max);
        params.put(AppStrings.RequestedData.type, type);
        return params;
    }
 public static RequestParams getFavUnfav(String userID ,String discountID,int status) {
        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.discountID, discountID);
        params.put(AppStrings.RequestedData.status, status);

        return params;
    }

    public static RequestParams getdiscountdetailsParms(String userID, String discountID) {
        RequestParams params = new RequestParams();


        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.discountID, discountID);
        params.put(AppStrings.RequestedData.latitude,  String.valueOf(PlayLocation.playlat));
        params.put(AppStrings.RequestedData.longitude,  String.valueOf(PlayLocation.playlog));



        return params;
    }
public static RequestParams getAdddetailsParms(String userID, String adID) {
        RequestParams params = new RequestParams();
        params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.adID, adID);

        return params;
    }
  public static RequestParams getrestaurentdetailsParms( String userID,String merchantID) {
        RequestParams params = new RequestParams();


      params.put(AppStrings.RequestedData.userID, userID);
        params.put(AppStrings.RequestedData.merchantID, merchantID);
      params.put(AppStrings.RequestedData.latitude,  String.valueOf(PlayLocation.playlat));
      params.put(AppStrings.RequestedData.longitude,  String.valueOf(PlayLocation.playlog));


      return params;
    }
}
