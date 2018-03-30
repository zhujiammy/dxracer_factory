package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
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
 * Created by DXSW5 on 2017/10/10.
 *
 * 物品列表
 */

public class Leaduse extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private String business_id,dealerId,loginId,username;
    private String  params;
    private ListAdapter mListAdapter;// adapter
    private ListView mListView;
    private List<MData> mListData=new ArrayList<>();
    private CheckBox mCheckAll; // 全选 全不选
    private TextView mDelete; // 订单预览
    net.sf.json.JSONArray jsonArray;
    JSONObject reslutJSONObject;
    private Toolbar toolbar;
    private Spinner type,durableTypeId;
    private ViewGroup.LayoutParams lp;
    private LinearLayout seach_view;
    private TextView seach_btn;
    private Intent intent;

    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaduse);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        initListener();
        sharedPreferences =getSharedPreferences("Session",
                Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        loginId=sharedPreferences.getString("systemUser_id","");
        username=sharedPreferences.getString("username","");
        intent=getIntent();
        String reslut=intent.getStringExtra("rows");
        if(reslut!=null){
            mListData=getData(reslut);
        }


    }
    private void initListener() {
        mDelete.setOnClickListener(this);
        mCheckAll.setOnClickListener(this);
    }
    private void initView() {

        mCheckAll = (CheckBox)findViewById(R.id.check_box);
        mDelete = (TextView)findViewById(R.id.tv_cart_buy_or_del);
        mListView = (ListView)findViewById(R.id.listview);
        mListView.setSelector(R.drawable.list_selector);
        type=(Spinner)findViewById(R.id.type);
        durableTypeId=(Spinner)findViewById(R.id.durableTypeId);
        seach_view=(LinearLayout)findViewById(R.id.seach_view);
        seach_btn=(TextView)findViewById(R.id.seach_btn);
        seach_btn.setOnClickListener(this);


    }
    private final List<Integer> getSelectedIds()
    {
        ArrayList<Integer> selectedIds = new ArrayList<Integer>();
        for (int index = 0; index < mSelectState.size(); index++)
        {
            if (mSelectState.valueAt(index))
            {
                selectedIds.add(mSelectState.keyAt(index));
            }
        }
        return selectedIds;
    }


    @Override
    protected void onResume() {
        super.onResume();
        String reslut=intent.getStringExtra("rows");
        if(reslut!=null){
            mListData=getData(reslut);
        }
    }

    private void refreshListView()
    {
        if (mListAdapter == null)
        {
            mListAdapter = new Leaduse.ListAdapter();
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListAdapter);

        } else
        {  mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListAdapter);
            mListAdapter.notifyDataSetChanged();

        }
    }

    private class ListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
    {
        HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

        @Override
        public int getCount()
        {
            return mListData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent)
        {
            Leaduse.ViewHolder holder = null;
            View view = convertView;
            if (view == null)
            {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.leaduse_data, null);
                holder = new Leaduse.ViewHolder(view);
                view.setTag(holder);
            } else
            {
                holder = (Leaduse.ViewHolder) view.getTag();
            }
            final MData data = mListData.get(position);
            bindListItem(holder, data);
     /*      holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        state.put(position,b);
                    }else {
                        state.remove(position);
                    }
                }
            });*/
            return view;
        }

        private void bindListItem(Leaduse.ViewHolder holder, final MData data)
        {

            final HashMap<Integer,Boolean>state=new HashMap<Integer,Boolean>();
            holder.code.setText(data.getGoodsItemCode());
            holder.name.setText(data.getName());
            holder.brand.setText(data.getBrand());
            holder.model.setText(data.getModel());
            holder.durableType.setText(data.getDurableType());
            if(!data.getGoodsItemId().equals("null")){
                holder.num.setText("1");
            }else if(data.getStock()>0){
                holder.num.setText(String.valueOf(data.getStock()));
            }
            holder.unit.setText(data.getUnit());
            holder.standard.setText(data.getStandard());
            final int _id = data.getId();
            final boolean selected = mSelectState.get(_id, false);
            holder.checkBox.setChecked(selected);

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            final MData bean = mListData.get(position);
            Leaduse.ViewHolder holder = (Leaduse.ViewHolder) view.getTag();
            final int _id =bean.getId();
            final boolean selected = !mSelectState.get(_id, false);
            holder.checkBox.toggle();
            if (selected)
            {
                mSelectState.put(_id, true);
            } else
            {
                mSelectState.delete(_id);
            }
            if (mSelectState.size() == mListData.size())
            {
                mCheckAll.setChecked(true);
            } else
            {
                mCheckAll.setChecked(false);
            }



        }

    }

    class ViewHolder
    {
        CheckBox checkBox;
        LinearLayout Lin_btn;
        TextView code,name,durableType,brand,model,standard,num,unit;

        public ViewHolder(View view)
        {
            code=(TextView)view.findViewById(R.id.code);
            name=(TextView)view.findViewById(R.id.name);
            durableType=(TextView)view.findViewById(R.id.durableType);
            brand=(TextView)view.findViewById(R.id.brand);
            model=(TextView)view.findViewById(R.id.model);
            standard=(TextView)view.findViewById(R.id.standard);
            num=(TextView)view.findViewById(R.id.num);
            unit=(TextView)view.findViewById(R.id.unit);
            checkBox = (CheckBox) view.findViewById(R.id.check_box);
        }
    }

    @SuppressLint("HandlerLeak")
    private List<MData> getData(String reslut)
    {
        final List<MData> result1 = new ArrayList<MData>();
                try {

                            Log.e("TAG", "handleMessage: "+reslut );
                            reslutJSONObject=new JSONObject(reslut);
                            JSONArray resultlist=reslutJSONObject.getJSONArray("list");
                            MData data = null;
                            for (int i = 0; i < resultlist.length(); i++)
                            {
                                JSONObject object=resultlist.getJSONObject(i);
                                data = new MData();
                                data.setId(i);
                                data.setID(object.getInt("goodsId"));
                                data.setPic(object.getString("picture"));
                                data.setCode(object.getString("code"));
                                data.setName(object.getString("name"));
                                data.setBrand(object.getString("brand"));
                                data.setStandard(object.getString("standard"));
                                data.setUnit(object.getString("unit"));
                                data.setCreateTime(object.getString("createTime"));
                                data.setType(object.getString("type"));
                                if(!object.isNull("goodsItemCode")){
                                    data.setGoodsItemCode(object.getString("goodsItemCode"));
                                }else {
                                    data.setGoodsItemCode("");
                                }

                                data.setUnitId(object.getString("unitId"));
                                if(!object.isNull("price")){
                                    data.setPrice(object.getString("price"));
                                }else {
                                    data.setPrice("");
                                }

                                if(!object.isNull("model")){
                                    data.setModel(object.getString("model"));
                                }else {
                                    data.setModel("");
                                }

                                data.setDurableType(object.getString("durableType"));
                                if(!object.isNull("goodsItemId")){
                                    data.setGoodsItemId(object.getString("goodsItemId"));
                                }else {
                                    data.setGoodsItemId("null");
                                }

                                data.setStock(object.getInt("stock"));
                                result1.add(data);
                            } Log.e("tag","relust1"+result1.size());
                            refreshListView();

                }catch (JSONException e){
                    e.printStackTrace();
                }



        return result1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_box:
                if (mCheckAll.isChecked())
                {

                    if (mListData != null)
                    {
                        mSelectState.clear();
                        int size = mListData.size();
                        if (size == 0)
                        {
                            return;
                        }
                        for (int i = 0; i < size; i++)
                        {
                            int _id = mListData.get(i).getId();
                            mSelectState.put(_id, true);
                            //num+=mListData.get(i).getTotalFee();

                        }
                        refreshListView();
                    }
                } else
                {
                    if (mListAdapter != null)
                    {
                        mSelectState.clear();
                        refreshListView();


                    }
                }
                break;

            case R.id.tv_cart_buy_or_del:
                List<Integer> ids1 = getSelectedIds();
                dosettlemetn(ids1);
                break;
            case R.id.seach_btn:
                intent =new Intent(getApplicationContext(),Listitems.class);
                startActivity(intent);
                finish();
                break;
        }
    }



    private void dosettlemetn(List<Integer> ids1){
        float Toatfree;
        int orderIds;
        net.sf.json.JSONObject jsonObj = new net.sf.json.JSONObject();
        net.sf.json.JSONObject jsonObject=new net.sf.json.JSONObject();
        try{
            jsonArray=new net.sf.json.JSONArray();
            for(int i=0;i<mListData.size();i++){
                long dataId=mListData.get(i).getId();
                for(int j=0;j<ids1.size();j++){
                    int id=ids1.get(j);
                    if(dataId==id){
                        orderIds=mListData.get(j).getID();
                        jsonObj.put("id", "");
                        jsonObj.put("goodsId",orderIds);
                        jsonObj.put("brand",mListData.get(j).getBrand());
                        jsonObj.put("businessId",business_id);
                        jsonObj.put("code",mListData.get(j).getCode());
                        jsonObj.put("createTime",mListData.get(j).getCreateTime());
                        jsonObj.put("durableType",mListData.get(j).getDurableType());
                        jsonObj.put("goodsItemCode",mListData.get(j).getGoodsItemCode());
                        jsonObj.put("model",mListData.get(j).getModel());
                        jsonObj.put("name",mListData.get(j).getName());
                        jsonObj.put("picture",mListData.get(j).getPic());
                        jsonObj.put("price",mListData.get(j).getPrice());
                        jsonObj.put("unitId",mListData.get(j).getUnitId());
                        jsonObj.put("stock",mListData.get(j).getStock());
                        jsonObj.put("standard",mListData.get(j).getStandard());
                        jsonObj.put("unit",mListData.get(j).getUnit());
                        jsonObj.put("goodsItemId",mListData.get(j).getGoodsItemId());
                        jsonObj.put("num",1);


                        jsonObj.put("type",mListData.get(j).getType());

                        jsonArray.add(jsonObj);
                    }
                }
            }

        }catch (net.sf.json.JSONException e){
            e.printStackTrace();
        }
        if(jsonArray.size()==0){

        }else {
            Log.e("TAG", "dosettlemetn: "+jsonArray);

            try{
                net.sf.json.JSONObject jsonObjs = new net.sf.json.JSONObject();
                jsonObjs.put("businessId",business_id);
                jsonObjs.put("userId",loginId);
                jsonObjs.put("username",username);
                jsonObjs.element("items",jsonArray);
                params=jsonObjs.toString().replace("\\","");
                Log.e("TAG", "json: "+params );
                HttpUtility.doPostAsyn1("/servlet/officegoods/officegoodstakeitem/add", params, new IHttpCallBack() {
                    @Override
                    public void onRequestComplete(String result, Handler handler, String errcode) {
                        if ((null == result) || (result.equals(""))) {
                            // 网络连接异常
                            mHandler.sendEmptyMessage(9);

                        }else {

                            Message msg=Message.obtain(
                                    handler,0,result
                            );
                            mHandler.sendMessage(msg);

                        }
                    }
                },mHandler,this);
            }catch (net.sf.json.JSONException e){
                e.printStackTrace();
            }


        }



    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
          try{
              switch (msg.what){
                  case 0:
                        JSONObject object=new JSONObject(msg.obj.toString());
                        String message=object.getString("message");
                        if(object.getString("code").equals("success")){
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }
                      break;
              }
          }catch (JSONException e){
              e.printStackTrace();
          }
        }
    };
    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }

}
