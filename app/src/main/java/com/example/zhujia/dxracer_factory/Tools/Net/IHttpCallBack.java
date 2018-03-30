package com.example.zhujia.dxracer_factory.Tools.Net;

import android.os.Handler;

/**
 * 异步Http请求回调接口
 * 
 * @author chenlongtao
 */
public interface  IHttpCallBack{
	// 请求完成回调方法
	public void onRequestComplete(String result, Handler handler, String errcode);
}
