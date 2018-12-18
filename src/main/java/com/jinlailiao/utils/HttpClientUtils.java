package com.jinlailiao.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


public class HttpClientUtils {

    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static HttpParams httpParams;
    private static PoolingClientConnectionManager connectionManager;

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 800;
    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 400;
    /**
     * 连接超时时间60秒钟
     */
    public final static int CONNECT_TIMEOUT =   60 * 1000;
    /**
     * 设置等待数据超时时间60秒钟
     */
    public final static int READ_TIMEOUT = 60 * 1000;

    static {
        httpParams = new BasicHttpParams();
        // 设置连接超时时间
        httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
        // 设置读取超时时间
        httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT);
        // 设置访问协议
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        // 多连接的线程安全的管理器
        connectionManager = new PoolingClientConnectionManager(registry);
        // 每个主机的最大并行链接数
        connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
        // 客户端总并行链接最大数
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    }

    public static HttpClient getCustomerHttpClient() {
        return new DefaultHttpClient(connectionManager, httpParams);
    }

    public static String getMethod(String httpUrl, List<BasicNameValuePair> params) throws Throwable {
        logger.info("HTTP URL: " + httpUrl);
        if (params != null) {
            logger.info("HTTP Request params: " + params.toString());
        }
        HttpClient httpClient = getCustomerHttpClient();
        if (params != null && params.size() > 0) {
            //对参数编码
            String strParams = URLEncodedUtils.format(params, "UTF-8");
            httpUrl += "?" + strParams;
        }
        HttpGet httpGet = new HttpGet(httpUrl);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        try {
            //logger.info("HTTP Entity" + EntityUtils.toString(httpResponse.getEntity(), "无实体"));
            logger.info("HTTP Status: " + httpResponse.getStatusLine().getStatusCode());
            logger.info("HTTP ReasonPhrase: " + httpResponse.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            logger.error("Log error: " + e.toString());
        }

        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        }
        return null;
    }

    public static String postMethod(String httpUrl, List<BasicNameValuePair> params) throws Throwable {
        HttpResponse httpResponse = executePost(httpUrl, params);
        try {
            //logger.info("HTTP Entity" + EntityUtils.toString(httpResponse.getEntity(), "无实体"));
            logger.info("HTTP Status: " + httpResponse.getStatusLine().getStatusCode());
            logger.info("HTTP ReasonPhrase: " + httpResponse.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            logger.error("Log error: " + e.toString());
        }
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        }
        return null;
    }

    public static String putMethod(String httpUrl, List<BasicNameValuePair> params) throws Throwable {
        HttpClient httpClient = getCustomerHttpClient();
        HttpPut httpPut = new HttpPut(httpUrl);
        if (params != null && params.size() > 0) {
            httpPut.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //将参数填入POST Entity中
        }
        HttpResponse httpResponse = httpClient.execute(httpPut);
        try {
            //logger.info("HTTP Entity" + EntityUtils.toString(httpResponse.getEntity(), "无实体"));
            logger.info("HTTP Status: " + httpResponse.getStatusLine().getStatusCode());
            logger.info("HTTP ReasonPhrase: " + httpResponse.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            logger.error("Log error: " + e.toString());
        }
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        }
        return null;
    }

    public static String deleteMethod(String httpUrl, List<BasicNameValuePair> params) throws Throwable {
        logger.info("HTTP URL: " + httpUrl);
        if (params != null) {
            logger.info("HTTP Request params: " + params.toString());
        }
        HttpClient httpClient = getCustomerHttpClient();
        if (params != null && params.size() > 0) {
            //对参数编码
            String strParams = URLEncodedUtils.format(params, "UTF-8");
            httpUrl += "?" + strParams;
        }
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        HttpResponse httpResponse = httpClient.execute(httpDelete);
        try {
            //logger.info("HTTP Entity" + EntityUtils.toString(httpResponse.getEntity(), "无实体"));
            logger.info("HTTP Status: " + httpResponse.getStatusLine().getStatusCode());
            logger.info("HTTP ReasonPhrase: " + httpResponse.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            logger.error("Log error: " + e.toString());
        }

        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        }
        return null;
    }

    public static String patchMethod(String httpUrl, List<BasicNameValuePair> params) throws Throwable {
        HttpClient httpClient = getCustomerHttpClient();
        HttpPatch httpPatch = new HttpPatch(httpUrl);
        if (params != null && params.size() > 0) {
            httpPatch.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //将参数填入POST Entity中
        }
        HttpResponse httpResponse = httpClient.execute(httpPatch);
        try {
            //logger.info("HTTP Entity" + EntityUtils.toString(httpResponse.getEntity(), "无实体"));
            logger.info("HTTP Status: " + httpResponse.getStatusLine().getStatusCode());
            logger.info("HTTP ReasonPhrase: " + httpResponse.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            logger.error("Log error: " + e.toString());
        }
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        }
        return null;
    }

    public static InputStream postImage(String httpUrl, List<BasicNameValuePair> params) throws Throwable {
        HttpResponse httpResponse = executePost(httpUrl, params);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            //将返回内容转换为bitmap
            InputStream inputStream = httpResponse.getEntity().getContent();
            return inputStream;
        }
        return null;
    }

    public static HttpResponse executePost(String httpUrl, List<BasicNameValuePair> params) throws Throwable {
        HttpClient httpClient = getCustomerHttpClient();
        HttpPost httpPost = new HttpPost(httpUrl);
        if (params != null && params.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //将参数填入POST Entity中
        }
        HttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            //logger.info("HTTP Entity" + EntityUtils.toString(httpResponse.getEntity(), "无实体"));
            logger.info("HTTP Status: " + httpResponse.getStatusLine().getStatusCode());
            logger.info("HTTP ReasonPhrase: " + httpResponse.getStatusLine().getReasonPhrase());
        } catch (Exception e) {
            logger.error("Log error: " + e.toString());
        }
        return httpResponse;
    }

    public static boolean postUpload(File file, String url, Map<String, String> params) throws Throwable {
        HttpClient httpclient = getCustomerHttpClient();
        HttpPost httppost = new HttpPost(url);
        System.out.println("executing request url " + url);
        MultipartEntity mpEntity = new MultipartEntity(); //文件传输
        // 添加上传文件
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("file", cbFile); // <input type="file" name="userfile" />  对应的
        // 添加请求参数
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                StringBody stringBody = new StringBody(params.get(key));
                mpEntity.addPart(key, stringBody);
            }
        }

        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);//通信Ok
        if (resEntity != null) {
            resEntity.getContent().close();
        }
        return statusCode == HttpStatus.SC_OK;
    }

    public static void postDowload(String filePath, String url) throws Throwable {
        HttpClient httpclient = getCustomerHttpClient();
        HttpPost httppost = new HttpPost(url);
        System.out.println("executing request url " + url);
        HttpResponse resp = httpclient.execute(httppost);
        //判断访问状态是否正确执行
        if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {
            HttpEntity entity = resp.getEntity();
            InputStream inputStream = entity.getContent();
            File file = new File(filePath);
            OutputStream outputStream = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((inputStream.read(buffer)) != -1) {
                outputStream.write(buffer);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
    }

}

