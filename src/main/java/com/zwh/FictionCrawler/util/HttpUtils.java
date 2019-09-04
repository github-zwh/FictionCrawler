package com.zwh.FictionCrawler.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class HttpUtils {
    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
    }
    /*根据请求地址下载页面数据
     * @param Url
     * @return
     */
    public String doGetHtml(String Url){
        //获取Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //获取HttpGet请求对象，设置Url
        HttpGet httpGet = new HttpGet(Url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0");
        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //使用Httpcilent发起请求，获取响应
        CloseableHttpResponse response =null;
        try {
            response = httpClient.execute(httpGet);
            //解析响应，返回结果
            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空,如果不为空就可以使用EntityUtils
                if (response.getEntity()!=null){
                    String content = EntityUtils.toString(response.getEntity(), "gbk");
                    return content;
                }
            }
        } catch (IOException e) {
                e.printStackTrace();
        }finally {
            if (response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
         return "";
    }

    private RequestConfig getConfig() {
        RequestConfig config= RequestConfig.custom()
                .setConnectionRequestTimeout(10000)   //获取连接的最长时间
                .setConnectTimeout(10000)         //创建连接的最长时间
                .setSocketTimeout(10000)            //数据传输的最长时间
                .build();
        return config;
    }
    /**根据请求的url下载图片
     * @param Url
     * @return
     */
    public String doGetImage(String Url){
        //获取Httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //获取HttpGet请求对象，设置Url
        HttpGet httpGet = new HttpGet(Url);
        //设置请求信息
        httpGet.setConfig(this.getConfig());
        //使用Httpcilent发起请求，获取响应
        CloseableHttpResponse response =null;
        try {
            response = httpClient.execute(httpGet);
            //解析响应，返回结果
            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否为空,如果不为空就可以使用EntityUtils
                if (response.getEntity()!=null){
                    //下载图片
                    //获取图片的后缀
                    String lastName = Url.substring(Url.lastIndexOf("."));
                    //创建图片名，重新命名图片
                    String imangeName= UUID.randomUUID().toString()+lastName;
                    //下载图片
                    OutputStream os = new FileOutputStream(new File("C:\\Users\\lenovo\\Desktop\\"+imangeName));
                    response.getEntity().writeTo(os);
                    //返回图片的名称
                    return imangeName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
