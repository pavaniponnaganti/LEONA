package com.leona.home;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.leona.R;
import com.leona.handlers.HomeAdapter;
import com.leona.handlers.HomeViewpagerAdapter;
import com.leona.models.HomeModel;
import com.leona.restservices.AsyncHttpResponse;
import com.leona.restservices.RestApis;
import com.leona.restservices.RestMethods;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.utils.Appintegers;
import com.leona.utils.EndlessParentScrollListener;
import com.leona.utils.ScrollViewExt;
import com.leona.utils.ScrollViewListener;
import com.leona.utils.SessionManager;
import com.leona.widget.AutoScrollViewPager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements AsyncHttpResponse.AsyncHttpResponseListener ,ScrollViewListener {
    private static final String TAG = "HomeFragment";
    SessionManager sm;
    RecyclerView home_rv;
    ArrayList<HomeModel> addList, discountList;
    HomeAdapter adapter;
    LinearLayoutManager manager;
    //    SwipeRefreshLayout home_srl;
   // ViewPager home_vpager;
    AutoScrollViewPager home_vpager;
    HomeViewpagerAdapter homeViewpagerAdapter;

    int min_f = 0, max_f = Appintegers.Count;
    int min = 0, max = Appintegers.Count;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    int totalCount = 0;

    private int page = 0;
    private Handler handler;
    NestedScrollView frag_home_nsv;
    private int delay =3000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        intiUI(view);
        advertismentApi(min,max,true);
        discountApi(min,max,true);
        return view;
    }

   /* Runnable runnable = new Runnable() {
        public void run() {
            if (homeViewpagerAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            home_vpager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };
*/

    public void intiUI(View view) {

        handler = new Handler();

        sm = new SessionManager(getActivity());
       // home_vpager = (ViewPager) view.findViewById(R.id.home_vpager);
        home_vpager = (AutoScrollViewPager) view.findViewById(R.id.home_vpager);
        home_rv = (RecyclerView) view.findViewById(R.id.home_rv);
        home_rv.setNestedScrollingEnabled(false);

//        home_srl = (SwipeRefreshLayout) view.findViewById(R.id.home_srl);
//        home_srl.setOnRefreshListener(HomeFragment.this);
        addList = new ArrayList<>();
        discountList = new ArrayList<>();
        adapter = new HomeAdapter(discountList, getActivity());
        manager = new LinearLayoutManager(getActivity());
        manager = AppMethods.getLinearLayoutWithOutScroll(getActivity());
        home_rv.setAdapter(adapter);
        home_rv.setLayoutManager(manager);


        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "FCM Registration Token: " + token);


        frag_home_nsv = (NestedScrollView) view.findViewById(R.id.frag_home_nsv);
        frag_home_nsv.setOnScrollChangeListener(new EndlessParentScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount < totalCount) {
                    min = min + Appintegers.Count;
                    discountApi(min, max, false);
                }
            }
        });

        homeViewpagerAdapter = new HomeViewpagerAdapter(addList ,getActivity());
        home_vpager.setOffscreenPageLimit(homeViewpagerAdapter.getCount());
        home_vpager.setAdapter(homeViewpagerAdapter);
        home_vpager.startAutoScroll();
        home_vpager.setCycle(true);


      /*  home_rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                Log.e(TAG, "onScrolled: "+visibleItemCount );
                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();
                pastVisiblesItems = manager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        if (totalCount > min) {
                            min = min + Appintegers.Count;
                            discountApi(min,max,false);
                        }

                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                loading = true;
            }
        });*/


//        home_srl.post(new Runnable() {
//            @Override
//            public void run() {
//                onRefresh();
//                home_srl.setRefreshing(true);
//
//            }
//        });




    }


    public void discountApi(int min ,int max ,boolean status) {
        if (!AppMethods.isConnectingToInternet(getActivity())) {
            AppMethods.alertForNoInternet(getActivity());
        } else {

            Log.e(TAG, "discountApi: Token"+ sm.getStringData(AppStrings.Responsedata.token) );
            RequestParams params = RestMethods.homeDiscountApi(sm.getStringData(AppStrings.Responsedata.userID),
                    min, max,Appintegers.featureType.home,sm.getIntData(AppStrings.RequestedData.type));
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.allDiscounts, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }
    }

    public void advertismentApi(int min ,int max ,boolean status) {
        if (!AppMethods.isConnectingToInternet(getActivity())) {
            AppMethods.alertForNoInternet(getActivity());
        } else {
            RequestParams params = RestMethods.allAdvertisementsparams(sm.getStringData(AppStrings.Responsedata.userID),
                   min, max ,Appintegers.featureType.home ,sm.getIntData(AppStrings.RequestedData.type));

            Log.e(TAG, "advertismentApi: "+params );
            AsyncHttpResponse asyncHttpResponse = new AsyncHttpResponse(this, false);
            asyncHttpResponse.postAsyncHttp(RestApis.allAdvertisements, params,
                    sm.getStringData(AppStrings.Responsedata.token),status);

        }
    }




    @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy) {
        // We take the last son in the scrollview
        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        Log.e(TAG, "onScrollChanged: 1111111111");
        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
            // do stuff
            Log.e(TAG, "onScrollChanged: 22222222");
            //recyClerMinMaxFunctionality();
        }
    }

    @Override
    public void onAsyncHttpResponseGet(String response, String URL ,boolean status) throws JSONException {
        if (URL.equals(RestApis.allDiscounts)) {
            discountApiJson(response,status);
        } else if (URL.equals(RestApis.allAdvertisements)) {
            adverstismentApiJson(response,status);
        }
    }

    public void apidiscountJsonData(String imageBaseUrl, String arrayData) {
        try {
            JSONArray jsonArray = new JSONArray(arrayData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);


                String discountID = job.optString(AppStrings.Responsedata.discountID);
                String image = job.optString(AppStrings.Responsedata.image);
                String discountName = job.optString(AppStrings.Responsedata.discountName);
                String originalPrice = job.optString(AppStrings.Responsedata.originalPrice);
                String discountPrice = job.optString(AppStrings.Responsedata.discountPrice);
                String startTimeDate = job.optString(AppStrings.Responsedata.startTimeDate);
                String endTimeDate = job.optString(AppStrings.Responsedata.endTimeDate);
                String description = job.getString(AppStrings.Responsedata.description);
                String featured = job.optString(AppStrings.Responsedata.featured);
                String timeDateStamp = job.optString(AppStrings.Responsedata.timeDateStamp);
                String merchantID = job.optString(AppStrings.Responsedata.merchantID);
                String storeName = job.optString(AppStrings.Responsedata.storeName);


                HomeModel model = new HomeModel();
                model.setDiscountID(discountID);
                model.setImage(imageBaseUrl + image);
                model.setDiscountName(discountName);
                model.setDiscountPrice(discountPrice);
                model.setOriginalPrice(originalPrice);
                model.setStartTimeDate(startTimeDate);
                model.setEndTimeDate(endTimeDate);
                model.setDescription(description);
                model.setFeatured(featured);
                model.setTimeDateStamp(timeDateStamp);
                model.setMerchantID(merchantID);
                model.setStoreName(storeName);
                discountList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apiaddJsonData(String imageBaseUrl, String arrayData) {
        try {
            JSONArray jsonArray = new JSONArray(arrayData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject job = jsonArray.getJSONObject(i);


                String adID = job.optString(AppStrings.Responsedata.adID);
                String image = job.optString(AppStrings.Responsedata.adimage);
                String discountName = job.optString(AppStrings.Responsedata.discountName);
                String originalPrice = job.optString(AppStrings.Responsedata.originalPrice);
                String discountPrice = job.optString(AppStrings.Responsedata.discountPrice);
                String startTimeDate = job.optString(AppStrings.Responsedata.startTimeDate);
                String endTimeDate = job.optString(AppStrings.Responsedata.endTimeDate);
                String description = job.getString(AppStrings.Responsedata.description);
                String featured = job.optString(AppStrings.Responsedata.featured);
                String timeDateStamp = job.optString(AppStrings.Responsedata.timeDateStamp);
                String merchantID = job.optString(AppStrings.Responsedata.merchantID);
                String storeName = job.optString(AppStrings.Responsedata.storeName);
                HomeModel model = new HomeModel();
                model.setAdID(adID);
                model.setImage(imageBaseUrl + image);
                model.setDiscountName(discountName);
                model.setDiscountPrice(AppStrings.symbols.dollar+discountPrice);
                model.setOriginalPrice(AppStrings.symbols.dollar+originalPrice);
                model.setStartTimeDate(startTimeDate);
                model.setEndTimeDate(endTimeDate);
                model.setDescription(description);
                model.setFeatured(featured);
                model.setTimeDateStamp(timeDateStamp);
                model.setMerchantID(merchantID);
                model.setStoreName(storeName);
                addList.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void discountApiJson(String response, boolean status) {

        if(status) {
            discountList.clear();
            adapter = new HomeAdapter(discountList, getActivity());
            manager = new LinearLayoutManager(getActivity());
            manager = AppMethods.getLinearLayoutWithOutScroll(getActivity());
            home_rv.setAdapter(adapter);
            home_rv.setLayoutManager(manager);
        }
        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String discounts = jo.optString(AppStrings.Responsedata.discounts);

                totalCount       =jo.getInt(AppStrings.Responsedata.totalCount);
                apidiscountJsonData(imageBaseURL, discounts);
//                home_srl.setRefreshing(false);
                adapter.notifyDataSetChanged();
            } else {
//                home_srl.setRefreshing(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adverstismentApiJson(String response, boolean status) {
        addList.clear();
        try {
            JSONObject jo = new JSONObject(response);
            String message = jo.optString(AppStrings.Responsedata.message);
            if (jo.getInt(AppStrings.Responsedata.status) == Appintegers.statuscode.success) {
                String imageBaseURL = jo.optString(AppStrings.Responsedata.imageBaseURL);
                String Advertisements = jo.optString(AppStrings.Responsedata.Advertisements);

                apiaddJsonData(imageBaseURL, Advertisements);

                homeViewpagerAdapter.notifyDataSetChanged();
              /*  homeViewpagerAdapter = new HomeViewpagerAdapter(addList ,getActivity());
                home_vpager.setOffscreenPageLimit(homeViewpagerAdapter.getCount());
                home_vpager.setAdapter(homeViewpagerAdapter);
*/
              /*  home_vpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        page = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
*/
                if(addList.isEmpty()){

                    home_vpager.setVisibility(View.GONE);
                }else{
                    home_vpager.setVisibility(View.VISIBLE);
                }

            } else {

                if(addList.isEmpty()){

                    home_vpager.setVisibility(View.GONE);
                }else{
                    home_vpager.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void call4Home(){


    }

   /* @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }*/



//    @Override
//    public void onRefresh() {
//        discountApi();
//    }

}
    /*private class HomeViewPagerAdapter extends PagerAdapter {

        int NumberOfPages = addList.size();


        @Override
        public float getPageWidth(int position) {
            return (1.0f);
        }


        @Override
        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View page = getActivity().getLayoutInflater().inflate(R.layout.row_home_image_slide, container, false);

            ImageView row_home_image_iv = (ImageView) page.findViewById(R.id.row_home_image_iv);
            TextView row_home_image_title_tv = (TextView) page.findViewById(R.id.row_home_image_title_tv);
            TextView row_image_des_tv = (TextView) page.findViewById(R.id.row_image_des_tv);
            TextView row_home_image_discount_price_tv = (TextView) page.findViewById(R.id.row_home_image_discount_price_tv);
            TextView row_home_image_original_price_tv = (TextView) page.findViewById(R.id.row_home_image_original_price_tv);

            final HomeModel model = addList.get(position);

            row_home_image_original_price_tv.setText(model.getOriginalPrice());
            row_home_image_discount_price_tv.setText(model.getDiscountPrice());
            row_image_des_tv.setText(model.getDescription());
            row_home_image_title_tv.setText(model.getDiscountName());

            Log.e(TAG, "instantiateItem: model "+model.getImage() );
            Glide.with(getActivity())
                    .load(model.getImage()).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                    .into(row_home_image_iv);


            container.addView(page);
            page.setTag(position);

            return (page);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

    }
}*/
