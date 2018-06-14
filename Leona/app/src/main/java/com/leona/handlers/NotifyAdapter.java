package com.leona.handlers;

import android.app.Activity;
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
import com.leona.models.NotifyModel;
import com.leona.utils.AppStrings;
import com.leona.widget.RoundedCornersTransformation;

import java.util.ArrayList;

/**
 * Created by krify on 9/5/17.
 */

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.HomeFolder> {


    ArrayList<NotifyModel> arrayList;
    Activity activity;

    ViewGroup.LayoutParams params;
    WindowManager w;
    Display d;
    DisplayMetrics metrics;

    public NotifyAdapter(ArrayList<NotifyModel> arrayList, Activity activity) {

        this.arrayList = arrayList;
        this.activity = activity;

        w = activity.getWindowManager();
        d = w.getDefaultDisplay();
        metrics = new DisplayMetrics();
        d.getMetrics(metrics);
    }

    @Override
    public NotifyAdapter.HomeFolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(activity, R.layout.row_list_notifications, null);
        NotifyAdapter.HomeFolder holder = new NotifyAdapter.HomeFolder(view);

        return holder;
    }




    @Override
    public void onBindViewHolder(final NotifyAdapter.HomeFolder holder, int position) {
        final NotifyModel model = arrayList.get(position);



        holder.list_notify_time_tv.setText(model.getMessage());
        holder.list_notify_data_tv.setText(model.getCreatDatetime());








    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HomeFolder extends RecyclerView.ViewHolder {

        ImageView list_notify_bell_iv;
        TextView list_notify_time_tv, list_notify_data_tv;

        RelativeLayout row_content_rl;



        public HomeFolder(View itemView) {
            super(itemView);

            list_notify_bell_iv = (ImageView) itemView.findViewById(R.id.list_notify_bell_iv);
            list_notify_data_tv = (TextView) itemView.findViewById(R.id.list_notify_time_tv);

            list_notify_time_tv = (TextView) itemView.findViewById(R.id.list_notify_message_tv);

        }
    }
}