package com.example.zhujia.dxracer_factory.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zhujia.dxracer_factory.Data.Dict;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhujia on 2017/12/18.
 */

public class ListDataSave {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public ListDataSave(Context mContext, String preferenceName) {
        preferences = mContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public <Dict> void setDataList(String tag, List<com.example.zhujia.dxracer_factory.Data.Dict> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public <Dict> List<com.example.zhujia.dxracer_factory.Data.Dict> getDataList(String tag) {
        List<com.example.zhujia.dxracer_factory.Data.Dict> datalist=new ArrayList<com.example.zhujia.dxracer_factory.Data.Dict>();
        String strJson = preferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<com.example.zhujia.dxracer_factory.Data.Dict>>() {
        }.getType());
        return datalist;

    }
}
