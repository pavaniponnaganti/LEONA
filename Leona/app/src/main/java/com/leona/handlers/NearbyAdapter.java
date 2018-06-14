package com.leona.handlers;

import android.app.Activity;
import android.app.Fragment;
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
import com.leona.home.HomeFragment;
import com.leona.home.NearByFragment;
import com.leona.home.RestaruntDetails;
import com.leona.models.HomeModel;
import com.leona.models.NearbyModel;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.widget.RoundedCornersTransformation;

import java.util.ArrayList;

/**
 * Created by krify on 9/5/17.
 */

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.HomeFolder> {

    NearByFragment nearFragment;
    ArrayList<NearbyModel> arrayList;
    Activity activity;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;

    public NearbyAdapter(NearByFragment nearFragment, ArrayList<NearbyModel> arrayList, Activity activity) {
        this.nearFragment = nearFragment;
        this.arrayList = arrayList;
        this.activity = activity;

        w = activity.getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);
    }

    @Override
    public NearbyAdapter.HomeFolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_restaurent_item, null);
        NearbyAdapter.HomeFolder holder = new NearbyAdapter.HomeFolder(view);

        return holder;
    }




    @Override
    public void onBindViewHolder(final NearbyAdapter.HomeFolder holder, int position) {
        final NearbyModel model = arrayList.get(position);


        Glide.with(activity)
                .load(model.getStoreImage()).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                .bitmapTransform(new RoundedCornersTransformation(activity, 30, 0))
                .into(holder.row_rest_iv);

        holder.row_rest_title_name_tv.setText(model.getStoreName());
        holder.row_rest_distace_tv.setText(model.getDistance());
        holder.row_rest_offers_tv.setText(model.getOffersCount());



        params = holder.row_rest_iv.getLayoutParams();
        params.height =    d.getHeight() / 3;
        params.width =   d.getWidth() ;

         params = holder.row_content_rl.getLayoutParams();
        params.height =    d.getHeight() / 3;
        params.width =   d.getWidth() ;



        holder.row_content_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(activity, RestaruntDetails.class);
                i.putExtra(AppStrings.Responsedata.merchantID,model.getMerchantID());
                activity.startActivity(i);
            }
        });


        holder.row_rest_share_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String share =  AppMethods.getShareDataRes(model ,activity);
                AppMethods.shareViaIntent(activity, share);


              // AppMethods.shareViaIntent(activity, model.getStoreName() +model.getStoreImage());

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HomeFolder extends RecyclerView.ViewHolder {

        ImageView row_rest_iv, row_rest_featured_iv;
        TextView row_rest_title_name_tv, row_rest_des_name_tv, row_rest_offers_tv,
                row_rest_distace_tv;
        RelativeLayout row_rest_share_rl ,row_content_rl;



        public HomeFolder(View itemView) {
            super(itemView);

            row_rest_iv = (ImageView) itemView.findViewById(R.id.row_rest_iv);
            row_rest_featured_iv = (ImageView) itemView.findViewById(R.id.row_rest_featured_iv);
            row_rest_title_name_tv = (TextView) itemView.findViewById(R.id.row_rest_title_name_tv);

            row_rest_offers_tv = (TextView) itemView.findViewById(R.id.row_rest_offers_tv);
            row_rest_distace_tv = (TextView) itemView.findViewById(R.id.row_rest_distace_tv);
            row_rest_share_rl = (RelativeLayout) itemView.findViewById(R.id.row_rest_share_rl);
            row_content_rl = (RelativeLayout) itemView.findViewById(R.id.row_content_rl);

        }
    }
}