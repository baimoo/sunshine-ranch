package com.baimo.tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 该类存放两个请求方式
 */
public class URLTools {
    int i = 0;

    //发送get请求获取网页
    public String getDocmentByGep(String url, String param) {

        String result = "";
        BufferedReader in = null;

        try {
            String charset = StandardCharsets.UTF_8.name();
            String request = url + "?" + param;

            //打开和URL之间的连接
            URLConnection connection = new URL(request).openConnection();

            /* begin获取响应码 */
            HttpURLConnection httpUrlConnection = (HttpURLConnection) connection;
            httpUrlConnection.setConnectTimeout(30000);
            httpUrlConnection.setReadTimeout(30000);
            httpUrlConnection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                result += "\n" + inputLine;
            }

        } catch (Exception e) {
            i++;
            if (i < 4) {
                System.err.println("操作失败！重在重试..." + i + "/3" + "\t错误原因：" + e);
                getDocmentByGep(url, param);
            } else {
                System.err.println("3次重试失败！请稍后再试！");
            }
//            e.printStackTrace();
        }// 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    //发送post请求获取网页
    public String getDocmentByPost(String url, Map<String, String> parameterMap) {
        BufferedReader br = null;
        String result = null;
        try {

            URL url2 = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true); // 设置该连接是可以输出的
            httpURLConnection.setRequestMethod("POST"); // 设置请求方式
            httpURLConnection.setRequestProperty("charset", "utf-8");
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(httpURLConnection.getOutputStream()));
            StringBuffer parameter = new StringBuffer();
            for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
                parameter.append("&" + entry.getKey() + "=" + entry.getValue());
            }
            pw.write(parameter.toString());// 向连接中写数据（相当于发送数据给服务器）
            pw.flush();
            pw.close();
            br = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream(), "utf-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) { // 读取数据
                sb.append(line + "\n");
            }
            br.close();
            result = sb.toString();
            return result;
        } catch (IOException e) {
            i++;
            if (i < 4) {
                System.err.println("操作失败！重在重试..." + i + "/3" + "\t错误原因：" + e);
                getDocmentByPost(url, parameterMap);
            } else {
                System.err.println("3次重试失败！请稍后再试！");
            }
//            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
