package com.leona.handlers;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leona.R;
import com.leona.models.HomeModel;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.widget.RoundedCornersTransformation;

import java.util.ArrayList;

/**
 * Created by krify on 15/5/17.
 */

public class ResdetailsViewpagerAdapter extends PagerAdapter {



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

    public ResdetailsViewpagerAdapter(ArrayList<HomeModel> arrayList, Activity activity) {

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
        View itemView = activity.getLayoutInflater().inflate(R.layout.row_home_adapter, container, false);


        ImageView row_home_iv = (ImageView) itemView.findViewById(R.id.row_home_iv);
        ImageView row_home_featured_iv = (ImageView) itemView.findViewById(R.id.row_home_featured_iv);
        TextView row_home_title_name_tv = (TextView) itemView.findViewById(R.id.row_home_title_name_tv);
        TextView row_home_des_name_tv = (TextView) itemView.findViewById(R.id.row_home_des_name_tv);
        TextView row_home_price_tv = (TextView) itemView.findViewById(R.id.row_home_price_tv);
        final TextView row_home_time_tv = (TextView) itemView.findViewById(R.id.row_home_time_tv);
        TextView row_home_orginal_price_tv = (TextView) itemView.findViewById(R.id.row_home_orginal_price_tv);
        RelativeLayout row_home_share_rl = (RelativeLayout) itemView.findViewById(R.id.row_home_share_rl);
        RelativeLayout row_home__content_rl = (RelativeLayout) itemView.findViewById(R.id.row_home__content_rl);





        final HomeModel model = addList.get(position);


        Glide.with(activity)
                .load(model.getImage()).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                .bitmapTransform(new RoundedCornersTransformation(activity, 30, 0))
                .into(row_home_iv);

        row_home_title_name_tv.setText(model.getDiscountName());
        // holder.row_home_des_name_tv.setText(model.getDescription());
        row_home_price_tv.setText(AppStrings.symbols.dollar+model.getDiscountPrice());
        row_home_orginal_price_tv.setText(AppStrings.symbols.dollar+model.getOriginalPrice());

        params = row_home_iv.getLayoutParams();
        params.height =    d.getHeight() / 4;
        params.width =   d.getWidth() ;

        if (model.getFeatured().equals("1")) {
            row_home_featured_iv.setVisibility(View.VISIBLE);
        } else {
            row_home_featured_iv.setVisibility(View.GONE);
        }


        long timer = AppMethods.getMilliseconds(model.getEndTimeDate());;

        // timer = timer*1000;

        CountDownTimer timer1 = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
                row_home_time_tv.setText(AppMethods.DateToAgoFormat(model.getEndTimeDate()));
            }

            public void onFinish() {
                row_home_time_tv.setText("00:00:00");
            }
        }.start();




        container.addView(itemView);
        itemView.setTag(position);

        return (itemView);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
