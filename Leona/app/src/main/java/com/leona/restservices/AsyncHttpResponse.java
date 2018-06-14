package com.leona.restservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.leona.utils.AppMethods;
import com.leona.utils.Appintegers;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AsyncHttpResponse {

    private static final String TAG = "AsyncHttpResponse";
   // private SessionManager sessionManager;
    private Context context;
    private ProgressDialog progressDialog;
    private boolean isProgressVisible;
    private AsyncHttpResponseListener mListener;

    public AsyncHttpResponse(Context context, boolean isProgressVisible) {
        this.context = context;
        this.isProgressVisible = isProgressVisible;
        this.mListener = (AsyncHttpResponseListener) context;
    }

    public AsyncHttpResponse(Fragment fragment, boolean isProgressVisible) {

        this.context = fragment.getContext();
        this.isProgressVisible = isProgressVisible;
        this.mListener = (AsyncHttpResponseListener) fragment;

    }

    private void showProgressDialog() {

        if (isProgressVisible) {

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }
    }
    private void dismissProgressDialog() {

        if (isProgressVisible) {
            progressDialog.dismiss();
        }
    }
    public String postAsyncHttp(final String URL, final RequestParams postParams, final String api_key ,final boolean status) {
        showProgressDialog();
        final long firstime = System.currentTimeMillis();
        RestBase.post(URL, postParams, api_key, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                dismissProgressDialog();

                float ftime = (System.currentTimeMillis() - firstime)/1000;

                try {
                    Log.e(TAG,  URL+" : "+response.toString() );
                    mListener.onAsyncHttpResponseGet(response.toString(), URL ,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            @Override
//            public void onProgress(long bytesWritten, long totalSize) {
//                super.onProgress(bytesWritten, totalSize);
//                int prog = (int) ((bytesWritten * 100) / totalSize);
//                Log.v(TAG, "onProgress:val---> " + prog);
//            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);


                Log.e(TAG, "onFailure 1: "+errorResponse );

                try {
                    dismissProgressDialog();
                          if(statusCode == Appintegers.Unauthorized){
                              AppMethods.LogoutDialog(context);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.e(TAG, "onFailure 2: "+responseString );
                Log.e(TAG, "onFailure 2: "+statusCode );

                try {
                    dismissProgressDialog();
                    if(statusCode == Appintegers.Unauthorized){
                        AppMethods.LogoutDialog(context);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(TAG, "onFailure 3: "+errorResponse );

                try {
                    dismissProgressDialog();
                    if(statusCode == Appintegers.Unauthorized){
                        AppMethods.LogoutDialog(context);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        return null;
    }

    public String getAsyncHttp(final String URL, final RequestParams postParams, final String api_key ,final  boolean status) {
        showProgressDialog();
        final long firstime = System.currentTimeMillis();
        RestBase.get(URL, postParams,api_key, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                dismissProgressDialog();

                float ftime = (System.currentTimeMillis() - firstime)/1000;
                    try {
                    mListener.onAsyncHttpResponseGet(response.toString(), URL,status);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            @Override
//            public void onProgress(long bytesWritten, long totalSize) {
//                super.onProgress(bytesWritten, totalSize);
//                int prog = (int) ((bytesWritten * 100) / totalSize);
//                Log.e(TAG, "onProgress:val---> " + prog);
//            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    dismissProgressDialog();
                    Log.e(TAG, "onFailure: errorResponse--->" + errorResponse);
                    Log.e(TAG, "onFailure: statusCode--->" + statusCode);
                   /* if(statusCode == Appintegers.Unauthorized){
                        logoutIntent(context);
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }

  /*  private void logoutIntent(Context ctx) {
        sessionManager = new SessionManager(context);
        if (sessionManager.getBooleanData(AppStrings.Session.isuserlogin)) {
            sessionManager.setBooleanData(AppStrings.Session.isuserlogin, false);
            sessionManager.logoutUser();
            Intent intent = new Intent(ctx, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ctx.startActivity(intent);
        }
    }
*/
    public interface AsyncHttpResponseListener {

        void onAsyncHttpResponseGet(String response, String URL, boolean status) throws JSONException;
    }
}
