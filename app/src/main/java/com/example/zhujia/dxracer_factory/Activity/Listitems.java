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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Listitems extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private String business_id,dealerId,loginId,username;
    private Map<String,String> params;
    private ListAdapter mListAdapter;// adapter
    private ListView mListView;
    private List<MData> mListData=new ArrayList<>();
    private CheckBox mCheckAll; // 全选 全不选
    private TextView mDelete; // 订单预览
    JSONArray jsonArray;
    JSONObject reslutJSONObject;
    private Toolbar toolbar;
    private Handler mHandler;
    private float num;
    private int istouch=0;
    private TextView name,code,brand,model,btn_colse;
    private Spinner type,durableTypeId;
    private ViewGroup.LayoutParams lp;
    private LinearLayout seach_view;

    List<String>ids =new ArrayList<>();
    List<String>orderIds1 =new ArrayList<>();
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listitem);
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

        loaddata();
        mListData=getData();

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
        name=(TextView)findViewById(R.id.name);
        code=(TextView)findViewById(R.id.code);
        brand=(TextView)findViewById(R.id.brand);
        model=(TextView)findViewById(R.id.model);
        type=(Spinner)findViewById(R.id.type);
        durableTypeId=(Spinner)findViewById(R.id.durableTypeId);
        seach_view=(LinearLayout)findViewById(R.id.seach_view);
        btn_colse=(TextView)findViewById(R.id.btn_colse);
        btn_colse.setOnClickListener(this);


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
    private void refreshListView()
    {
        if (mListAdapter == null)
        {
            mListAdapter = new Listitems.ListAdapter();
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
            Listitems.ViewHolder holder = null;
            View view = convertView;
            if (view == null)
            {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.cart_list_item1, null);
                holder = new Listitems.ViewHolder(view);
                view.setTag(holder);
            } else
            {
                holder = (Listitems.ViewHolder) view.getTag();
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

        private void bindListItem(Listitems.ViewHolder holder, final MData data)
        {

            final HashMap<Integer,Boolean>state=new HashMap<Integer,Boolean>();
            if(!data.getPic().equals("null")){
                Glide.with(getApplicationContext()).load(Constant.APPURLIMG+data.getPic()).into(holder.picture);
            }

            holder.code.setText(data.getCode());
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
            Listitems.ViewHolder holder = (Listitems.ViewHolder) view.getTag();
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
        private ImageView picture;

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
            picture=(ImageView)view.findViewById(R.id.picture);
        }
    }
    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("sorttype","asc");
        params.put("sort","code asc,goodsItemCode");
        params.put("name", name.getText().toString());
        params.put("code",code.getText().toString());
        params.put("brand",brand.getText().toString());
        params.put("model",model.getText().toString());
        params.put("type","");
        params.put("durableTypeId","");
        HttpUtility.doPostAsyn("/servlet/officegoods/officegoods/take/list", params, new IHttpCallBack() {
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
    }

    @SuppressLint("HandlerLeak")
    private List<MData> getData()
    {
        final List<MData> result1 = new ArrayList<MData>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {

                    switch (msg.what){
                        case 0:

                            mListData.clear();
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            if(!reslutJSONObject.isNull("rows")){
                                JSONArray resultlist=reslutJSONObject.getJSONArray("rows");
                                MData data = null;
                                for (int i = 0; i < resultlist.length(); i++)
                                {
                                    JSONObject object=resultlist.getJSONObject(i);
                                    data = new MData();
                                    data.setId(i);
                                    data.setID(object.getInt("id"));
                                    if(!object.isNull("picture")){
                                        data.setPic(object.getString("picture"));
                                    }else {
                                        data.setPic("-");
                                    }

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
                            }

                            break;

                        case 1:
                            reslutJSONObject=new JSONObject(msg.obj.toString());
                            String result_code=reslutJSONObject.getString("result_code");
                            if(result_code.equals("success")){
                                mListData.clear();
                                loaddata();
                                mListData=getData();
                            }
                            break;
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        };
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
            case R.id.btn_colse:
                finish();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.seach_btn) {
            loaddata();
        }
        if(id==R.id.open_seach){
            //打开搜索界面
            if(istouch==0){
                lp= seach_view.getLayoutParams();
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ViewGroup.LayoutParams.WRAP_CONTENT, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.up);
                istouch=1;

                //backgroundAlpha(0.5f);

            }else {
                lp= seach_view.getLayoutParams();
                lp.height=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
                seach_view.setLayoutParams(lp);
                item.setIcon(R.drawable.down);
                istouch=0;
                // backgroundAlpha(1f);

            }


        }

        return super.onOptionsItemSelected(item);
    }

    private void dosettlemetn(List<Integer> ids1){
        float Toatfree;
        int orderIds;
        net.sf.json.JSONObject jsonObj = new net.sf.json.JSONObject();
        net.sf.json.JSONObject jsonObject=new net.sf.json.JSONObject();
        try{
            net.sf.json.JSONArray jsonArray=new net.sf.json.JSONArray();
            for(int i=0;i<mListData.size();i++){
                long dataId=mListData.get(i).getId();
                for(int j=0;j<ids1.size();j++){
                    int id=ids1.get(j);
                    if(dataId==id){
                        orderIds=mListData.get(j).getID();
                        jsonObj.put("id","");
                        jsonObj.put("goodsId",orderIds);
                        jsonObj.put("brand",mListData.get(j).getBrand());
                        jsonObj.put("businessId",mListData.get(j).getBusinessId());
                        jsonObj.put("code",mListData.get(j).getCode());
                        jsonObj.put("createTime",mListData.get(j).getCreateTime());
                        jsonObj.put("durableType",mListData.get(j).getDurableType());
                        jsonObj.put("goodsItemCode",mListData.get(j).getGoodsItemCode());
                        jsonObj.put("model",mListData.get(j).getModel());
                        jsonObj.put("name",mListData.get(j).getName());
                        jsonObj.put("num",mListData.get(j).getNum());
                        jsonObj.put("picture",mListData.get(j).getPic());
                        jsonObj.put("price",mListData.get(j).getPrice());
                        jsonObj.put("unitId",mListData.get(j).getUnitId());
                        jsonObj.put("stock",mListData.get(j).getStock());
                        jsonObj.put("standard",mListData.get(j).getStandard());
                        jsonObj.put("unit",mListData.get(j).getUnit());
                        jsonObj.put("goodsItemId",mListData.get(j).getGoodsItemId());
                        jsonObj.put("type",mListData.get(j).getType());
                        jsonArray.add(jsonObj);
                    }
                }
            }
            jsonObject.element("list",jsonArray);

        }catch (net.sf.json.JSONException e){
            e.printStackTrace();
        }
        if(jsonObject.getJSONArray("list").size()==0){

        }else {
            Log.e("TAG", "dosettlemetn: "+jsonObject);

           Intent intent=new Intent(getApplicationContext(),Leaduse.class);
            intent.putExtra("rows",jsonObject.toString());
            startActivity(intent);
            finish();

        }



    }
    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);
    }

}
