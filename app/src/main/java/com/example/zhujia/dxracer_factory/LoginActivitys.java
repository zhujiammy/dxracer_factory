package com.example.zhujia.dxracer_factory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Tools.CheckExitService;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;
import com.example.zhujia.dxracer_factory.Tools.PermissionsActivity;
import com.example.zhujia.dxracer_factory.Tools.PermissionsChecker;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DXSW5 on 2017/7/6.
 *
 *
 */

public class LoginActivitys extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_CODE = 0; // 请求码
    private EditText input_enterprisenumber,input_account,input_password;
    private Button _loginButton;
    private TextView _signupLink;
    private CheckBox remember;
    Map<String,String> params;
    Map<String,String> params1;
    JSONObject jsonObj;
    SharedPreferences sharedPreferences;
    String json;
    private String user_name,password,business_id;
    ProgressDialog progressDialog;
    private  SharedPreferences.Editor editor;


    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            // Manifest.permission.ACCESS_COARSE_LOCATION,
            //Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            //Manifest.permission.MODIFY_AUDIO_SETTINGS
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // LoadID();
        Intent intent=new Intent(this,CheckExitService.class);
        getApplicationContext().startService(intent);
        initUI();
        mPermissionsChecker = new PermissionsChecker(this);
        updataapk();
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    //检查更新
    private void updataapk(){
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Beta.largeIconId = R.mipmap.ic_launcher;
        Bugly.init(getApplicationContext(), "9e3fff37ad", false);
    }


    private void initUI(){
        input_account=(EditText)findViewById(R.id.input_account);
        input_password=(EditText)findViewById(R.id.input_password);
        input_enterprisenumber=(EditText)findViewById(R.id.input_enterprisenumber);
        _loginButton=(Button)findViewById(R.id.btn_login);
        remember=(CheckBox)findViewById(R.id.remember);
        //初始化用户名、密码，记住密码
        sharedPreferences=getSharedPreferences("userInfo",0);
        String name=sharedPreferences.getString("USER_NAME","");
        String pass=sharedPreferences.getString("PASSWORD","");
        String busi=sharedPreferences.getString("BUSI","");
        boolean choseRemember=sharedPreferences.getBoolean("remember",false);
        //如果上次记住了密码，那进入登陆页面自动勾选记住密码，并填上用户名密码


        if(choseRemember){
            input_account.setText(name);
            input_password.setText(pass);
            input_enterprisenumber.setText(busi);
            remember.setChecked(true);
        }
 
    }

    //登陆
    public void login() {
        Log.d(TAG, "Login");
        progressDialog = new ProgressDialog(LoginActivitys.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.Loggingin));
        progressDialog.show();
        business_id=input_enterprisenumber.getText().toString();
        user_name =input_account.getText().toString();
        password =input_password.getText().toString();
        editor =sharedPreferences.edit();
        String checkword="30e865d444ea47b58380b892e558484a";
        String data=business_id+user_name+password;

        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("username",user_name);
        params.put("password",password);
        params.put("ipAdress",getHostIP());
        Log.e(TAG, "login: "+params);
        // TODO: Implement your own authentication logic here.
        //final String params="token=" +token+ "&json=" +json;
        new HttpUtils().post(Constant.APPURLS+"/user/login",params,new HttpUtils.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");

                JSONObject reslutjsonobject;
                try{
                    SharedPreferences sp= getSharedPreferences("Session", Activity.MODE_PRIVATE);
                    reslutjsonobject=new JSONObject(data);
                    String result_code=reslutjsonobject.getString("result_code");
                    if(result_code.equals("success")){
                        JSONObject rows=reslutjsonobject.getJSONObject("rows");
                        //JSONObject user=rows.getJSONObject("user");
                        //JSONObject role=rows.getJSONObject("role");
                        //存储个人信息
                        SharedPreferences.Editor editor=sp.edit();
                        //系统用户
                        JSONObject systemUser=rows.getJSONObject("systemUser");
                        editor.putString("username",systemUser.getString("username"));
                        editor.putString("status",systemUser.getString("status"));
                        editor.putString("business_id",systemUser.getString("businessId"));
                        editor.putString("systemUser_id",systemUser.getString("id"));
                        editor.putString("password",systemUser.getString("password"));
                        editor.putString("personId",systemUser.getString("personId"));
                        if(!rows.isNull("systemUserRole")){
                            JSONObject systemUserRole=rows.getJSONObject("systemUserRole");
                            editor.putString("systemUserRole_id",systemUserRole.getString("id"));
                            editor.putString("roleId",systemUserRole.getString("roleId"));
                            editor.putString("userId",systemUserRole.getString("userId"));
                        }
                        if(!rows.isNull("departmentPerson")){
                            JSONObject personCode=rows.getJSONObject("departmentPerson");
                            editor.putString("personCode",personCode.getString("personCode"));
                            editor.putString("realName",personCode.getString("realName"));
                            editor.putString("departmentPersonSession",rows.getString("departmentPerson"));
                            editor.putString("departmentId",personCode.getString("departmentId"));
                            editor.putString("departmentPersonId",personCode.getString("id"));
                        }

                        editor.commit();
                        Message msg= Message.obtain(
                                mHandler,1,"ok"
                        );
                        mHandler.sendMessage(msg);

                    }else {
                        Message msg= Message.obtain(
                                mHandler,2,"ok"
                        );
                        mHandler.sendMessage(msg);
                    }

                    Log.d("code","result_code"+result_code);

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

        });


    }




    /**
     * 消息处理Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:// 解析返回数据
                    //toMainActivity();
                    break;
                case 1:
                    //保存密码
                    editor.putString("BUSI",business_id);
                    editor.putString("USER_NAME",user_name);
                    editor.putString("PASSWORD",password);
                    //是否记住密码
                    if(remember.isChecked())
                    {
                        editor.putBoolean("remember",true);
                    }else
                    {
                        editor.putBoolean("remember",false);
                    }
                    editor.commit();
                    //跳转
                    mHandler.postDelayed(runnable,2000);

                    break;

                case 2:
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    break;


                default:
                    Toast.makeText(LoginActivitys.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            progressDialog.dismiss();
            Intent intent=new Intent(LoginActivitys.this,MainActivity.class);
            startActivity(intent);
            finish();

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }else {
            updataapk();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED)
        {
            finish();
        }
    }

    /**
     * 获取ip地址
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }
}
