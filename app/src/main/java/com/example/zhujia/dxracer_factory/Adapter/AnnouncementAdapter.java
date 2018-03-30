package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.zhujia.dxracer_factory.Activity.Notice;
import com.example.zhujia.dxracer_factory.Data.AnnouncementData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.BaseRecyclerAdapter;
import com.example.zhujia.dxracer_factory.Tools.insertComma;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/9/6.
 */

public class AnnouncementAdapter extends BaseRecyclerAdapter<BaseRecyclerAdapter.BaseRecyclerViewHolder> implements View.OnClickListener  {

    private List<AnnouncementData> data;
    public Context context;
    private int type=0;
    private AnnouncementAdapter.OnitemClickListener onitemClickListener=null;
    private SharedPreferences sharedPreferences;
    private String dealerId;
    private String status;
    Map<String,String> params;

    @SuppressLint("WrongConstant")
    public AnnouncementAdapter(Context context1, List<AnnouncementData> data){
        this.context=context1;
        this.data=data;
        sharedPreferences =context1.getSharedPreferences("Session",
                Context.MODE_APPEND);
        dealerId=sharedPreferences.getString("dealerId","");
    }
    @Override
    public void onClick(View view) {
        if(onitemClickListener!=null){
            onitemClickListener.onItemClick(view,(int)view.getTag());
        }


    }

    public void setOnitemClickListener(AnnouncementAdapter.OnitemClickListener onitemClickListener) {
        this.onitemClickListener = onitemClickListener;
    }

    public static interface OnitemClickListener{
        void onItemClick(View view, int position);
    }


    //点击切换布局的时候通过这个方法设置type
    public void setType(int type) {
        this.type = type;
    }

    @Override
    //用来获取当前项Item是哪种类型的布局
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View baseView;
        if (viewType == 0) {
            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_data, parent, false);
            AnnouncementAdapter.LinearViewHolder linearViewHolder = new AnnouncementAdapter.LinearViewHolder(baseView);
            baseView.setOnClickListener(this);
            return linearViewHolder;
        } else {
            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_data, parent, false);
            AnnouncementAdapter.GridViewHolder gridViewHolder = new AnnouncementAdapter.GridViewHolder(baseView);
            baseView.setOnClickListener(this);
            return gridViewHolder;
        }

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, List<Map<String, Object>> data) {

    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        if (type==0){
            final AnnouncementAdapter.LinearViewHolder linearViewHolder= (AnnouncementAdapter.LinearViewHolder) holder;
            linearViewHolder.annTheme.setText(data.get(position).getAnnTheme());
            linearViewHolder.annWriter.setText(data.get(position).getAnnWriter());
            linearViewHolder.createTime.setText(insertComma.stampToDate(data.get(position).getCreateTime()));
            linearViewHolder.lin_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Notice.class);
                    intent.putExtra("content",data.get(position).getAnnContent());
                    context.startActivity(intent);
                }
            });
        }
    }


    public static class LinearViewHolder extends BaseRecyclerViewHolder {

        private TextView annTheme,annWriter,createTime;
        private LinearLayout EDbtn,delbtn,lin_btn;
        public LinearViewHolder(View itemView) {
            super(itemView);
            annTheme=(TextView)itemView.findViewById(R.id.annTheme);
            annWriter=(TextView)itemView.findViewById(R.id.annWriter);
            createTime=(TextView)itemView.findViewById(R.id.createTime);
            lin_btn=(LinearLayout)itemView.findViewById(R.id.lin_btn);
        }
    }

    public static class GridViewHolder extends BaseRecyclerViewHolder {


        public GridViewHolder(View itemView) {
            super(itemView);

        }
    }

}