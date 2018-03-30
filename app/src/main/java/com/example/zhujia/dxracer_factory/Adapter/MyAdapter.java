package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhujia on 2017/12/13.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, Object>> dataList;
    private String roleId,userid;
    private List<String>list=new ArrayList<>();
    Map<String,String> params;
    private SharedPreferences sharedPreferences;
    /**
     * 数据资源：标题 ＋ 图片
     * */
    //图标
    int icno[] = {R.mipmap.pdsc,R.mipmap.plck,R.mipmap.xhck,R.mipmap.shck,R.mipmap.cggl,R.mipmap.gnch,R.mipmap.gwch};
    //图标下的文字
    String name[]={"排单生产","批量仓库","现货仓库","售后仓库","采购管理","国内出货","国外出货"};

    @SuppressLint("WrongConstant")
    public MyAdapter(Context context) {
        this.context = context;
        sharedPreferences =context.getSharedPreferences("Session",
                Context.MODE_APPEND);
        roleId=sharedPreferences.getString("roleId","");
        load(roleId);
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <icno.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);

        }

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.homeitem, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.img);
            //设置显示图片
            //holder.iv.setImageDrawable();
            holder.tv = (TextView) convertView.findViewById(R.id.text);
            //设置标题
            holder.tv.setText(dataList.get(position).get("text").toString());
            for(int i=0;i<list.size();i++){
                if(list.get(i).contains("46")){
                    //convertView.setVisibility(View.VISIBLE);
                    AbsListView.LayoutParams param = new AbsListView.LayoutParams(0, 0);
                    //将设置好的布局属性应用到GridView的Item上
                    convertView.setLayoutParams(param);
                }
            }

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        return convertView;
    }

    private void  load(String id){
        params=new HashMap<>();
        params.put("roleId",id);
        HttpUtility.doPostAsyn("/user/getAppResourceByRPId", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常
                    mHandler.sendEmptyMessage(9);

                }else {

                    Message msg=Message.obtain(
                            handler,1,result
                    );
                    mHandler.sendMessage(msg);

                }
            }
        },mHandler,context);
    }

    /**
     * 消息处理Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {

                    case 1:// 解析返回mode类型数据

                        JSONObject reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONObject rows=reslutJSONObject.getJSONObject("rows");
                        JSONArray resultList=rows.getJSONArray("resultList");

                        for(int i=0;i<resultList.length();i++){
                            JSONObject object=resultList.getJSONObject(i);
                            list.add(object.getString("id"));
                        }



                        break;
                    case 2:
                        JSONObject reslutJSONObjects=new JSONObject(msg.obj.toString());
                        break;



                    default:
                        Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show();
                        break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };
    class Holder {
        ImageView iv;
        TextView tv;
    }
}