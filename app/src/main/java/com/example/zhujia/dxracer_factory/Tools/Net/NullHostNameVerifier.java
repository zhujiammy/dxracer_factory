package com.example.zhujia.dxracer_factory.Tools.Net;

import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		// TODO Auto-generated method stub
		Log.i("RestUtilImpl","Approving certificate for"+hostname);
		
		return true;
	}

}