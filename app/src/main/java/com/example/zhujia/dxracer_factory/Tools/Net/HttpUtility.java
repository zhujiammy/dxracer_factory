package com.example.zhujia.dxracer_factory.Tools.Net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Loader;
import android.os.Handler;
import android.util.Log;
import android.widget.Spinner;

import com.example.zhujia.dxracer_factory.Tools.DXApp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Response;

import static java.lang.String.valueOf;

/**
 * <p>
 * Http工具类,包括异步请求的方式
 * </p>
 *
 * @author chenlongtao
 */
@SuppressLint("DefaultLocale")
public class HttpUtility {
    // 超时默认时长，单位：毫秒
    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";
    private static final int mTimeOut = 15000;
    private static SSLSocketFactory socketFactory;
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";


    /**
     * <p>
     * 异步Http请求，以Get方式请求
     * </p>
     *
     * @param requestUrl   请求url
     * @param httpCallBack Http请求回调函数
     * @param handler      消息处理Handler
     */
    public static void doGetAsyn(final String requestUrl, final IHttpCallBack httpCallBack, Handler handler) {
        // 启动一个线程
        new HttpGetThread(requestUrl, httpCallBack, handler) {
        }.start();
    }

    /**
     * <p>
     * 异步Http请求，以Post方式请求
     * </p>
     *
     * @param requestUrl   请求url
     * @param params       请求参数
     * @param httpCallBack Http请求回调函数
     */




    public static void doPostAsyn(final String requestUrl, Map<String,String> params, final IHttpCallBack httpCallBack,
                                  Handler handler, Context context) {


        // 启动一个线程
        if(ConnectManager.isNetworkAlive(context)) {
            new HttpPostThread(requestUrl, params, httpCallBack, handler, context).start();
        }else{
            //  CommonUtility.showToast(context, "网络状态不可连接，请设置网络！");
        }

//		new HttpPostThread(requestUrl, URLEncoder.encode(params), httpCallBack, handler,context, requestToken).start();
    }

    public static void doPostAsyn1(final String requestUrl, String params, final IHttpCallBack httpCallBack,
                                   Handler handler, Context context) {


        // 启动一个线程
        if(ConnectManager.isNetworkAlive(context)) {
            new HttpPostThreads(requestUrl, params, httpCallBack, handler, context).start();
        }else{
            //  CommonUtility.showToast(context, "网络状态不可连接，请设置网络！");
        }

//		new HttpPostThread(requestUrl, URLEncoder.encode(params), httpCallBack, handler,context, requestToken).start();
    }
    public static void doPostAsynupload(final Map<String,String> params, final String Filename, final File file, final String newfilename, final String requestUrl, final IHttpCallBack httpCallBack,
                                        Handler handler, Context context) {


        // 启动一个线程
        if(ConnectManager.isNetworkAlive(context)) {
            new HttpPostupload(params,Filename,file,newfilename,requestUrl,httpCallBack, handler, context).start();
        }else{
            //  CommonUtility.showToast(context, "网络状态不可连接，请设置网络！");
        }

//		new HttpPostThread(requestUrl, URLEncoder.encode(params), httpCallBack, handler,context, requestToken).start();
    }


    public static void doPostAsynupload1(final Map<String,String> params, final List<File> fileList,final List<String> newfilename,final String requestUrl, final IHttpCallBack httpCallBack,
                                         Handler handler, Context context) {


        // 启动一个线程
        if(ConnectManager.isNetworkAlive(context)) {
            new HttpPostuploads(params,fileList,newfilename,requestUrl,httpCallBack, handler, context).start();
        }else{
            //  CommonUtility.showToast(context, "网络状态不可连接，请设置网络！");
        }

//		new HttpPostThread(requestUrl, URLEncoder.encode(params), httpCallBack, handler,context, requestToken).start();
    }


    /**
     * <p>
     * 以Get方式发送Http请求
     * </p>
     *
     * @param requestUrl 请求url
     * @return Http请求返回的数据，以String形式返回
     */
    public static String doGet(String requestUrl) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(requestUrl);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(mTimeOut);
            conn.setConnectTimeout(mTimeOut);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                return baos.toString();
            } else {
                throw new RuntimeException("ResponseCode is not 200 ...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return "";
    }

    /**
     * <p>
     * 以Post方式发送Http请求
     * </p>
     *
     * @param requestUrl
     * 请求url
     * @param param
     * 请求参数，请求参数应该是 name1=value1&name2=value2 的形式;
     * @return Http请求返回的数据，以String形式返回
     */


    static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};

    public static String doPost(String requestUrl, Map<String,String> param) throws UnsupportedEncodingException {
        OutputStream out = null;
        BufferedReader in = null;
        byte[] data = getRequestData(param).toString().getBytes();
        String result = "";
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url = new URL(requestUrl);

            // 打开和URL之间的连接

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("Content-Length",
                    valueOf(data.length));
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(mTimeOut);
            conn.setConnectTimeout(mTimeOut);
            if (param != null) {
                out = conn.getOutputStream();
                out.write(data);
                // 发送请求参数
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return result;
    }


    public static String doPost1(String requestUrl, String param) {
        PrintWriter out = null;
        BufferedReader in = null;

        String result = "";
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url = new URL(requestUrl);

            // 打开和URL之间的连接

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(mTimeOut);
            conn.setConnectTimeout(mTimeOut);

            if (param != null && !param.trim().equals("")) {
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return result;
    }
    //文件上传
    public static String upload(Map<String, String> params, String fileFormName, File uploadFile, String newFileName, String urlStr)
            throws IOException {
        String reslut="";
        if (newFileName == null || newFileName.trim().equals("")) {
            newFileName = uploadFile.getName();
        }

        StringBuilder sb = new StringBuilder();
        /**
         * 普通的表单数据
         */
        for (String key : params.keySet()) {
            sb.append("--" + BOUNDARY + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"" + key + "\""
                    + "\r\n");
            sb.append("\r\n");
            sb.append(params.get(key) + "\r\n");
        }
        /**
         * 上传文件的头
         */
        sb.append("--" + BOUNDARY + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"" + fileFormName
                + "\"; filename=\"" + newFileName + "\"" + "\r\n");
        sb.append("Content-Type: image/png" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
        sb.append("\r\n");

        byte[] headerInfo = sb.toString().getBytes("UTF-8");
        byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
        System.out.println(sb.toString());
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        conn.setRequestProperty("Content-Length",
                valueOf(headerInfo.length + uploadFile.length()
                        + endInfo.length));
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        InputStream in = new FileInputStream(uploadFile);
        out.write(headerInfo);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) != -1)
            out.write(buf, 0, len);

        out.write(endInfo);
        in.close();
        out.close();
        if (conn.getResponseCode() == 200) {
            System.out.println("上传成功");
            reslut="0";
        }

        return reslut;

    }

    //文件上传
    public static String uploads(Map<String, String> params, List<File> fileList,List<String> newfilename, String urlStr)
            throws IOException {
        String reslut="";
        OkHttpClient client = new OkHttpClient();
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("image/png") , fileList.get(0));
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("image/png") , fileList.get(1));
        /* form的分割线,自己定义 */
        String boundary = "xx--------------------------------------------------------------xx";

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
                /*上传两个文件 */
        requestBody.addFormDataPart("filePath" , newfilename.get(0) , fileBody1);
        requestBody.addFormDataPart("filePath1" , newfilename.get(1) , fileBody2);

        if (params != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : params.entrySet()) {
                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
            }
        }
        /* 下边的就和post一样了 */
        Request request = new Request.Builder().url(urlStr).post(requestBody.build()).build();
        okhttp3.Response response= client.newCall(request).execute();
        if(response.isSuccessful()){
            reslut="0";
        }else {
            reslut="1";
        }
        return reslut;

    }



    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue()))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }


}