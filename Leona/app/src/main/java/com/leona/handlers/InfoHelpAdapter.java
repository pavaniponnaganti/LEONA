package com.leona.handlers;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.leona.R;
import com.leona.home.InfoHelpActivity;
import com.leona.models.InfoHelpModel;

import java.util.ArrayList;


public class InfoHelpAdapter extends RecyclerView.Adapter<InfoHelpAdapter.ViewHolder>{
    private static final String TAG = "InfoHelpAdapter";
    Activity activity;
    ArrayList<InfoHelpModel> arralist;

    public InfoHelpAdapter(Activity activity, ArrayList<InfoHelpModel> arrayList){
        this.activity = activity;
        this.arralist = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_info_help,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);
        final InfoHelpModel model = arralist.get(position);
        holder.info_help_question_tv.setText(model.getQuestion());
        holder.info_help_answer_tv.setText(model.getAnswer());

        if(arralist.get(position).isExpandStatus()){
            holder.info_help_question_main_ll.setBackgroundColor(ContextCompat.getColor(activity, R.color.bg_clor));

            holder.info_help_answer_tv.setVisibility(View.VISIBLE);
            holder.info_help_question_iv.setImageDrawable(activity.getResources().getDrawable(R.mipmap.down));


        }else {
            holder.info_help_answer_tv.setVisibility(View.GONE);
            holder.info_help_question_iv.setImageDrawable(activity.getResources().getDrawable(R.mipmap.for1));
        }

        holder.info_help_question_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InfoHelpActivity)activity).updateExpandStatus(position,arralist.get(position).isExpandStatus());

            }
        });



    }

    @Override
    public int getItemCount() {
        return arralist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView info_help_question_tv,info_help_answer_tv,info_help_queshead_tv;
        LinearLayout info_help_question_main_ll;
        ImageView info_help_question_iv;
        public ViewHolder(View itemView) {
            super(itemView);
            info_help_question_tv = (TextView)itemView.findViewById(R.id.info_help_question_tv);
            info_help_answer_tv = (TextView)itemView.findViewById(R.id.info_help_answer_tv);
             info_help_question_main_ll = (LinearLayout) itemView.findViewById(R.id.info_help_question_main_ll);
             info_help_question_iv = (ImageView) itemView.findViewById(R.id.info_help_question_iv);
        }
    }

}
