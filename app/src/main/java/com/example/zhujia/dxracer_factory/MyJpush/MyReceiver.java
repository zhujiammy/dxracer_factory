package com.example.zhujia.dxracer_factory.MyJpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.example.zhujia.dxracer_factory.MainActivity;
import com.example.zhujia.dxracer_factory.Tools.Net.JPushUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */

public class MyReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent)
    {
		Bundle bundle = intent.getExtras();
		//一有消息过来就hasNews = true，显示所有的消息闪动
		//DXApp.getInstance().hasNews = true;
		// 推送通知的接收
		if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
		{
			processCustomMessage(context, bundle);
		}
		// 推送消息的接收
		else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			// 获取消息的内容
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			Log.e("TAG,", "onReceive: "+message );
		}
		// 推送通知被打开
		else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			String sExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Log.i("1111111111111111", sExtra);
//			try {

				if (!sExtra.equals("") && sExtra != null)
				{
					//Intent i=new Intent(context,VoucherCenter.class);
					//context.startActivity(i);
				    /*i = new Intent(context, Message_List.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					context.startActivity(i);*/

				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
		}
	}// 发送消息到MainActivity
	private void processCustomMessage(Context context, Bundle bundle)
    {
		if (MainActivity.isForeground)
		{
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!JPushUtil.isEmpty(extras))
			{
				try
                {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0)
					{
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				}
				catch (JSONException e)
                {

                }
			}
			context.sendBroadcast(msgIntent);
		}
	}

}