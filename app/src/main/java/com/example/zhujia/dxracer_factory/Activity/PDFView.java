package com.example.zhujia.dxracer_factory.Activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.zhujia.dxracer_factory.R;
import com.lidong.pdf.listener.OnDrawListener;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.zhujia.dxracer_factory.Tools.Net.Constant.APPURLIMG;


/**
 * Created by ZHUJIA on 2018/2/2.
 */

public class PDFView extends AppCompatActivity implements OnPageChangeListener
        ,OnLoadCompleteListener, OnDrawListener {

    private com.lidong.pdf.PDFView pdfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfview);
        Intent intent=getIntent();
        pdfView=(com.lidong.pdf.PDFView)findViewById(R.id.pdfView);
        pdfView.enableSwipe(true);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
       String filename = format.format(date);
        displayFromFile1(APPURLIMG+intent.getStringExtra("url"), filename+".pdf");
        Log.e("TAG", "onClick: "+intent.getStringExtra("url"));

    }

    /**
     * 获取打开网络的pdf文件
     * @param fileUrl
     * @param fileName
     */
    private void displayFromFile1( String fileUrl ,String fileName) {
        pdfView.recycle();
     pdfView.fileFromLocalStorage(this,this,this,fileUrl,fileName);   //设置pdf文件地址
    pdfView.setSwipeVertical(true);
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}
