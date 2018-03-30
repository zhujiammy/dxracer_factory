package com.example.zhujia.dxracer_factory.Tools.Net;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.Map;

public class HttpPostupload extends Thread {
    private String requestUrl;
    private Map<String,String> params;
    private IHttpCallBack httpCallBack;
    private Handler handler;
    private File file;
    String fileFormName;
    String newFileName;
    Context context;
    //	URLBean urlBean = new URLBean();
    //通用的URL前缀
    SharedPreferences sp;

    public HttpPostupload(Map<String, String> params, String fileFormName, File uploadFile, String newFileName, String urlStr, IHttpCallBack callback, Handler hd, Context ctx) {
        this.requestUrl = Constant.APPURLS+urlStr;
        this.httpCallBack = callback;
        this.fileFormName=fileFormName;
        this.newFileName=newFileName;
        this.handler = hd;
        this.context = ctx;
        this.file=uploadFile;
        sp = ctx.getSharedPreferences("Session", Activity.MODE_PRIVATE);
        //在这里拼接公共参数
        this.params = params;
    }

    public void run() {
        try {

            // 调用Post方式请求


            String result = HttpUtility.upload(params,fileFormName,file,newFileName,requestUrl);
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
