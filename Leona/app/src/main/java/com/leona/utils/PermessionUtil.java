package com.leona.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.leona.R;


/**
 * Created by krify on 18/1/17.
 */

public class PermessionUtil {

    public static boolean checkReadExternalPermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    public static boolean checkCallPermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    public static boolean checkVibratePermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    public static boolean checkWriteExternalPermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    public static boolean checkGetAccountsPermission(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    public static void requestWritePermission(Activity activity) {

        /*if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

//            Toast.makeText(activity, "Storage permission allows us to access storage to save images and to upload profile pictures. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

            ShowSingleButtonDialog(activity, activity.getString(R.string.error),
                    activity.getString(R.string.no_storage_permission),
                    activity.getString(R.string.Ok));

// ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA}, AppStrings.PermissionCode.Storage);
        }*/


        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA ,Manifest.permission.RECORD_AUDIO}, AppStrings.PermissionCode.Storage);

    }



    public static void requestPermission(Activity activity){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)){

            ShowSingleButtonDialog(activity, activity.getString(R.string.error),
                    activity.getString(R.string.no_storage_permission),
                    activity.getString(R.string.ok));

        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},0);
        }
    }





    public static void ShowSingleButtonDialog(Context activity, String title, String message, String buttontext) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(message);
        alert.setTitle(title);
        alert.setPositiveButton(buttontext, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });


        alert.create();
        alert.show();
    }


    public static void locationPermisson(Activity activity) {
        int netPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (netPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppStrings.PermissionCode.Location);
                return;
            }
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppStrings.PermissionCode.Location);
            return;
        }
    }

    public static boolean locationInitialCheck(Activity activity) {
        int netPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (netPermission != PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
