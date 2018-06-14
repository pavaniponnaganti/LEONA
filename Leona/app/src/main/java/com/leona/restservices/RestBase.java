package com.leona.restservices;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RestBase {

    private static final String BASE_URL = RestApis.BaseURl;

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static final String TAG = "RestBase";




    public static void get(String url, RequestParams params,String auth_key, AsyncHttpResponseHandler responseHandler) {
//        Log.e(TAG, "get() called with: url = [" + url + "], params = [" + params + "], responseHandler = [" + responseHandler + "]");
        client.addHeader("Authorization",auth_key);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params,String auth_key, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization",auth_key);
        client.post(getAbsoluteUrl(url), params, responseHandler);

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
