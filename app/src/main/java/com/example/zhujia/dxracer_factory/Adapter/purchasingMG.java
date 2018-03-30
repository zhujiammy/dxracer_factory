package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.zhujia.dxracer_factory.Activity.purchasing;
import com.example.zhujia.dxracer_factory.Fragment.Productionholiday;
import com.example.zhujia.dxracer_factory.Fragment.Productionorderperson;
import com.example.zhujia.dxracer_factory.Fragment.Rowparameter;
import com.example.zhujia.dxracer_factory.Activity.Singlerow;
import com.example.zhujia.dxracer_factory.Fragment.Alldocuments;
import com.example.zhujia.dxracer_factory.Fragment.Schedulingsheet;
import com.example.zhujia.dxracer_factory.Fragment.myorder;
import com.example.zhujia.dxracer_factory.Fragment.productionorder;
import com.example.zhujia.dxracer_factory.Fragment.purchaseorder;
import com.example.zhujia.dxracer_factory.Fragment.purchaseorderPIA;
import com.example.zhujia.dxracer_factory.Fragment.purchaseorderPIC;
import com.example.zhujia.dxracer_factory.Fragment.purchaseorderPID;

import java.util.List;

/**
 * Created by zhujia on 2017/11/21.
 */

public class purchasingMG extends FragmentPagerAdapter {


    private FragmentManager fManager;
    public purchasing context;
    private  String[] mTitles={"全部采购单","批量采购单","现货采购单","售后采购单"};
    @SuppressLint("WrongConstant")
    public purchasingMG(FragmentManager fm, purchasing ctx){
        super(fm);
        this.fManager=fm;
        this.context=ctx;
    }



    @Override
    public Fragment getItem(int position) {


        if(position==1){
            return new purchaseorderPIA();
        }else if(position==2){
            return new purchaseorderPIC();
        }else if(position==3){
            return new purchaseorderPID();
        }
        return new purchaseorder();
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

