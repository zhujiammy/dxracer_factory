package com.example.zhujia.dxracer_factory.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.ImageService;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;

import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZHUJIA on 2018/1/24.
 *
 * 申请应付款
 */

public class ApplyPayments extends AppCompatActivity implements View.OnClickListener{

    private TextView MaintainNo;
    private LinearLayout view_group;
    private EditText field2,field3;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentPersonId,userId;
    Map<String,String> params;
    private JSONObject reslutJSONObject;
    private Toolbar toolbar;
    private JSONArray rows;
    private ImageView filePath;
    File outputImage;
    public Uri imageUri;
    public static final int HANDLE_MSG_LOAD_IMAGE = 10;
    public static  final int HANDLE_MSG_LOAD_IMAGE1=11;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int CHOOSE_PHOTO = 3;
    private Bitmap photo;
    private File FilePath;
    private  String filename;
    View view;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applypayment);
        sharedPreferences =getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentPersonId=sharedPreferences.getString("departmentPersonId","");
        userId=sharedPreferences.getString("userId","");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent=getIntent();
        initUI();
        loadlist();

    }

    private void initUI(){
        MaintainNo=(TextView)findViewById(R.id.field2);
        MaintainNo.setText(intent.getStringExtra("MaintainNo"));
        view_group=(LinearLayout)findViewById(R.id.view_group);
        field3=(EditText)findViewById(R.id.field5);
        field2=(EditText)findViewById(R.id.field4);
        field2.setText(intent.getStringExtra("field2"));
        field3.setText(intent.getStringExtra("field3"));
        filePath=(ImageView)findViewById(R.id.filePath);
        filePath.setOnClickListener(this);
    }


    private void loadlist(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("maintainId",intent.getStringExtra("id"));
        params.put("sorttype","asc");
        params.put("sort","undefined");
        params.put("page", "1");
        Log.e("TAG", "loadcategory: "+params );
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipmentmaintaindetail/list",params,new HttpUtils.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                Message msg=Message.obtain(
                        mHandler,2,data
                );
                mHandler.sendMessage(msg);
            }


        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.save_btn){
            //保存

            if(TextUtils.isEmpty(field3.getText().toString())){
                Toast.makeText(getApplicationContext(),"发票代码不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(field2.getText().toString())){
                Toast.makeText(getApplicationContext(),"发票号码不能为空",Toast.LENGTH_SHORT).show();
            }else if(photo == null) {
                Toast.makeText(getApplicationContext(),"发票不能为空",Toast.LENGTH_SHORT).show();
            }else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("field2",field2.getText().toString());
                params.put("field3",field3.getText().toString());
                params.put("id",intent.getStringExtra("id"));
                FilePath = ImageService.compressImage1(photo,filename);
                Log.e("TAG", "onOptionsItemSelected: "+params );
                new HttpUtils().postUpload(Constant.APPURLS + "/servlet/equipment/equipmentmaintain/getInvoice",params, FilePath, filename + ".png", new HttpUtils.HttpCallback() {
                    @Override
                    public void onSuccess(String data) {
                        com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");
                        Message msg=Message.obtain(
                                mHandler,4,data
                        );
                        mHandler.sendMessage(msg);
                    }
                });
            }

        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){

                    case 2:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        if(!reslutJSONObject.isNull("rows")){
                            rows=reslutJSONObject.getJSONArray("rows");
                            view_group.removeAllViews();
                            for(int i=0;i<rows.length();i++){
                                view=View.inflate(getApplicationContext(),R.layout.layout_adds,null);
                                TextView name=(TextView)view.findViewById(R.id.name);
                                TextView cost=(TextView)view.findViewById(R.id.cost);
                                ImageView del=(ImageView)view.findViewById(R.id.del);
                                del.setVisibility(View.INVISIBLE);
                                final JSONObject object2=rows.getJSONObject(i);
                                cost.setText(object2.getString("cost"));
                                JSONObject equipmentDetail=object2.getJSONObject("equipmentDetail");
                                name.setText(equipmentDetail.getString("name"));
                                view_group.addView(view);

                            }
                        }else {
                            view_group.removeAllViews();
                        }


                        break;
                    case 3:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        if(reslutJSONObject.getString("code").equals("success")){
                            loadlist();
                            Toast.makeText(getApplicationContext(),"删除成功",Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(),"删除失败",Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 4:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        if(reslutJSONObject.getString("code").equals("success")){
                            Toast.makeText(getApplicationContext(),reslutJSONObject.getString("message"),Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),reslutJSONObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case HANDLE_MSG_LOAD_IMAGE:
                        filePath.setImageBitmap(photo);
                        break;

                }
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    };


    @Override
    public void onClick(View v) {

        if(v==filePath){
            //上传图片
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            filename = format.format(date);
            showPopwindows();
        }

    }

    //打开相机
    private void showPopwindows(){
        View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        View popView = View.inflate(this, R.layout.camera_pop_menu, null);

        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        Button btnCancel = (Button) popView.findViewById(R.id.btn_camera_pop_cancel);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:

                        //创建file对象，用于存储拍照后的图片
                        outputImage = new File(Environment.getExternalStorageDirectory(), "output_Image.jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }

                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int permissionCheck = ContextCompat.checkSelfPermission(ApplyPayments.this,
                                Manifest.permission.CAMERA);
                        //存储权限
                        int permissionCheck_storage = ContextCompat.checkSelfPermission(ApplyPayments.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                            if (permissionCheck_storage == PackageManager.PERMISSION_GRANTED) {
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    imageUri = FileProvider.getUriForFile(getApplicationContext(),"com.example.zhujia.dxracer_factory.fileProvider",outputImage);
                                }else{
                                    imageUri = Uri.fromFile(outputImage);
                                }
                                intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                                //启动相机程序
                                startActivityForResult(intent, TAKE_PHOTO);
                            } else {
                                // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面
                                new AlertDialog.Builder(ApplyPayments.this)
                                        .setMessage("app需要读取存储权限")
                                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                intent.setData(Uri.parse("package:" + getPackageName()));
                                                startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("取消", null)
                                        .create()
                                        .show();
                            }

                        } else {

                            // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面
                            new AlertDialog.Builder(ApplyPayments.this)
                                    .setMessage("app需要开启相机权限")
                                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.parse("package:" + getPackageName()));
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create()
                                    .show();

                        }


                        break;

                    case R.id.btn_camera_pop_album:
                        //存储权限
                        int permissionCheck_storage_xc = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permissionCheck_storage_xc == PackageManager.PERMISSION_GRANTED) {


//                            intent = new Intent("android.intent.action.GET_CONTENT");
//							String Action = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT;
                            String Action = Intent.ACTION_GET_CONTENT;
                            intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Action);


//                             打开相册*/
                            startActivityForResult(intent, CHOOSE_PHOTO);

                        } else {
                            // 没有权限，跳到设置界面，调用Android系统“应用程序信息（Application Info）”界面
                            new AlertDialog.Builder(ApplyPayments.this)
                                    .setMessage("app需要读取存储权限")
                                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.parse("package:" + getPackageName()));
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .create()
                                    .show();
                        }

                        break;
                    case R.id.btn_camera_pop_cancel:

                        break;

                }
                popWindow.dismiss();
            }
        };

        btnCamera.setOnClickListener(listener);
        btnAlbum.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {
                        this.startPhotoZoom(imageUri);
                    }
                    break;
                case CHOOSE_PHOTO:
                    if (resultCode == RESULT_OK) {


                        photo=null;
                        photo= ImageService.loadImgFromLocal(ApplyPayments.this,data.getData());
                        if(photo!=null){
                            Message msg = Message.obtain(
                                    mHandler, HANDLE_MSG_LOAD_IMAGE, photo
                            );
                            mHandler.sendMessage(msg);
                        }
                    }
                    break;
                case CROP_PHOTO:
                    if (resultCode == RESULT_OK) {

                        photo=null;
                        photo= ImageService.loadImgFromLocal(outputImage.getAbsolutePath());
                        if(photo!=null){
                            Message msg = Message.obtain(
                                    mHandler, HANDLE_MSG_LOAD_IMAGE, photo
                            );
                            mHandler.sendMessage(msg);
                        }




                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void startPhotoZoom(Uri uri) {
        //imageUri=FileProvider.getUriForFile(getApplicationContext(), "com.uroad.cargo.alpha.fileprovider", new File(ImageService.getImageAbsolutePath(getApplicationContext(), uri)));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // aspectX aspectY 是宽高的比例
        //intent.putExtra("aspectX", 1);
        //intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        //intent.putExtra("outputX", 800);
        //intent.putExtra("outputY", 600);
        // 启动裁剪程序
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_PHOTO);
    }
}
