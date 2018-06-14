package com.leona.home;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leona.R;
import com.leona.handlers.NearbyAdapter;
import com.leona.handlers.ResturantAdapter;
import com.leona.models.NearbyModel;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.services.PlayLocation;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.SessionManager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment implements AsyncHttpResponse.AsyncHttpResponseListener {

    private static final String TAG = "RestaurantFragment";
    ArrayList<HashMap<String, Object>> rest_list;

    RecyclerView rest_rv;
    SwipeRefreshLayout mSwipeRefreshLayout;

    HashMap<String, Object> map;

    SessionManager session;

    ArrayList<NearbyModel> arrayList;
    ResturantAdapter adapter;


    LinearLayoutManager mLayoutManager;

    int min_f = 0, max_f = Appintegers.Count;
    int min = 0, max = Appintegers.Count;
    int pro_count = 0;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private boolean loading = true;
    TextView fragment_nearby_tv;


    public RestaurantFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_near_by, container, false);

        initData(view);
        return view;

    }


    public void initData(View view) {


        session = new SessionManager(getActivity());

        fragment_nearby_tv = (TextView) view.findViewById(R.id.fragment_nearby_tv);
        rest_rv = (RecyclerView) view.findViewById(R.id.fragment_nearby_rv);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.frag_nearby_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.MAGENTA, Color.YELLOW, Color.BLUE);
        arrayList = new ArrayList<>();
        adapter = new ResturantAdapter(arrayList, getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        // mLayoutManager = AppMethods.getLinearLayoutWithOutScroll(getActivity());
        rest_rv.setAdapter(adapter);
        rest_rv.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items

                if (!AppMethods.isConnectingToInternet(getActivity())) {

                    AppMethods.alertForNoInternet(getActivity());

                } else {
                    getRestarunts(min_f, max_f, true);
                }
            }
        });

        if (!AppMethods.isConnectingToInternet(getActivity())) {

            AppMethods.alertForNoInternet(getActivity());

        } else {

            // setDatatolist();

            getRestarunts(min, max, true);

        }


        rest_rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                Log.e(TAG, "onScrolled: " + visibleItemCount);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        if (pro_count > min) {
                            min = min + Appintegers.Count;
                            getRestarunts(min, max, false);
                        }

                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                loading = true;
            }
        });

    }

    private void getRestarunts(int min, int max, boolean status) {


        RequestParams params = RestMethods.getNearbyparams(getActivity(), session.getStringData(AppStrings.RequestedData.userID), String.valueOf(PlayLocation.playlat), String.valueOf(PlayLocation.playlog), min, max);

        Log.e(TAG, "getRestarunts: " + params);
        Log.e(TAG, "getRestarunts: " + params);

        AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(RestaurantFragment.this, true);

        asyncHttpResponse.postAsyncHttp(RestApis.allRestaurants, params, session.getStringData(AppStrings.Responsedata.token), status);

    }

    void refreshItems() {
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }


    void onItemsLoadStart() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(true);
    }


    @Override
    public void onAsyncHttpResponseGet(String response, String URL, boolean status) throws JSONException {
        Log.e(TAG, "onAsyncHttpResponseGet : " + response);

        if (URL.equals(RestApis.allRestaurants)) {
            nearbyApiJson(response, status);
        }
    }


    public void nearbyApiJson(String response, boolean status) {

        if (status) {
            arrayList.clear();
        }
        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String Restaurants = jo.optString(AppStrings.Responsedata.Restaurants);
                pro_count = jo.getInt(AppStrings.Responsedata.totalCount);

                apiJsonData(imageBaseURL, Restaurants);
//                home_srl.setRefreshing(false);
                adapter.notifyDataSetChanged();

                setStatus();

            } else {
//                home_srl.setRefreshing(false);

                setStatus();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setStatus() {

        if (arrayList.isEmpty()) {
            fragment_nearby_tv.setVisibility(View.VISIBLE);
        } else {
            fragment_nearby_tv.setVisibility(View.GONE);
        }
    }

    public void apiJsonData(String imageBaseUrl, String arrayData) {
        try {
            JSONArray jsonArray = new JSONArray(arrayData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);


                String merchantID = job.optString(AppStrings.Responsedata.merchantID);
                String storeImage = job.optString(AppStrings.Responsedata.storeImage);
                String storeName = job.optString(AppStrings.Responsedata.storeName);
                String distance = job.optString(AppStrings.Responsedata.distance);
                String offersCount = job.optString(AppStrings.Responsedata.offersCount);
                String branchsCount = job.optString(AppStrings.Responsedata.branchsCount);
                // String discription = job.getString(AppStrings.Responsedata.discription);
               /* String latitude = job.getString(AppStrings.Responsedata.latitude);
                String longitude = job.getString(AppStrings.Responsedata.longitude);*/

                NearbyModel model = new NearbyModel();


                model.setStoreImage(imageBaseUrl + storeImage);
                model.setStoreName(storeName);
                model.setDistance(distance);
                model.setOffersCount(offersCount);
                model.setBranchsCount(branchsCount);
               /* model.setLatitude(latitude);
                model.setLogitude(longitude);*/
                //  model.setDescription(discription);

                model.setMerchantID(merchantID);
                arrayList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
