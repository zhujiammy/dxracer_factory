package com.example.zhujia.dxracer_factory.Tools.Net;

import android.os.Handler;

public class HttpGetThread extends Thread {
	private String requestUrl;
	private IHttpCallBack httpCallBack;
	private Handler handler;

	public HttpGetThread(String requestUrl, IHttpCallBack httpCallBack, Handler handler) {
		this.requestUrl = requestUrl;
		this.httpCallBack = httpCallBack;
		this.handler = handler;
	}

	public void run() {
		try {
			// 调用Get方式请求
			String result = HttpUtility.doGet(requestUrl);
			if (httpCallBack != null) {
				// 回调函数设置值
				String errcode = null;

				httpCallBack.onRequestComplete(result, handler,errcode);
			}
		} catch (Exception e) {
			// 异常捕获
			e.printStackTrace();
		}
	};
}
