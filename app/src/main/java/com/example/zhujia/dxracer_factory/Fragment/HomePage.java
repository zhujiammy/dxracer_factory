package com.example.zhujia.dxracer_factory.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhujia.dxracer_factory.Activity.Afterwarehouse;
import com.example.zhujia.dxracer_factory.Activity.Batchwarehouse;
import com.example.zhujia.dxracer_factory.Activity.Detainedwarehouse;
import com.example.zhujia.dxracer_factory.Activity.Devicemanagement;
import com.example.zhujia.dxracer_factory.Activity.Managementgoods;
import com.example.zhujia.dxracer_factory.Activity.MySalary;
import com.example.zhujia.dxracer_factory.Activity.Myindex;
import com.example.zhujia.dxracer_factory.Activity.Personnelmatters;
import com.example.zhujia.dxracer_factory.Activity.PurchasingmgMG;
import com.example.zhujia.dxracer_factory.Activity.Shipmentorder;
import com.example.zhujia.dxracer_factory.Activity.Singlerow;
import com.example.zhujia.dxracer_factory.Activity.Spotwarehouses;
import com.example.zhujia.dxracer_factory.Adapter.MyAdapter;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.LoginActivitys;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.FixedGridLayout;
import com.example.zhujia.dxracer_factory.Tools.MaskableImageView;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DXSW5 on 2017/10/16.
 */

public class HomePage extends Fragment implements View.OnClickListener {
    private MaskableImageView singlerow,Batchwarehouses,Spotwarehouse,devicemanagement,Mysalary,Aftersalewarehouse,purchasingmg,Domesticshipments,Foreignshipments,personnelmanagement,Managementgoodss,zlckms;
    private TextView balance;
    private String user,comp,business_id,password;
    private CircleImageView profile_image;
    private SharedPreferences sharedPreferences,sharedPreferences1;
    private String roleId,userid,id;
    Map<String,String> params;
    private int pageindex=1;
    private View view;
    private FixedGridLayout grid;
    android.app.AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private List<String> list=new ArrayList<>();
    private List<String> Mlist=new ArrayList<>();
    private LinearLayout pdsc,plck,xhck,shck,cgkl,gnch,gwch,rsgl,wpgl,zlck,sbgl,wdgz;
    private ViewGroup.LayoutParams lp;


    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.home_page,container,false);
        sharedPreferences =getActivity().getSharedPreferences("Session",
                Context.MODE_APPEND);
        sharedPreferences1 =getActivity().getSharedPreferences("quanxian",
                Context.MODE_APPEND);
        roleId=sharedPreferences.getString("roleId","");
        user=sharedPreferences.getString("username","");
        comp=sharedPreferences.getString("supplierName","");
        business_id=sharedPreferences.getString("business_id","");
        userid=sharedPreferences.getString("systemUser_id","");
        password=sharedPreferences.getString("password","");
        id=sharedPreferences.getString("departmentPersonId","");
        initUI();
        loaddata();
        load(roleId);
        return view;
    }


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("page", String.valueOf(pageindex));
        params.put("id",id);
        HttpUtility.doPostAsyn("/servlet/organization/departmentperson/myindex", params, new IHttpCallBack() {
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
        },mHandler,getActivity());
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
        },mHandler,getActivity());
    }
    private void initUI(){
        grid=(FixedGridLayout)view.findViewById(R.id.grid);
        balance=(TextView)view.findViewById(R.id.balance);
        balance.setText(comp+" "+user);
        profile_image=(CircleImageView)view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        plck=(LinearLayout)view.findViewById(R.id.plck);
        pdsc=(LinearLayout)view.findViewById(R.id.pdsc);
        xhck=(LinearLayout)view.findViewById(R.id.xhck);
        shck=(LinearLayout)view.findViewById(R.id.shck);
        cgkl=(LinearLayout)view.findViewById(R.id.cgkl);
        gnch=(LinearLayout)view.findViewById(R.id.gnch);
        gwch=(LinearLayout)view.findViewById(R.id.gwch);
        rsgl=(LinearLayout)view.findViewById(R.id.rsgl);
        wpgl=(LinearLayout)view.findViewById(R.id.wpgl);
        zlck=(LinearLayout)view.findViewById(R.id.zlck);
        sbgl=(LinearLayout)view.findViewById(R.id.sbgl);
        wdgz=(LinearLayout)view.findViewById(R.id.wdgz);

        singlerow=(MaskableImageView)view.findViewById(R.id.Singlerow);
        singlerow.setOnClickListener(listener);
        Batchwarehouses=(MaskableImageView)view.findViewById(R.id.Batchwarehouse);
        Batchwarehouses.setOnClickListener(listener1);
        Spotwarehouse=(MaskableImageView)view.findViewById(R.id.Spotwarehouse);
        Spotwarehouse.setOnClickListener(listener2);
        Aftersalewarehouse=(MaskableImageView)view.findViewById(R.id.Aftersalewarehouse);
        Aftersalewarehouse.setOnClickListener(listener3);
        purchasingmg=(MaskableImageView)view.findViewById(R.id.purchasingmg);
        purchasingmg.setOnClickListener(listener4);
        Domesticshipments=(MaskableImageView)view.findViewById(R.id.Domesticshipments);
        Domesticshipments.setOnClickListener(listener5);
        Foreignshipments=(MaskableImageView)view.findViewById(R.id.Foreignshipments);
        Foreignshipments.setOnClickListener(listener6);
        personnelmanagement=(MaskableImageView)view.findViewById(R.id.personnelmanagement);
        personnelmanagement.setOnClickListener(listener7);
        Managementgoodss=(MaskableImageView)view.findViewById(R.id.Managementgoods);
        Managementgoodss.setOnClickListener(listener8);
        zlckms=(MaskableImageView)view.findViewById(R.id.zlckms);
        zlckms.setOnClickListener(listener9);
        devicemanagement=(MaskableImageView)view.findViewById(R.id.devicemanagement);
        devicemanagement.setOnClickListener(listener10);
        Mysalary=(MaskableImageView)view.findViewById(R.id.Mysalary);
        Mysalary.setOnClickListener(listener11);


        grid.removeView(pdsc);
        grid.removeView(xhck);
        grid.removeView(shck);
        grid.removeView(cgkl);
        grid.removeView(gnch);
        grid.removeView(gwch);
        grid.removeView(rsgl);
        grid.removeView(wpgl);
        grid.removeView(zlck);
        grid.removeView(plck);
        grid.removeView(sbgl);
        grid.removeView(wdgz);

    }


    //排单生产
    MaskableImageView.OnClickListener listener=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Singlerow.class);
            startActivity(intent);
        }
    };

    //批量仓库
    MaskableImageView.OnClickListener listener1=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Batchwarehouse.class);
            startActivity(intent);
        }
    };

    //滞留仓库
    MaskableImageView.OnClickListener listener9=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {

            Intent intent=new Intent(getContext(), Detainedwarehouse.class);
            startActivity(intent);
        }
    };
    //现货仓库
    MaskableImageView.OnClickListener listener2=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Spotwarehouses.class);
            startActivity(intent);
        }
    };
    //现货仓库
    MaskableImageView.OnClickListener listener3=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Afterwarehouse.class);
            startActivity(intent);
        }
    };
    //采购管理
    MaskableImageView.OnClickListener listener4=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), PurchasingmgMG.class);
            startActivity(intent);
        }
    };

    //国内出货
    MaskableImageView.OnClickListener listener5=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Shipmentorder.class);
            intent.putExtra("type","0");
            startActivity(intent);
        }
    };

    //国外出货
    MaskableImageView.OnClickListener listener6=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Shipmentorder.class);
            intent.putExtra("type","1");
            startActivity(intent);
        }
    };

    //人事管理
    MaskableImageView.OnClickListener listener7=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Personnelmatters.class);
            startActivity(intent);
        }
    };

    //物品管理
    MaskableImageView.OnClickListener listener8=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Managementgoods.class);
            startActivity(intent);
        }
    };

    //设备管理
    MaskableImageView.OnClickListener listener10=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), Devicemanagement.class);
            startActivity(intent);
        }
    };

    //我的工资
    MaskableImageView.OnClickListener listener11=new MaskableImageView.OnClickListener() {
        @Override
        public void onClick() {
            Intent intent=new Intent(getContext(), MySalary.class);
            startActivity(intent);
        }
    };


    @Override
    public void onClick(View v) {
        if(v==profile_image){
            showPopWindows(v);
        }
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

                case 0:
                   JSONObject data=new JSONObject(msg.obj.toString());
                   JSONArray contentjsonarry=data.getJSONArray("rows");
                    for(int i=0;i<contentjsonarry.length();i++){
                        JSONObject object=contentjsonarry.getJSONObject(i);
                        if(!object.isNull("file")){
                            Glide.with(getActivity()).load(Constant.APPURLIMG+object.getString("file")).into(profile_image);
                        }else {
                            profile_image.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.img_def));
                        }

                    }

                    break;

                case 1:// 解析返回mode类型数据

                        JSONObject reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONObject rows=reslutJSONObject.getJSONObject("rows");
                        JSONArray resultList=rows.getJSONArray("resultList");
                        for(int i=0;i<resultList.length();i++){
                            JSONObject object=resultList.getJSONObject(i);
                            if(object.getString("id").equals("46"))
                            {
                                grid.removeView(pdsc);
                                grid.addView(pdsc);
                            }
                            if(object.getString("id").equals("54"))
                            {
                                Log.e("TAG", "handleMessage: "+"有批量仓库");
                                grid.removeView(plck);
                                grid.addView(plck);
                            }
                            if(object.getString("id").equals("64"))
                            {
                                grid.removeView(cgkl);
                                grid.addView(cgkl);
                            }
                            if(object.getString("id").equals("545"))
                            {
                                grid.removeView(zlck);
                                grid.addView(zlck);
                                Log.e("TAG", "handleMessage: "+"有滞留仓库");
                            }
                            if(object.getString("id").equals("382"))
                            {
                                grid.removeView(gnch);
                                grid.addView(gnch);
                            }
                            if(object.getString("id").equals("517"))
                            {
                                grid.removeView(gwch);
                                grid.addView(gwch);
                            }
                            if(object.getString("id").equals("43"))
                            {
                                grid.removeView(xhck);
                                grid.addView(xhck);
                            }

                            if(object.getString("id").equals("259"))
                            {
                                grid.removeView(shck);
                                grid.addView(shck);
                            }
                            if(object.getString("id").equals("71"))
                            {
                                grid.removeView(rsgl);
                                grid.addView(rsgl);
                            }
                            if(object.getString("id").equals("776"))
                            {
                                grid.removeView(wpgl);
                                grid.addView(wpgl);
                            }
                            if(object.getString("id").equals("633"))
                            {
                                grid.removeView(sbgl);
                                grid.addView(sbgl);
                            }
                            if(object.getString("id").equals("1346")){
                                grid.removeView(wdgz);
                                grid.addView(wdgz);
                            }

                        }



                    break;
                case 2:
                    JSONObject reslutJSONObjects=new JSONObject(msg.obj.toString());
                    fillDataToList(reslutJSONObjects);
                    break;
                
                default:
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    break;
            }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };





    private void  loadPId(String id){
        params=new HashMap<>();
        params.put("roleId",id);
        params.put("pId","46");
        HttpUtility.doPostAsyn("/user/getAppResourceByRPId", params, new IHttpCallBack() {
            @Override
            public void onRequestComplete(String result, Handler handler, String errcode) {
                if ((null == result) || (result.equals(""))) {
                    // 网络连接异常
                    mHandler.sendEmptyMessage(9);

                }else {

                    Message msg=Message.obtain(
                            handler,2,result
                    );
                    mHandler.sendMessage(msg);

                }
            }
        },mHandler,getActivity());
    }


    private void fillDataToList(JSONObject data) throws JSONException {
        JSONObject contentjsonobject=data.getJSONObject("rows");
        JSONArray  contentjsonarry=contentjsonobject.getJSONArray("resultList");
        for(int i=0;i<contentjsonarry.length();i++){
            JSONObject object=contentjsonarry.getJSONObject(i);
            Mlist.add(object.getString("NAME"));
        }
        String s= Arrays.toString(Mlist.toArray());
        SharedPreferences sp= getActivity().getSharedPreferences("quanxian", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("arr",s.substring(1,s.length()-1) );
        editor.commit();

    }

    private void showPopWindows(View view){
        //自定义布局
        View contentView= LayoutInflater.from(getActivity()).inflate(R.layout.popxml,null);
        TextView edpass=(TextView)contentView.findViewById(R.id.etpass);
        TextView out=(TextView)contentView.findViewById(R.id.out);
        //修改密码
        edpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETpass();
            }
        });

        //退出
        out.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.clear().commit();
                Intent intent =new Intent(getActivity(), LoginActivitys.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        final PopupWindow popupWindow=new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.base));
        popupWindow.showAsDropDown(view);
    }

    //修改密码
    private void ETpass(){
        builder=new android.app.AlertDialog.Builder(getActivity());
        final View view1= LayoutInflater.from(getActivity()).inflate(R.layout.depass,null);
        final EditText newPassword=(EditText)view1.findViewById(R.id.newPassword);
        final EditText confirmPassword=(EditText)view1.findViewById(R.id.confirmPassword);
        builder.setNegativeButton(getResources().getString(R.string.cel),null);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             if(TextUtils.isEmpty(newPassword.getText()))
                {
                    Toast.makeText(getActivity(),getResources().getString(R.string.ts), Toast.LENGTH_SHORT).show();
                } else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.ts1), Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog = new ProgressDialog(getActivity(),
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getResources().getString(R.string.save));
                    progressDialog.show();
                    params=new HashMap<>();
                    params.put("businessId",business_id);
                    params.put("userId",userid);
                    params.put("newPassword",newPassword.getText().toString());
                    HttpUtility.doPostAsyn("/user/restMyPwd", params, new IHttpCallBack() {
                        @Override
                        public void onRequestComplete(String result, Handler handler, String errcode) {
                            if ((null == result) || (result.equals(""))) {
                                // 网络连接异常
                                mHandler.sendEmptyMessage(9);

                            }else {
                                JSONObject resulutJsonobj;
                                try
                                {
                                    resulutJsonobj=new JSONObject(result);
                                    String result_code=resulutJsonobj.getString("result_code");
                                    if(result_code.equals("success")){
                                        progressDialog.dismiss();
                                        Looper.prepare();
                                        Toast.makeText(getActivity(),"修改成功,下次登录时生效", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    },mHandler,getActivity());
                }

            }
        });
        builder.setView(view1);
        builder.create().show();
    }

    public String listToString(List list, char separator)
    {    StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0,sb.toString().length()-1);}

}
