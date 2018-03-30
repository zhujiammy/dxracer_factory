package com.example.zhujia.dxracer_factory.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import net.sf.json.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhujia on 2017/12/5.
 *
 * 排单参数
 */

public class Rowparameter extends Fragment implements View.OnClickListener {

    private EditText normalOutput,overtimeOutput,queueDay,field2;
    Map<String,String> params;
    private SharedPreferences sharedPreferences;
    private String business_id;
    private Button add;
    private View view;
    private java.util.Calendar cal;
    private int year,month,day;
    private TextView field1;

    @SuppressLint("WrongConstant")

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.rowparameter,container,false);
        sharedPreferences =getActivity().getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        intUI();
        loaddata();
        getDate();//获取当前日期

        return view;

    }

    private void loaddata(){

        params=new HashMap<>();


        params.put("businessId",business_id);
        HttpUtility.doPostAsyn("/servlet/production/productionconfig", params, new IHttpCallBack() {
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

    @SuppressLint("WrongConstant")
    private void getDate(){
        cal= java.util.Calendar.getInstance();
        year=cal.get(java.util.Calendar.YEAR);
        month=cal.get(java.util.Calendar.MONTH);
        day=cal.get(java.util.Calendar.DAY_OF_MONTH);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                switch (msg.what) {

                    case 0:
                        //返回item类型数据
                       JSONObject reslutJSONObject=new JSONObject(msg.obj.toString());
                        String result_code=reslutJSONObject.getString("result_code");
                        if(result_code.equals("success")){
                            JSONObject rows=reslutJSONObject.getJSONObject("rows");
                            JSONObject data=rows.getJSONObject("data");
                            normalOutput.setText(data.getString("normalOutput"));
                            overtimeOutput.setText(data.getString("overtimeOutput"));
                            queueDay.setText(data.getString("queueDay"));
                            field1.setText(data.getString("field1"));
                            field2.setText(data.getString("field2"));
                        }
                        break;
                    case 1:
                        JSONObject object=new JSONObject(msg.obj.toString());
                        String code=object.getString("result_code");
                        String msg1=object.getString("result_msg");
                        if(code.equals("success")){
                           Toast.makeText(getActivity(),msg1,Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getActivity(),msg1,Toast.LENGTH_LONG).show();
                        }
                        break;

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };


    private void intUI(){
        normalOutput=(EditText)view.findViewById(R.id.normalOutput);
        overtimeOutput=(EditText)view.findViewById(R.id.overtimeOutput);
        queueDay=(EditText)view.findViewById(R.id.queueDay);
        field1=(TextView) view.findViewById(R.id.field1);
        field2=(EditText)view.findViewById(R.id.field2);
        add=(Button)view.findViewById(R.id.add);
        add.setOnClickListener(this);
        field1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==add){
            if(TextUtils.isEmpty(normalOutput.getText().toString())){
                Toast.makeText(getActivity(),"正常产量不能为空",Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(overtimeOutput.getText().toString())){
                Toast.makeText(getActivity(),"加班产量不能为空",Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(queueDay.getText().toString())){
                Toast.makeText(getActivity(),"插单限制天数不能为空",Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(field1.getText().toString())){
                Toast.makeText(getActivity(),"初始排单日期不能为空",Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(field2.getText().toString())){
                Toast.makeText(getActivity(),"限制接单数不能为空",Toast.LENGTH_LONG).show();
            }else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("id","1");
                params.put("normalOutput",normalOutput.getText().toString());
                params.put("overtimeOutput",overtimeOutput.getText().toString());
                params.put("queueDay",queueDay.getText().toString());
                params.put("field1",field1.getText().toString());
                params.put("field2",field2.getText().toString());

                HttpUtility.doPostAsyn("/servlet/production/productionconfig/add", params, new IHttpCallBack() {
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

        }

        if(v==field1){
            //选择日期
            DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String date=(i+"-"+(++i1)+"-"+i2);
                    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    try {
                        date1 = format1.parse(date);
                        String startime = format1.format(date1);
                        field1.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(getActivity(),DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
        }
    }
}
