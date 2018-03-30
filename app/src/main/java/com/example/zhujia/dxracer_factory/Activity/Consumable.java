package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.zhujia.dxracer_factory.Data.ConsumableData;
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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by ZHUJIA on 2018/1/2.
 *
 * 易耗品报表
 */

public class Consumable extends AppCompatActivity {

    private ColumnChartView columnChartView,columnChartView1,columnChartView2;
    private ColumnChartData data,data1,data2;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonSession;
    Map<String,String> params;
    private List<ConsumableData> consumableData=new ArrayList<>();
    private List<ConsumableData> consumableData1=new ArrayList<>();
    private List<ConsumableData> consumableData2=new ArrayList<>();
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumalbel);
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
        sharedPreferences =getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonSession=sharedPreferences.getString("departmentPersonId","");
        initView();
        loaddata();
    }


    private void loaddata(){

        params=new HashMap<>();
        params.put("businessId",business_id);
        HttpUtility.doPostAsyn("/servlet/officegoods/chart/consumable", params, new IHttpCallBack() {
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
        },mHandler,Consumable.this);
    }
    private void initView(){
        columnChartView=(ColumnChartView)findViewById(R.id.chart);
        columnChartView.setZoomEnabled(false);
        columnChartView1=(ColumnChartView)findViewById(R.id.chart1);
        columnChartView1.setZoomEnabled(false);
        columnChartView2=(ColumnChartView)findViewById(R.id.chart2);
        columnChartView2.setZoomEnabled(false);
    }



    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 1:
                        JSONObject object=new JSONObject(msg.obj.toString());
                        JSONArray purchaseChart=object.getJSONArray("purchaseChart");

                        for(int i=0;i<purchaseChart.length();i++){
                            JSONObject object1=purchaseChart.getJSONObject(i);
                            consumableData.add(new ConsumableData(object1.getString("num"),object1.getString("durableType")+"("+object1.getString("unit")+")"));
                        }

                        int numSubcolumns=1;
                        int numColumns=consumableData.size();
                        Log.e("TAG", "initData: "+consumableData.size() );
                        List<AxisValue>axisValues=new ArrayList<>();
                        List<Column>columns=new ArrayList<>();
                        List<SubcolumnValue>values;
                        for(int i=0;i<numColumns;i++){
                            values=new ArrayList<>();
                            for(int j=0;j<numSubcolumns;j++){
                                values.add(new SubcolumnValue(Float.parseFloat(consumableData.get(i).getNum()),
                                        ChartUtils.pickColor()));
                            }
                            //点击柱状图就展示数据
                            columns.add(new Column(values).setHasLabels(true).setHasLabelsOnlyForSelected(true));
                            axisValues.add(new AxisValue(i).setLabel(consumableData.get(i).getText()));
                        }

                       /* axisValues.add(new AxisValue(5).setLabel(consumableData.get(5).getText()));
                        axisValues.add(new AxisValue(15).setLabel(consumableData.get(15).getText()));
                        axisValues.add(new AxisValue(20).setLabel(consumableData.get(20).getText()));
                        axisValues.add(new AxisValue(25).setLabel(consumableData.get(25).getText()));
                        axisValues.add(new AxisValue(34).setLabel(consumableData.get(34).getText()));*/
                        data=new ColumnChartData(columns);
                        data.setAxisXBottom(new Axis(axisValues).setHasLines(true).setTextColor(Color.BLACK).setHasTiltedLabels(true).setTextSize(10).setInside(false));
                        data.setAxisYLeft(new Axis().setHasLines(true).setTextColor(Color.BLACK).setMaxLabelChars(3));
                        columnChartView.setColumnChartData(data);

                        //采购金额
                        JSONArray purchaseCostChart=object.getJSONArray("purchaseCostChart");

                        for(int i=0;i<purchaseCostChart.length();i++){
                            JSONObject object1=purchaseCostChart.getJSONObject(i);
                            consumableData1.add(new ConsumableData(object1.getString("subtotal"),object1.getString("durableType")));
                        }
                        int numSubcolumns1=1;
                        int numColumns1=consumableData1.size();
                        Log.e("TAG", "initData: "+consumableData1.size() );
                        List<AxisValue>axisValues1=new ArrayList<>();
                        List<Column>columns1=new ArrayList<>();
                        List<SubcolumnValue>values1;
                        for(int i=0;i<numColumns1;i++){
                            values1=new ArrayList<>();
                            for(int j=0;j<numSubcolumns1;j++){
                                values1.add(new SubcolumnValue(Float.parseFloat(consumableData1.get(i).getNum()),
                                        ChartUtils.pickColor()));
                            }
                            //点击柱状图就展示数据
                            columns1.add(new Column(values1).setHasLabels(true).setHasLabelsOnlyForSelected(true));
                            axisValues1.add(new AxisValue(i).setLabel(consumableData1.get(i).getText()));
                        }

                    /*    axisValues1.add(new AxisValue(5).setLabel(consumableData1.get(5).getText()));
                        axisValues1.add(new AxisValue(15).setLabel(consumableData1.get(15).getText()));
                        axisValues1.add(new AxisValue(20).setLabel(consumableData1.get(20).getText()));
                        axisValues1.add(new AxisValue(25).setLabel(consumableData1.get(25).getText()));
                        axisValues1.add(new AxisValue(34).setLabel(consumableData1.get(34).getText()));*/
                        data1=new ColumnChartData(columns1);
                        data1.setAxisXBottom(new Axis(axisValues1).setHasLines(true).setTextColor(Color.BLACK).setHasTiltedLabels(true).setTextSize(10).setInside(false));
                        data1.setAxisYLeft(new Axis().setHasLines(true).setTextColor(Color.BLACK).setMaxLabelChars(5));
                        columnChartView1.setColumnChartData(data1);


                        //当前正在使用

                        JSONArray userNumChart=object.getJSONArray("userNumChart");

                        for(int i=0;i<userNumChart.length();i++){
                            JSONObject object1=userNumChart.getJSONObject(i);
                            consumableData2.add(new ConsumableData(object1.getString("num"),object1.getString("durableType")));
                        }

                        int numSubcolumns2=1;
                        int numColumns2=consumableData2.size();
                        Log.e("TAG", "initData: "+consumableData2.size() );
                        List<AxisValue>axisValues3=new ArrayList<>();
                        List<Column>columns2=new ArrayList<>();
                        List<SubcolumnValue>values2;

                        for(int i=0;i<numColumns2;i++){
                            values2=new ArrayList<>();
                            for(int j=0;j<numSubcolumns2;j++){
                                values2.add(new SubcolumnValue(Float.parseFloat(consumableData2.get(i).getNum()),
                                        ChartUtils.pickColor()));
                            }
                            //点击柱状图就展示数据
                            columns2.add(new Column(values2).setHasLabels(true).setHasLabelsOnlyForSelected(true));
                            axisValues3.add(new AxisValue(i).setLabel(consumableData2.get(i).getText()));
                        }
                        //Log.e("TAG", "handleMessage: "+consumableData2.get(0).getText());

                 /*       axisValues3.add(new AxisValue(2).setLabel(consumableData2.get(2).getText()));
                        axisValues3.add(new AxisValue(3).setLabel(consumableData2.get(3).getText()));
                        axisValues3.add(new AxisValue(6).setLabel(consumableData2.get(6).getText()));
                        axisValues3.add(new AxisValue(8).setLabel(consumableData2.get(8).getText()));
                        axisValues3.add(new AxisValue(10).setLabel(consumableData2.get(10).getText()));
                        axisValues3.add(new AxisValue(12).setLabel(consumableData2.get(12).getText()));
                        axisValues3.add(new AxisValue(14).setLabel(consumableData2.get(14).getText()));
                        axisValues3.add(new AxisValue(16).setLabel(consumableData2.get(16).getText()));*/
                        data2=new ColumnChartData(columns2);
                        data2.setAxisXBottom(new Axis(axisValues3).setHasLines(true).setTextColor(Color.BLACK).setHasTiltedLabels(true).setTextSize(10).setInside(false));
                        data2.setAxisYLeft(new Axis().setHasLines(true).setTextColor(Color.BLACK).setMaxLabelChars(2));
                        columnChartView2.setColumnChartData(data2);
                        break;

                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
    };
}
