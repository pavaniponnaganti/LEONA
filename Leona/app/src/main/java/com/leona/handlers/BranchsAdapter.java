package com.leona.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.leona.home.NearByFragment;
import com.leona.home.RestaruntDetails;
import com.leona.models.NearbyModel;
import com.leona.utils.AppMethods;
import com.leona.utils.AppStrings;
import com.leona.widget.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by krify on 9/5/17.
 */

public class BranchsAdapter extends RecyclerView.Adapter<BranchsAdapter.HomeFolder> {

    NearByFragment nearFragment;
    ArrayList<HashMap<String,Object>> arrayList;
    Activity activity;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;

    public BranchsAdapter(ArrayList<HashMap<String,Object>> arrayList, Activity activity) {

        this.arrayList = arrayList;
        this.activity = activity;

        w = activity.getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);
    }

    @Override
    public BranchsAdapter.HomeFolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_list_res_branch, null);
        BranchsAdapter.HomeFolder holder = new BranchsAdapter.HomeFolder(view);

        return holder;
    }




    @Override
    public void onBindViewHolder(final BranchsAdapter.HomeFolder holder, final int position) {
        arrayList.get(position);



        Glide.with(activity)
                .load(arrayList.get(position).get(AppStrings.Responsedata.pic)).placeholder(R.mipmap.bg_logo_sign).error(R.mipmap.bg_logo_sign)
                .bitmapTransform(new RoundedCornersTransformation(activity, 30, 0))
                .into(holder.row_rest_iv);

        holder.row_rest_title_name_tv.setText(arrayList.get(position).get(AppStrings.Responsedata.storeName).toString());
        holder.row_rest_distace_tv.setText(arrayList.get(position).get(AppStrings.Responsedata.distance).toString()+" km");
        holder.row_rest_address_tv.setText(arrayList.get(position).get(AppStrings.Responsedata.address).toString());
        holder.row_rest_phone_tv.setText(arrayList.get(position).get(AppStrings.Responsedata.phoneNum).toString());
        holder.row_rest_storetime_tv.setText(arrayList.get(position).get(AppStrings.Responsedata.storeTime).toString());



       /* params = holder.row_rest_iv.getLayoutParams();
        params.height =    d.getHeight() / 3;
        params.width =   d.getWidth() ;

         params = holder.row_content_rl.getLayoutParams();
        params.height =    d.getHeight() / 3;
        params.width =   d.getWidth() ;
*/

       // redirct2Maps();


        holder.row_content_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppMethods.redirct2Maps(activity,arrayList.get(position).get(AppStrings.Responsedata.latlng).toString(), arrayList.get(position).get(AppStrings.Responsedata.storeName).toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HomeFolder extends RecyclerView.ViewHolder {

        ImageView row_rest_iv;
        TextView row_rest_title_name_tv, row_rest_storetime_tv, row_rest_phone_tv,
                row_rest_distace_tv,row_rest_address_tv;
        RelativeLayout row_rest_share_rl ,row_content_rl;



        public HomeFolder(View itemView) {
            super(itemView);

            row_rest_iv = (ImageView) itemView.findViewById(R.id.row_list_res_branch_img_iv);
              row_rest_title_name_tv = (TextView) itemView.findViewById(R.id.row_list_res_branch_name_tv);
              row_rest_address_tv = (TextView) itemView.findViewById(R.id.row_list_res_branch_address_tv);

            row_rest_storetime_tv = (TextView) itemView.findViewById(R.id.row_list_res_branch_storetime_tv);
            row_rest_distace_tv = (TextView) itemView.findViewById(R.id.row_list_res_branch_distance_tv);
            row_rest_phone_tv = (TextView) itemView.findViewById(R.id.row_list_res_branch_phone_tv);
           //row_rest_share_rl = (RelativeLayout) itemView.findViewById(R.id.row_rest_share_rl);
            row_content_rl = (RelativeLayout) itemView.findViewById(R.id.row_list_res_branch_redrict_rl);

        }
    }
}