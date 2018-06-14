package com.leona.utils;

/**
 * Created by krify on 12/12/16.
 */

public interface Appintegers {

    int Unauthorized = 401;
    int SPLASH_TIME_OUT = 3000;
    int Delay = 3000;

    int Count = 10;

    int Maxpref = 10;
    int Minpref = 2;
    int Minradius = 2;
    int facebook_act_start = 101;
    int FACEBOOK_RESULT_CODE = 64206;
    int PROFILE_PIC_SELECTION_RESULT = 5;

    interface statuscode {

        int success = 1;
        int error = 0;
    }

    interface sortTypes {

        int all = 0;
        int expired = 1;
        int featured = 2;
        int high_low = 3;
        int low_hign = 4;




    }

    interface featureType {

        int home = 3;
        int preference = 2;
    }

}
