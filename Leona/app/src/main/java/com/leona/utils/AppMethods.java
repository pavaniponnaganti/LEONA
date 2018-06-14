package com.leona.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.leona.R;
import com.leona.models.HomeModel;
import com.leona.models.NearbyModel;
import com.leona.registration.WelcomeScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/**
 * Created by krify on 12/12/16.
 */

public class AppMethods {


    private static String TAG = "AppMethods";

    /***
     *
     * @param activity
     * @return
     */

    public static String getVersionCode(Activity activity) {

        PackageInfo pInfo = null;
        String version = "Version ";

        try {
            pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            version += pInfo.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    /****
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /*
     * Checking for all possible internet providers
     */
    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                            Log.e(TAG, "isConnectingToInternet: " + info[i].getState());
                            return true;
                        }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    * AlertDialog for Internet Connection
    */
    public static void alertForNoInternet(final Activity context) {

        AlertDialog.Builder alertDialogBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(context);
        }


        alertDialogBuilder.setMessage(context.getResources().getString(R.string.no_internet_connection)).setCancelable(false).setPositiveButton(context.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        //context.finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void alertMessageNoGps(final Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.alert_no_gps))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void setLocale(Context context, String languageToLoad) {

        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());

    }


 public static boolean GetYears(String date, int years) {


        try {
            Calendar c = Calendar.getInstance();


            DateFormat df = new SimpleDateFormat("dd/mm/yyyy");

            try {


                c.setTime(df.parse(date));


            } catch (Exception e) {
                e.printStackTrace();
            }


            Date past = new Date(c.getTimeInMillis());
            ;
            Date now = new Date();


            df.format(now);


            if ((TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > (366 * years))) {
                return true;

            } else {

                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;


    }

    /***
     *
     * @param context
     * @param content
     */

    public static void shareViaIntent(Context context, String content) {

        /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(content));
        context.startActivity(Intent.createChooser(sharingIntent,"Share using"));
*/
        Intent sendIntent = new Intent();
        sendIntent.addCategory(Intent.CATEGORY_DEFAULT);

        sendIntent.setAction(Intent.ACTION_SEND);


        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        // context.startActivity(sendIntent);

        context.startActivity(Intent.createChooser(sendIntent, ""));


    }

    public static void onBackPressed(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    public static String getISO(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();

        return countryCodeValue;
    }

    public static void getKeyHash(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.leona",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("HAShkey", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (Exception e) {

        }
    }

    public static String getDistanceinKM(int meters) {


        String distance = String.valueOf(meters * 0.001);

        return distance;
    }

    public static void showLogoutDialog(final Context context) {

        AlertDialog.Builder alert;
        final SessionManager session = new SessionManager(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(context);
        }

        alert.setMessage(context.getResources().getString(R.string.logout_msg));
        alert.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                session.logoutUser();


                setPref(context);
                Intent intent = new Intent(context, WelcomeScreen.class);

                context.startActivity(intent);


            }
        });

        alert.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });

        alert.create();
        alert.show();

    }
    public static void LogoutDialog(final Context context) {


        final SessionManager session = new SessionManager(context);

                     session.logoutUser();


                Intent intent = new Intent(context, WelcomeScreen.class);

                context.startActivity(intent);




    }




    public static InputFilter ignoreFirstWhiteSpace() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        if (dstart == 0)
                            return "";
                    }
                }
                return null;
            }
        };
    }



    public  static String getRange(int value){

        String val = value+"m";


        /*if(value<1000){


            return val;
        }else{
            DecimalFormat df = new DecimalFormat("####0.0");

            double val1 = (double) value/1000;
            Log.e(TAG, "getRange: "+val1 );
            String result = df.format(val1)+"Km";
            return result;
        }*/


//        return  val;

        return String.valueOf(value);
    }

    public static boolean isSpace(final String testCode){

        Log.e(TAG, "isSpace: "+testCode );
        if(testCode != null){
            for(int i = 0; i < testCode.length(); i++){
                if(Character.isWhitespace(testCode.charAt(i))){

                    return true;
                }
            }
        }
        return false;
    }

    public static LinearLayoutManager getLinearLayoutWithOutScroll(Activity context) {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        return layoutManager;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setStrictMode() {
        if (Integer.valueOf(Build.VERSION.SDK) > 3) {
            Log.d(TAG, "Enabling StrictMode policy over Sample application");
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }



    public static String DateToAgoFormat(String date) {




        Calendar c = Calendar.getInstance();
//        2017-05-31 09:11:12

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        //df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {


            c.setTime(df.parse(date));


        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            utcTIme = sdf.parse(date);
            utcTIme = new Date(sdf.format(utcTIme));
            // utcTIme=sdf.parse(utcTIme.toString());
            // date=utcTIme.toString();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }*/


        Date past = new Date(c.getTimeInMillis());;
        Date now = new Date();


        df.format(now);





        /*
         * try { past = format.parse(date); } catch (Exception e) { // TODO
       * Auto-generated catch block e.printStackTrace(); }

       *
       * try { now=format.parse(now.toString()); } catch (Exception e) { //
       * TODO Auto-generated catch block e.printStackTrace(); }
       */
     /* long hr =  TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
      long min =  TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
    */  long sec =  TimeUnit.MILLISECONDS.toSeconds(past.getTime() - now.getTime());

       // Log.e(TAG, "DateToAgoFormat: "+sec );


        return  splitToComponentTimes(sec);
    }

    public static long getMilliseconds(String date) {

        Calendar c = Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        //df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {


            c.setTime(df.parse(date));


        } catch (Exception e) {
            e.printStackTrace();
        }


        Date past = new Date(c.getTimeInMillis());;
        Date now = new Date();


        df.format(now);




     long sec =  TimeUnit.MILLISECONDS.toMillis(past.getTime() - now.getTime());



        return  sec;
    }



    public static String splitToComponentTimes(long biggy)
    {
        //Log.e(TAG, "splitToComponentTimes: "+biggy );
        String time="";

        long longVal = biggy;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};

        time = hours + ":" + mins + ":" + secs;


      /*  if(biggy<0) {

            time =  "00:00:00";

        }else{
            time = hours + ":" + mins + ":" + secs;


        }*/

        return time;
    }


    public static void redirct2Maps(Context context ,String latlng ,String resnam){

        String geoUriString="geo:"+latlng+"?q=("+resnam+")@"+latlng;
        Uri geoUri = Uri.parse(geoUriString);

        Intent mapCall  = new Intent(Intent.ACTION_VIEW, geoUri);
        context.startActivity(mapCall);
    }



    public static  String getcurrentDateUTC(){

        try {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

//Local time zone
            SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

//Time in GMT

            Log.e(TAG, "getcurrentDateUTC: "+dateFormatGmt.format(new Date()));
            return  dateFormatGmt.format(new Date()) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }



    public static void setPref(Context context){

        SessionManager sm = new SessionManager(context);
        sm.settutorialsStatus();
    }

    public static void setRoundImage(final Activity activity, String pic_url, final ImageView round_iv){
        Glide.with(activity).load(pic_url).asBitmap().centerCrop().error(R.mipmap.profile).placeholder(R.mipmap.profile).into(new BitmapImageViewTarget(round_iv) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
//                circularBitmapDrawable.setCornerRadius(5f);
                round_iv.setImageDrawable(circularBitmapDrawable);
            }
        });
    }


    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void store(Bitmap bm, String fileName){
        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getShareData(HomeModel model ,Context activity) {

        String share = "Product name : "+model.getDiscountName() +", Price: "+AppStrings.symbols.dollar+model.getOriginalPrice()+", Discount: "+AppStrings.symbols.dollar+model.getDiscountPrice()+" ,App link : "+activity.getResources().getString(R.string.link_app);


         return  share;

    }

    public static String getShareDataRes(NearbyModel model, Activity activity) {

        String share = "Restaurant name : "+model.getStoreName() +", Location: ,App link : "+activity.getResources().getString(R.string.link_app);


        return share;
    }
}
