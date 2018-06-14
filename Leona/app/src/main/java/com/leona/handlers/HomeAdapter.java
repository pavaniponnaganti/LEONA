package com.leona.handlers;


import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
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
import com.leona.home.DiscountDetails;
import com.leona.home.HomeFragment;
import com.leona.models.HomeModel;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.widget.RoundedCornersTransformation;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeFolder> {


    ArrayList<HomeModel> arrayList;
    Activity activity;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;

    public HomeAdapter( ArrayList<HomeModel> arrayList, Activity activity) {

        this.arrayList = arrayList;
        this.activity = activity;

        w = activity.getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);
    }

    @Override
    public HomeAdapter.HomeFolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_home_adapter, null);
        HomeFolder holder = new HomeFolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.HomeFolder holder, int position) {
        final HomeModel model = arrayList.get(position);


        Glide.with(activity)
                .load(model.getImage()).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                .bitmapTransform(new RoundedCornersTransformation(activity, 30, 0))
                .into(holder.row_home_iv);

        holder.row_home_title_name_tv.setText(model.getDiscountName());
        holder.row_home_des_name_tv.setText(model.getStoreName());
        holder.row_home_price_tv.setText(AppStrings.symbols.dollar+model.getDiscountPrice());
        holder.row_home_orginal_price_tv.setText(AppStrings.symbols.dollar+model.getOriginalPrice());

        params = holder.row_home_iv.getLayoutParams();
        params.height =    d.getHeight() / 4;
        params.width =   d.getWidth() ;

        if (model.getFeatured().equals("1")) {
            holder.row_home_featured_iv.setVisibility(View.VISIBLE);
        } else {
            holder.row_home_featured_iv.setVisibility(View.GONE);
        }


        long timer = AppMethods.getMilliseconds(model.getEndTimeDate());;

       // timer = timer*1000;

        holder.timer = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
                holder.row_home_time_tv.setText(AppMethods.DateToAgoFormat(model.getEndTimeDate()));
            }

            public void onFinish() {
                holder.row_home_time_tv.setText("00:00:00");
            }
        }.start();



        holder.row_home__content_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, DiscountDetails.class) ;
                i.putExtra(AppStrings.Responsedata.discountID,model.getDiscountID()) ;

                activity.startActivity(i);

            }
        });
        holder.row_home_share_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String share =  AppMethods.getShareData(model ,activity);
                AppMethods.shareViaIntent(activity, share);

               // AppMethods.shareViaIntent(activity, model.getDiscountName() +model.getImage());

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HomeFolder extends RecyclerView.ViewHolder {

        ImageView row_home_iv, row_home_featured_iv;
        TextView row_home_title_name_tv, row_home_des_name_tv, row_home_price_tv,
                row_home_time_tv, row_home_orginal_price_tv;
        RelativeLayout row_home_share_rl ,row_home__content_rl;

        CountDownTimer timer;

        public HomeFolder(View itemView) {
            super(itemView);

            row_home_iv = (ImageView) itemView.findViewById(R.id.row_home_iv);
            row_home_featured_iv = (ImageView) itemView.findViewById(R.id.row_home_featured_iv);
            row_home_title_name_tv = (TextView) itemView.findViewById(R.id.row_home_title_name_tv);
            row_home_des_name_tv = (TextView) itemView.findViewById(R.id.row_home_des_name_tv);
            row_home_price_tv = (TextView) itemView.findViewById(R.id.row_home_price_tv);
            row_home_time_tv = (TextView) itemView.findViewById(R.id.row_home_time_tv);
            row_home_orginal_price_tv = (TextView) itemView.findViewById(R.id.row_home_orginal_price_tv);
            row_home_share_rl = (RelativeLayout) itemView.findViewById(R.id.row_home_share_rl);
            row_home__content_rl = (RelativeLayout) itemView.findViewById(R.id.row_home__content_rl);

        }
    }
}
