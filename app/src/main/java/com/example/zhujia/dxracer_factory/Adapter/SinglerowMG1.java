package com.example.zhujia.dxracer_factory.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.zhujia.dxracer_factory.Fragment.Productionholiday;
import com.example.zhujia.dxracer_factory.Fragment.Productionorderperson;
import com.example.zhujia.dxracer_factory.Fragment.Rowparameter;
import com.example.zhujia.dxracer_factory.Activity.Singlerow;
import com.example.zhujia.dxracer_factory.Fragment.Alldocuments;
import com.example.zhujia.dxracer_factory.Fragment.Schedulingsheet;
import com.example.zhujia.dxracer_factory.Fragment.myorder;
import com.example.zhujia.dxracer_factory.Fragment.productionorder;

import java.util.List;

/**
 * Created by zhujia on 2017/11/21.
 */

public class SinglerowMG1 extends FragmentPagerAdapter {


    private FragmentManager fManager;
    public Singlerow context;
    private  String[] mTitles={"排单计划","待生产单","待完成单","所有单据"};
    private List<String> Mlist;
    private String roleId;
    private SharedPreferences sharedPreferences;
    private String qx;
    String [] stringArr;


    @SuppressLint("WrongConstant")
    public SinglerowMG1(FragmentManager fm, Singlerow ctx){
        super(fm);
        this.fManager=fm;
        this.context=ctx;
        sharedPreferences= ctx.getSharedPreferences("quanxian", Activity.MODE_PRIVATE);
        qx=sharedPreferences.getString("arr","");
       /* if(qx.contains(",")){
            System.out.println("包含");
            stringArr= qx.replace(" ", "") .split(",");

            mTitles=new String[stringArr.length];
            for(int i=0;i<stringArr.length;i++){
                mTitles[i]=stringArr[i];
                Log.e("TAG", "SinglerowMG: "+stringArr[i]);
            }
        }else{ System.out.println("不包含");
            String[] array = {qx};
            for (String string : array){
                System.out.println(array.length);
            }
            mTitles=new String[array.length];
            for(int i=0;i<array.length;i++){
                mTitles[i]=array[i];
                Log.e("TAG", "SinglerowMG: "+array[i] );
            }

        }*/

    }



    @Override
    public Fragment getItem(int position) {


        if(position==1){
            return new productionorder();
        }else if(position==2){
            return new myorder();
        }else if(position==3){
            return new Alldocuments();
        }
        return new Schedulingsheet();
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

