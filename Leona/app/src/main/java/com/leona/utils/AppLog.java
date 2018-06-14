package com.leona.utils;

import android.util.Log;

/**
 * Created by krify on 23/5/17.
 */

public class AppLog{

        public static void LOGV(String s){
            Log.v("AppLogV", "Data:"+s );
        }
        public static void LOGI(String s){
            Log.i("AppLogI", "Data:"+s );
        }
        public static void LOGD(String s){
            Log.d("AppLogD", "Data:"+s );
        }
        public static void LOGE(String s){
            Log.e("AppLogE", "Data:"+s );
        }
}
