package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.zhujia.dxracer_factory.Fragment.Batchorder;
import com.example.zhujia.dxracer_factory.Fragment.Productionholiday;
import com.example.zhujia.dxracer_factory.Fragment.Productionorderperson;
import com.example.zhujia.dxracer_factory.Fragment.Rowparameter;
import com.example.zhujia.dxracer_factory.Activity.Singlerow;
import com.example.zhujia.dxracer_factory.Fragment.Alldocuments;
import com.example.zhujia.dxracer_factory.Fragment.Schedulingsheet;
import com.example.zhujia.dxracer_factory.Fragment.myorder;

import java.util.List;

/**
 * Created by zhujia on 2017/11/21.
 */

public class SinglerowMG3 extends FragmentPagerAdapter {


    private FragmentManager fManager;
    public Singlerow context;
    private  String[] mTitles={"批量订单","排单计划"};
    private List<String> Mlist;
    private String roleId;
    private SharedPreferences sharedPreferences;
    private String qx;
    String [] stringArr;


    @SuppressLint("WrongConstant")
    public SinglerowMG3(FragmentManager fm, Singlerow ctx){
        super(fm);
        this.fManager=fm;
        this.context=ctx;
        sharedPreferences= ctx.getSharedPreferences("quanxian", Activity.MODE_PRIVATE);
        qx=sharedPreferences.getString("arr","");


    }



    @Override
    public Fragment getItem(int position) {


         if(position==1){
            return new Schedulingsheet();
        }
        return new Batchorder();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment=(Fragment)super.instantiateItem(container,position);
        this.fManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    public CharSequence getPageTitle(int position){
        return mTitles[position];
    }


}

