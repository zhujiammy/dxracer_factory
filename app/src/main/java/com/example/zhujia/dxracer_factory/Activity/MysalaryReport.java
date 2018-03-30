package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Adapter.MysalaryDetailAdapter;
import com.example.zhujia.dxracer_factory.Adapter.MysalaryReportAdapter;
import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.Data.ConsumableData;
import com.example.zhujia.dxracer_factory.Data.MData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.OnLoadMoreListener;
import com.example.zhujia.dxracer_factory.Tools.OnRefreshListener;
import com.example.zhujia.dxracer_factory.Tools.SuperRefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by ZHUJIA on 2018/1/2.
 *
 * 每日汇总
 */

public class MysalaryReport extends AppCompatActivity implements View.OnClickListener,OnRefreshListener,OnLoadMoreListener {

    private LineChartView lineChart;
    private LineChartData data;
    private Toolbar toolbar;
    private Spinner salaryMonth;
    private Handler mHandler;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonSession,salaryCardId;
    Map<String,String> params;
    List<AllData> dicts1 = new ArrayList<AllData>();
    private JSONObject reslutJSONObject;
    private SuperRefreshRecyclerView recyclerView;
    private List<MData> mListData=new ArrayList<>();
    private MysalaryReportAdapter adapter;
    private List<ConsumableData> consumableData=new ArrayList<>();
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysalaryreport);
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
        initdate();
        adapter=new MysalaryReportAdapter(MysalaryReport.this,getData());
    }



    private  void initdate(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("personId",departmentPersonSession);
        new HttpUtils().post(Constant.APPURLS+"/servlet/salary/mysalary/reporthtm",params,new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Message msg=Message.obtain(
                        mHandler,0,data
                );
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {
                // TODO Auto-generated method stub
                super.onError(msg);
                Toast.makeText(MysalaryReport.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loaddata(String salaryMonth){

        params=new HashMap<>();
        params.put("businessId",business_id);
        if(salaryMonth==null){
            params.put("salaryMonth","");
        }else {
            params.put("salaryMonth",salaryMonth);
        }

        params.put("personId",departmentPersonSession);
        new HttpUtils().post(Constant.APPURLS+"/servlet/salary/mysalary/reportjson",params,new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Message msg=Message.obtain(
                        mHandler,1,data
                );
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {
                // TODO Auto-generated method stub
                super.onError(msg);
                Toast.makeText(MysalaryReport.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void loadList(String salaryCardId){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("sorttype","asc");
        params.put("sort","salaryDate");
        if(salaryCardId==null){
            params.put("salaryCardId","");
        }else {
            params.put("salaryCardId",salaryCardId);
        }
        params.put("personId",departmentPersonSession);
        new HttpUtils().post(Constant.APPURLS+"/servlet/salary/salarycarditems/list",params,new HttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                Message msg=Message.obtain(
                        mHandler,2,data
                );
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String msg) {
                // TODO Auto-generated method stub
                super.onError(msg);
                Toast.makeText(MysalaryReport.this, "error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView(){
        lineChart=(LineChartView)findViewById(R.id.lineChart);
        lineChart.setZoomEnabled(false);
        //初始化
        recyclerView= (SuperRefreshRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.init(this,this);
        recyclerView.setRefreshEnabled(true);
        recyclerView.setLoadingMoreEnable(true);
        recyclerView.setHasFixedSize(true);
        salaryMonth=(Spinner)findViewById(R.id.salaryMonth);
        salaryMonth.setOnItemSelectedListener(listener1);
    }


    //计件年月
    Spinner.OnItemSelectedListener listener1=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            salaryCardId=((AllData)salaryMonth.getSelectedItem()).getStr();
           String SalaryMonth=((AllData)salaryMonth.getSelectedItem()).getText();
            loaddata(SalaryMonth);
            loadList(salaryCardId);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @SuppressLint("HandlerLeak")
    private List<MData>getData(){
    mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){

                    case 0:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONArray searchYearMonth=reslutJSONObject.getJSONArray("searchYearMonth");
                        for(int i=0;i<searchYearMonth.length();i++){
                            JSONObject object=searchYearMonth.getJSONObject(i);
                            dicts1.add(new AllData(object.getString("id"),object.getString("salaryMonth")));
                            ArrayAdapter<AllData> arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
                            //设置样式
                            arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            salaryMonth.setAdapter(arrAdapterpay3);
                        }
                        break;
                    case 1:
                        List<PointValue> pointValues = new ArrayList<PointValue>();// 节点数据结合
                        Axis axisY = new Axis().setHasLines(true);// Y轴属性
                        Axis axisX = new Axis();// X轴属性
                       // axisY.setName(String yName);//设置Y轴显示名称
                        //axisX.setName(String xName);//设置X轴显示名称
                        ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
                        ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
                        axisX.setValues(axisValuesX);//为X轴显示的刻度值设置数据集合
                        axisX.setLineColor(Color.BLACK);// 设置X轴轴线颜色
                        axisY.setLineColor(Color.BLACK);// 设置Y轴轴线颜色
                       //axisX.setTextColor(Color color);// 设置X轴文字颜色
                        //axisY.setTextColor(Color color);// 设置Y轴文字颜色
                        axisX.setTextSize(14);// 设置X轴文字大小
                        axisX.setTypeface(Typeface.DEFAULT);// 设置文字样式，此处为默认
                        //axisX.setHasTiltedLabels(bolean isHasTit);// 设置X轴文字向左旋转45度
                        //axisX.setHasLines(boolean isHasLines);// 是否显示X轴网格线
                        //axisY.setHasLines(boolean isHasLines);// 是否显示Y轴网格线
                        //axisX.setHasSeparationLine(boolean isHasSeparationLine);// 设置是否有分割线
                        //axisX.setInside(boolean isInside);// 设置X轴文字是否在X轴内部

                        JSONArray jsonArray=new JSONArray(msg.obj.toString());
                        consumableData.clear();
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object1=jsonArray.getJSONObject(i);
                            consumableData.add(new ConsumableData(object1.getString("finish_date"),object1.getString("total_fee")));
                        }

                        for(int j=0;j<consumableData.size();j++){
                            float num= Float.parseFloat(consumableData.get(j).getText());
                            DecimalFormat fnum  =   new  DecimalFormat("##0.00");
                            String dd=fnum.format(num);
                            Log.e("TAG", "handleMessage: "+Float.parseFloat(dd) );
                            pointValues.add(new PointValue(j,Float.valueOf(dd)));// 添加节点数据
                            axisValuesY.add(new AxisValue(j).setValue(j));// 添加Y轴显示的刻度值
                            axisValuesX.add(new AxisValue(j).setValue(j).setLabel(consumableData.get(j).getNum()));// 添加X轴显示的刻度值
                        }

                        List<Line> lines = new ArrayList<Line>();//定义线的集合
                        Line line = new Line(pointValues);//将值设置给折线
                        line.setColor(Color.parseColor("#0D589A"));// 设置折线颜色
                        line.setCubic(true);// 是否设置为立体的
                        line.setHasLabels(true);// 是否显示节点数据
                        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
                        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
                        line.setFilled(false);//是否填充曲线的面积
                        LineChartValueFormatter chartValueFormatter=new SimpleLineChartValueFormatter(2);
                        line.setFormatter(chartValueFormatter);
                        //line.setHasLabels(true);//曲线的数据坐标是否加上备注
                        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
                        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
                        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
                        lines.add(line);// 将数据集合添加线

                        data = new LineChartData(lines);//将线的集合设置为折线图的数据
                        data.setAxisYLeft(axisY);// 将Y轴属性设置到左边
                        data.setAxisXBottom(axisX);// 将X轴属性设置到底部
                        //data.setAxisYRight(axisYRight);//设置右边显示的轴
                        //data.setAxisXTop(axisXTop);//设置顶部显示的轴
                        data.setBaseValue(20);// 设置反向覆盖区域颜色
                        data.setValueLabelBackgroundAuto(false);// 设置数据背景是否跟随节点颜色
                        data.setValueLabelBackgroundColor(Color.BLUE);// 设置数据背景颜色
                        data.setValueLabelBackgroundEnabled(false);// 设置是否有数据背景
                        data.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
                        data.setValueLabelTextSize(15);// 设置数据文字大小
                        data.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式
                        lineChart.setInteractive(true);
                        lineChart.setZoomType(ZoomType.HORIZONTAL);
                        lineChart.setMaxZoom((float) 2);//最大方法比例
                        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
                        lineChart.setLineChartData(data);//最后为图表设置数据，数据类型为LineChartData
                        lineChart.setVisibility(View.VISIBLE);
                        Viewport v = new Viewport(lineChart.getMaximumViewport());
                        v.left = 0;
                        v.right = 8;
                        lineChart.setCurrentViewport(v);
                        axisX.setMaxLabelChars(8);
                        break;

                    case 2:
                        //返回item类型数据
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        mListData.clear();
                        fillDataToList(reslutJSONObject);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        recyclerView.showData();
                        recyclerView.setRefreshing(false);
                        recyclerView.setLoadingMore(false);
                        recyclerView.setLoadingMoreEnable(false);
                        break;

                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
    };





        return mListData;
    }

    private void fillDataToList(JSONObject data) throws JSONException {

        if(!data.isNull("rows")){
          JSONArray  contentjsonarry=data.getJSONArray("rows");
            MData rechargData=null;
            for(int i=0;i<contentjsonarry.length();i++){
                rechargData=new MData();
                JSONObject object=contentjsonarry.getJSONObject(i);
                rechargData.setSalaryDate(object.getString("salaryDate"));
                rechargData.setSalaryWeekday(object.getString("salaryWeekday"));
                rechargData.setSalaryType(object.getString("salaryType"));
                rechargData.setSalary(object.getString("salary"));
                mListData.add(rechargData);
            }
        }


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListData.clear();
                adapter.notifyDataSetChanged();
                loadList(salaryCardId);

            }
        },1000);
    }

    @Override
    public void onLoadMore() {

    }
}
