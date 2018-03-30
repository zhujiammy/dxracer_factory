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
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhujia.dxracer_factory.Data.AllData;
import com.example.zhujia.dxracer_factory.R;
import com.example.zhujia.dxracer_factory.Tools.ImageService;
import com.example.zhujia.dxracer_factory.Tools.Net.Constant;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtility;
import com.example.zhujia.dxracer_factory.Tools.Net.HttpUtils;
import com.example.zhujia.dxracer_factory.Tools.Net.IHttpCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by ZHUJIA on 2018/1/11.
 *
 * 新增设备
 */

public class AddEquipment extends AppCompatActivity implements View.OnClickListener {

    private TextView text1,equipmentUseTime;
    private Toolbar toolbar;
    String filename,filename1;
    private EditText field1,equipmentNo,equipmentName,equipmentColor,field2;
    private Spinner equipmentCategory,equipmentManager,equipmentSupplier,equipmentStatus;
    private ImageView filePath,filePath1;
    List<AllData> dicts1 = new ArrayList<AllData>();
    List<AllData> dicts2= new ArrayList<AllData>();
    List<AllData> dicts3= new ArrayList<AllData>();
    List<AllData> dicts4= new ArrayList<AllData>();
    private String categoryId;
    private java.util.Calendar cal;
    private int year,month,day;
    private SharedPreferences sharedPreferences;
    private String business_id,departmentId,sta,userId;
    Map<String,String> params;
    private Intent intent;
    JSONObject reslutJSONObject;
    private String url,equipmentCategoryId,equipmentManagerid,equipmentSupplierId,equipmentStatusid;
    private int istouch;
    ArrayAdapter<AllData> arrAdapterpay4,arrAdapterpay1,arrAdapterpay2,arrAdapterpay3;
    File outputImage;
    public Uri imageUri;
    public static final int HANDLE_MSG_LOAD_IMAGE = 10;
    public static  final int HANDLE_MSG_LOAD_IMAGE1=11;
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int CHOOSE_PHOTO = 3;
    private Bitmap photo,photo1;
    private File certificateurl,certificateurl1;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addequipment);
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
        sharedPreferences =getSharedPreferences("Session", Context.MODE_APPEND);
        business_id=sharedPreferences.getString("business_id","");
        departmentId=sharedPreferences.getString("departmentId","");
        userId=sharedPreferences.getString("userId","");
        intent=getIntent();
        initUI();
        loadcategory();
        getDate();//获取当前日期
    }

    @SuppressLint("WrongConstant")
    private void getDate(){
        cal= java.util.Calendar.getInstance();
        year=cal.get(java.util.Calendar.YEAR);
        month=cal.get(java.util.Calendar.MONTH);
        day=cal.get(java.util.Calendar.DAY_OF_MONTH);

    }

    private void initUI(){
        text1=(TextView)findViewById(R.id.text1);
        equipmentUseTime=(TextView)findViewById(R.id.equipmentUseTime);
        equipmentUseTime.setOnClickListener(this);
        if(intent.getStringExtra("type")==null){
            text1.setText("修改");
        }else {
            text1.setText("新增设备");
        }

        field1=(EditText)findViewById(R.id.field1);
        equipmentNo=(EditText)findViewById(R.id.equipmentNo);
        equipmentName=(EditText)findViewById(R.id.equipmentName);
        equipmentColor=(EditText)findViewById(R.id.equipmentColor);
        field2=(EditText)findViewById(R.id.field2);
        equipmentCategory=(Spinner) findViewById(R.id.equipmentCategory);
        equipmentManager=(Spinner) findViewById(R.id.equipmentManager);
        equipmentSupplier=(Spinner) findViewById(R.id.equipmentSupplier);
        equipmentStatus=(Spinner) findViewById(R.id.equipmentStatus);


        equipmentCategory.setOnItemSelectedListener(listener1);
        equipmentManager.setOnItemSelectedListener(listener4);
        equipmentSupplier.setOnItemSelectedListener(listener2);
        equipmentStatus.setOnItemSelectedListener(listener3);

        filePath=(ImageView)findViewById(R.id.filePath);
        filePath1=(ImageView)findViewById(R.id.filePath1);
        filePath.setOnClickListener(this);
        filePath1.setOnClickListener(this);
        dicts4.add(new AllData("0","请选择"));
        dicts4.add(new AllData("A","正常"));
        dicts4.add(new AllData("B","停用"));
        ArrayAdapter<AllData> arrAdapterpay4 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts4);
        //设置样式
        arrAdapterpay4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equipmentStatus.setAdapter(arrAdapterpay4);



        if(intent.getStringExtra("type")==null){
            url="/servlet/equipment/equipment/update";

            int j=arrAdapterpay4.getCount();
            for(int i=0;i<j;i++){
                if(intent.getStringExtra("EquipmentStatus").equals(arrAdapterpay4.getItem(i).getStr())){
                    equipmentStatus.setAdapter(arrAdapterpay4);
                    equipmentStatus.setSelection(i,true);
                }
            }

            equipmentNo.setText(intent.getStringExtra("EquipmentNo"));
            equipmentName.setText(intent.getStringExtra("EquipmentName"));
            equipmentColor.setText(intent.getStringExtra("EquipmentColor"));
            field2.setText(intent.getStringExtra("Field2"));
            field1.setText(intent.getStringExtra("Field1"));
            equipmentUseTime.setText(intent.getStringExtra("EquipmentUseTime"));


        }else {
            url="/servlet/equipment/equipment/add";
        }

    }


    private void loadcategory(){
        params=new HashMap<>();
        params.put("businessId",business_id);
        params.put("type","all");
        new HttpUtils().post(Constant.APPURLS+"/servlet/equipment/equipment",params,new HttpUtils.HttpCallback() {

            @Override
            public void onSuccess(String data) {
                // TODO Auto-generated method stub
                com.example.zhujia.dxracer_factory.Tools.Log.printJson("tag",data,"header");

                Message msg=Message.obtain(
                        mHandler,1,data
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

            if(equipmentCategoryId==null||equipmentCategoryId.equals("0")){
                Toast.makeText(getApplicationContext(),"设备类别不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(field1.getText().toString())){
                Toast.makeText(getApplicationContext(),"设备规格不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(equipmentNo.getText().toString())){
                Toast.makeText(getApplicationContext(),"设备编号不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(equipmentName.getText().toString())){
                Toast.makeText(getApplicationContext(),"设备名称不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(equipmentColor.getText().toString())){
                Toast.makeText(getApplicationContext(),"设备颜色不能为空",Toast.LENGTH_SHORT).show();
            }else if(equipmentManagerid==null||equipmentManagerid.equals("0")){
                Toast.makeText(getApplicationContext(),"设备管理员不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(field2.getText().toString())){
                Toast.makeText(getApplicationContext(),"设备使用人不能为空",Toast.LENGTH_SHORT).show();
            }else if(equipmentSupplierId==null||equipmentSupplierId.equals("0")){
                Toast.makeText(getApplicationContext(),"设备维修商不能为空",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(equipmentUseTime.getText().toString())){
                Toast.makeText(getApplicationContext(),"开始使用时间不能为空",Toast.LENGTH_SHORT).show();
            }else if(equipmentStatusid==null||equipmentStatusid.equals("0")){
                Toast.makeText(getApplicationContext(),"设备状态不能为空",Toast.LENGTH_SHORT).show();
            }else if(photo == null) {
                Toast.makeText(getApplicationContext(),"设备图片不能为空",Toast.LENGTH_SHORT).show();
            }else if(photo1 == null){
                Toast.makeText(getApplicationContext(),"设备标示图不能为空",Toast.LENGTH_SHORT).show();
            }
            else {
                params=new HashMap<>();
                params.put("businessId",business_id);
                params.put("userId",userId);
                params.put("equipmentCategoryId",equipmentCategoryId);
                params.put("field1",field1.getText().toString());
                params.put("equipmentNo", equipmentNo.getText().toString());
                params.put("equipmentName",equipmentName.getText().toString());
                params.put("equipmentColor",equipmentColor.getText().toString());
                params.put("equipmentManager",equipmentManagerid);
                params.put("field2",field2.getText().toString());
                params.put("equipmentSupplierId",equipmentSupplierId);
                params.put("equipmentUseTime",equipmentUseTime.getText().toString());
                params.put("equipmentStatus",equipmentStatusid);
                if(intent.getStringExtra("type")==null){
                    params.put("id",intent.getStringExtra("id"));
                }
                certificateurl = ImageService.compressImage1(photo,filename);
                certificateurl1= ImageService.compressImage1(photo1,filename1);
                List<File> fileList=new ArrayList<>();
                List<String> newfilename=new ArrayList<>();
                fileList.add(certificateurl);
                fileList.add(certificateurl1);
                newfilename.add(filename+".png");
                newfilename.add(filename1+".png");
                Log.e("TAG", "onOptionsItemSelected: "+certificateurl.getAbsolutePath());
                Log.e("TAG", "onOptionsItemSelected: "+certificateurl1.getAbsolutePath());
                   HttpUtility.doPostAsynupload1(params,fileList,newfilename, url, new IHttpCallBack() {
                       @Override
                       public void onRequestComplete(String result, Handler handler, String errcode) {
                           if ((null == result) || (result.equals(""))) {
                               // 网络连接异常
                               mHandler.sendEmptyMessage(9);

                           } else {

                               Message msg = Message.obtain(
                                       handler, 0, result
                               );
                               mHandler.sendMessage(msg);

                           }
                       }
                   }, mHandler, getApplicationContext());


            }



        }

        return super.onOptionsItemSelected(item);
    }





    Spinner.OnItemSelectedListener listener1=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentCategoryId=((AllData)equipmentCategory.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    Spinner.OnItemSelectedListener listener2=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentSupplierId=((AllData)equipmentSupplier.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    Spinner.OnItemSelectedListener listener3=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentStatusid=((AllData)equipmentStatus.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    Spinner.OnItemSelectedListener listener4=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            equipmentManagerid=((AllData)equipmentManager.getSelectedItem()).getStr();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what){
                    case 0:

                        if(msg.obj.toString().equals("0")){
                            Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"保存失败",Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1:
                        reslutJSONObject=new JSONObject(msg.obj.toString());
                        JSONArray equipmentCategoryList=reslutJSONObject.getJSONArray("equipmentCategoryList");
                        dicts1.add(new AllData("0","请选择"));
                        for(int i=0;i<equipmentCategoryList.length();i++){
                            JSONObject object1=equipmentCategoryList.getJSONObject(i);
                            dicts1.add(new AllData(object1.getString("id"),object1.getString("name")));
                           arrAdapterpay1 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts1);
                            //设置样式
                            arrAdapterpay1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            equipmentCategory.setAdapter(arrAdapterpay1);
                        }



                        dicts2.add(new AllData("0","请选择"));
                        JSONArray equipmentSupplierList=reslutJSONObject.getJSONArray("equipmentSupplierList");
                        for(int i=0;i<equipmentSupplierList.length();i++){
                            JSONObject object2=equipmentSupplierList.getJSONObject(i);
                            dicts2.add(new AllData(object2.getString("id"),object2.getString("supplierName")));
                            arrAdapterpay2 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts2);
                            //设置样式
                            arrAdapterpay2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            equipmentSupplier.setAdapter(arrAdapterpay2);
                        }
                        dicts3.add(new AllData("0","请选择"));
                        JSONArray departmentPersonList=reslutJSONObject.getJSONArray("departmentPersonList");
                        for(int i=0;i<departmentPersonList.length();i++){
                            JSONObject object2=departmentPersonList.getJSONObject(i);
                            dicts3.add(new AllData(object2.getString("id"),object2.getString("realName")));
                            arrAdapterpay3 = new ArrayAdapter<AllData>(getApplicationContext(), R.layout.simple_spinner_item,dicts3);
                            //设置样式
                            arrAdapterpay3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            equipmentManager.setAdapter(arrAdapterpay3);
                        }

                        if(intent.getStringExtra("type")==null){
                            if(arrAdapterpay1.getCount()!=0){
                                int i=arrAdapterpay1.getCount();
                                for(int j=0;j<i;j++){
                                    if(intent.getStringExtra("CategoryName").equals(arrAdapterpay1.getItem(j).getText())){
                                        equipmentCategory.setAdapter(arrAdapterpay1);
                                        equipmentCategory.setSelection(j,true);
                                    }
                                }
                            }

                            int c=arrAdapterpay2.getCount();
                            for(int j=0;j<c;j++){
                                if(intent.getStringExtra("SupplierName").equals(arrAdapterpay2.getItem(j).getText())){
                                    equipmentSupplier.setAdapter(arrAdapterpay2);
                                    equipmentSupplier.setSelection(j,true);
                                }
                            }

                            int d=arrAdapterpay3.getCount();
                            for(int j=0;j<d;j++){
                                if(intent.getStringExtra("RealName").equals(arrAdapterpay3.getItem(j).getText())){
                                    equipmentManager.setAdapter(arrAdapterpay3);
                                    equipmentManager.setSelection(j,true);
                                }
                            }
                        }
                        break;

                    case HANDLE_MSG_LOAD_IMAGE:
                            filePath.setImageBitmap(photo);
                        break;
                    case  HANDLE_MSG_LOAD_IMAGE1:
                        filePath1.setImageBitmap(photo1);
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
            istouch=1;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            filename = format.format(date);
            showPopwindows();
        }

        if(v==filePath1){
            //上传图片
            istouch=2;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            filename1 = format.format(date);
            showPopwindows();
        }
        if(v==equipmentUseTime){
            //选择日期
            DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String date=(i+"-"+(++i1)+"-"+i2);
                    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = null;
                    try {
                        date1 = format1.parse(date);
                        String startime = format1.format(date1);
                        equipmentUseTime.setText(startime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            DatePickerDialog dialog=new DatePickerDialog(AddEquipment.this,DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,listener,year,month,day);
            dialog.show();
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
                        int permissionCheck = ContextCompat.checkSelfPermission(AddEquipment.this,
                                Manifest.permission.CAMERA);
                        //存储权限
                        int permissionCheck_storage = ContextCompat.checkSelfPermission(AddEquipment.this,
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
                                new AlertDialog.Builder(AddEquipment.this)
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
                            new AlertDialog.Builder(AddEquipment.this)
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
                            new AlertDialog.Builder(AddEquipment.this)
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


                        if(istouch==1){
                            photo=null;
                            photo= ImageService.loadImgFromLocal(AddEquipment.this,data.getData());
                            if(photo!=null){
                                Message msg = Message.obtain(
                                        mHandler, HANDLE_MSG_LOAD_IMAGE, photo
                                );
                                mHandler.sendMessage(msg);
                            }
                        }else {
                            photo1=null;
                            photo1= ImageService.loadImgFromLocal1(AddEquipment.this,data.getData());
                            if(photo1!=null){
                                Message msg = Message.obtain(
                                        mHandler, HANDLE_MSG_LOAD_IMAGE1, photo1
                                );
                                mHandler.sendMessage(msg);
                            }
                        }



                    }
                    break;
                case CROP_PHOTO:
                    if (resultCode == RESULT_OK) {


                        if(istouch==1){
                            photo=null;
                            photo= ImageService.loadImgFromLocal(outputImage.getAbsolutePath());
                            if(photo!=null){
                                Message msg = Message.obtain(
                                        mHandler, HANDLE_MSG_LOAD_IMAGE, photo
                                );
                                mHandler.sendMessage(msg);
                            }

                        }else {
                            photo1=null;
                            photo1= ImageService.loadImgFromLocal(outputImage.getAbsolutePath());
                            if(photo1!=null){
                                Message msg = Message.obtain(
                                        mHandler, HANDLE_MSG_LOAD_IMAGE1, photo1
                                );
                                mHandler.sendMessage(msg);
                            }

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
