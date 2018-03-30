package com.example.zhujia.dxracer_factory.Tools.Net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpPostuploads extends Thread {
    private String requestUrl;
    private Map<String,String> params;
    private IHttpCallBack httpCallBack;
    private Handler handler;
    List<File> fileList=new ArrayList<>();
    Context context;
    List<String> newfilename=new ArrayList<>();
    //	URLBean urlBean = new URLBean();
    //通用的URL前缀
    SharedPreferences sp;

    public HttpPostuploads(Map<String, String> params, List<File> fileList,List<String> newfilename, String urlStr, IHttpCallBack callback, Handler hd, Context ctx) {
        this.requestUrl = Constant.APPURLS+urlStr;
        this.httpCallBack = callback;
        this.handler = hd;
        this.context = ctx;
        this.fileList=fileList;
        this.newfilename=newfilename;
        sp = ctx.getSharedPreferences("Session", Activity.MODE_PRIVATE);
        //在这里拼接公共参数
        this.params = params;
    }

    public void run() {
        try {

            // 调用Post方式请求


            String result = HttpUtility.uploads(params,fileList,newfilename,requestUrl);

            Log.d(Constant.LOGTAG, "result : " + result);

            if (result != null && !result.equals("")) {
                if (httpCallBack != null) {
                    // 回调函数设置值
                    String errcode = null;
                /*
				 * 返回错误时 获取错误码
				 */

                    httpCallBack.onRequestComplete(result, handler, errcode);

                }

            } else {
                // CommonUtility.showThreadToast(context, "网络异常，请检查网络设置");
            }



        } catch (Exception e) {
            // 异常捕获
            e.printStackTrace();
        }
    }

    ;
}
