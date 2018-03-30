package com.example.zhujia.dxracer_factory.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.MyCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author baiyuliang
 *
 */
@SuppressLint("SimpleDateFormat")
public class Main_ac extends Activity implements MyCalendar.OnDaySelectListener{
	LinearLayout ll;
	 MyCalendar c1;
	 Date date;
	 String nowday;
	 long nd = 1000*24L*60L*60L;//一天的毫秒数
	 SimpleDateFormat simpleDateFormat,sd1,sd2;
	 
	 private String inday,outday,sp_inday,sp_outday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout3);
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        nowday=simpleDateFormat.format(new Date());
		sd1=new SimpleDateFormat("yyyy");
		sd2=new SimpleDateFormat("dd");
        ll=(LinearLayout) findViewById(R.id.ll);
        init();
    }
    
    private void init(){
    	List<String> listDate=getDateList();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for(int i=0;i<listDate.size();i++){
        	c1 = new MyCalendar(this);
            c1.setLayoutParams(params);
            Date date=null;
            try {
    		    date=simpleDateFormat.parse(listDate.get(i));
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
            if(!"".equals(sp_inday)){
            	c1.setInDay(sp_inday);
            }
            if(!"".equals(sp_outday)){
            	c1.setOutDay(sp_outday);
            }
            c1.setTheDay(date);
            c1.setOnDaySelectListener(Main_ac.this);
            ll.addView(c1);
        }
    }

	@Override
	public void onDaySelectListener(View view, String date) {
		//若日历日期小于当前日期，或日历日期-当前日期超过三个月，则不能点击
		try {
			if(simpleDateFormat.parse(date).getTime()<simpleDateFormat.parse(nowday).getTime()){
				return;
			 }
			long dayxc=(simpleDateFormat.parse(date).getTime()-simpleDateFormat.parse(nowday).getTime())/nd;
			if(dayxc>90){
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//若以前已经选择了日期，则在进入日历后会显示以选择的日期，该部分作用则是重新点击日历时，清空以前选择的数据（包括背景图案）

          
          String dateDay=date.split("-")[2];
          if(Integer.parseInt(dateDay)<10){
        	  dateDay=date.split("-")[2].replace("0", "");
          }
    	TextView textDayView=(TextView) view.findViewById(R.id.tv_calendar_day);
    	TextView textView=(TextView) view.findViewById(R.id.tv_calendar);
    	view.setBackgroundColor(Color.parseColor("#33B5E5"));
         textDayView.setTextColor(Color.WHITE);
         if(null==inday||inday.equals("")){
             textDayView.setText(dateDay);
             textView.setText("起始");
             inday=date;

         }else{
        	 if(inday.equals(date)){
        		 view.setBackgroundColor(Color.WHITE);
            	 textDayView.setText(dateDay);
            	 textDayView.setTextColor(Color.BLACK);
                 textView.setText("");
                 inday="";
        	 }else{
     			try {
					if(simpleDateFormat.parse(date).getTime()<simpleDateFormat.parse(inday).getTime()){
						view.setBackgroundColor(Color.WHITE);
						 textDayView.setTextColor(Color.BLACK);
						Toast.makeText(Main_ac.this, "起始日期不能小于结束日期", Toast.LENGTH_SHORT).show();
						return;
					 }
				} catch (ParseException e) {
					e.printStackTrace();
				}
            	 textDayView.setText(dateDay);
                 textView.setText("结束");
                 outday=date;
                 finish();
        	 }
         }
	}
	
	//根据当前日期，向后数三个月（若当前day不是1号，为满足至少90天，则需要向后数4个月）
	@SuppressLint("SimpleDateFormat")
	public List<String> getDateList(){
		List<String> list=new ArrayList<String>();
		Date date=new Date();
		int nowMon=date.getMonth()+1;
		String yyyy=sd1.format(date);
		String dd=sd2.format(date);
		if(nowMon==9){
			list.add(simpleDateFormat.format(date));
			list.add(yyyy+"-10-"+dd);
			list.add(yyyy+"-11-"+dd);
			if(!dd.equals("01")){
				list.add(yyyy+"-12-"+dd);
			}
		}else if(nowMon==10){
			list.add(yyyy+"-10-"+dd);
			list.add(yyyy+"-11-"+dd);
			list.add(yyyy+"-12-"+dd);
			if(!dd.equals("01")){
				list.add((Integer.parseInt(yyyy)+1)+"-01-"+dd);
			}
		}else if(nowMon==11){
			list.add(yyyy+"-11-"+dd);
			list.add(yyyy+"-12-"+dd);
			list.add((Integer.parseInt(yyyy)+1)+"-01-"+dd);
			if(!dd.equals("01")){
				list.add((Integer.parseInt(yyyy)+1)+"-02-"+dd);
			}
		}else if(nowMon==12){
			list.add(yyyy+"-12-"+dd);
			list.add((Integer.parseInt(yyyy)+1)+"-01-"+dd);
			list.add((Integer.parseInt(yyyy)+1)+"-02-"+dd);
			if(!dd.equals("01")){
				list.add((Integer.parseInt(yyyy)+1)+"-03-"+dd);
			}
		}else{
			list.add(yyyy+"-"+getMon(nowMon)+"-"+dd);
			list.add(yyyy+"-"+getMon((nowMon+1))+"-"+dd);
			list.add(yyyy+"-"+getMon((nowMon+2))+"-"+dd);
			if(!dd.equals("01")){
				list.add(yyyy+"-"+getMon((nowMon+3))+"-"+dd);
			}
		}
		return list;
	}
	
	public String getMon(int mon){
		String month="";
		if(mon<10){
			month="0"+mon;
		}else{
			month=""+mon;
		}
		return month;
	}

}
