package com.leona.handlers;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leona.R;
import com.leona.home.AddDetails;
import com.leona.home.DiscountDetails;
import com.leona.models.HomeModel;
import com.leona.utils.AppStrings;

import java.util.ArrayList;

/**
 * Created by krify on 15/5/17.
 */

public class HomeViewpagerAdapter  extends PagerAdapter {



    ArrayList<HomeModel> addList;
    Activity activity;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;

    @Override
    public float getPageWidth(int position) {
        return (1.0f);
    }

    public HomeViewpagerAdapter(ArrayList<HomeModel> arrayList, Activity activity) {

        this.addList = arrayList;
        this.activity = activity;

      //NumberOfPages =addList.size();


        w = activity.getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);
    }
    @Override
    public int getCount() {
        return addList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View page = activity.getLayoutInflater().inflate(R.layout.row_home_image_slide, container, false);

        ImageView row_home_image_iv = (ImageView) page.findViewById(R.id.row_home_image_iv);
        TextView row_home_image_title_tv = (TextView) page.findViewById(R.id.row_home_image_title_tv);
        TextView row_image_des_tv = (TextView) page.findViewById(R.id.row_image_des_tv);
        TextView row_home_image_discount_price_tv = (TextView) page.findViewById(R.id.row_home_image_discount_price_tv);
        TextView row_home_image_original_price_tv = (TextView) page.findViewById(R.id.row_home_image_original_price_tv);

        final HomeModel model = addList.get(position);

        row_home_image_original_price_tv.setText(model.getOriginalPrice());
        row_home_image_discount_price_tv.setText(model.getDiscountPrice());
        row_image_des_tv.setText(model.getStoreName());
        row_home_image_title_tv.setText(model.getDiscountName());


        Glide.with(activity)
                .load(model.getImage()).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                .into(row_home_image_iv);

        params = row_home_image_iv.getLayoutParams();
        params.height =    d.getHeight() / 2;
        params.width =   d.getWidth() ;


        row_home_image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, AddDetails.class) ;
                i.putExtra(AppStrings.Responsedata.adID,model.getAdID()) ;


                activity.startActivity(i);
            }
        });

        container.addView(page);
        page.setTag(position);

        return (page);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
