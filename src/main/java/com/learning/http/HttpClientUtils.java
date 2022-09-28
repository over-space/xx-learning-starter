package com.learning.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 李芳
 * @since 2022/9/26
 */
public final class HttpClientUtils {

    private static final String EMPTY_STR = "";
    private static final String UTF_8 = "UTF-8";
    private static PoolingHttpClientConnectionManager cm;

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            // 整个连接池最大连接数
            cm.setMaxTotal(150);
            // 每路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(50);
        }
    }

    private static CloseableHttpClient getHttpClient() {
        init();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000)//设定连接服务器超时时间
                .setConnectTimeout(120000)//设定从连接池获取可用连接的时间
                .setSocketTimeout(120000)//设定获取数据的超时时间
                .build();
        return HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
    }

    public static String post(String url, Map<String, Object> headers, String json) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.setHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        httpPost.setEntity(new StringEntity(json, ContentType.create("application/json", "utf-8")));

        Map<String, Object> builder = new HashMap<String, Object>();
        builder.put("header", headers);
        builder.put("request", json);

        return getResult(httpPost, builder);
    }

    private static String getResult(HttpRequestBase request, Map<String, Object> logBuilder) throws IOException {
        logBuilder.put("method", request.getMethod());
        logBuilder.put("url", request.getURI());
        return getResult(getHttpClient().execute(request), logBuilder);
    }

    private static String getResult(CloseableHttpResponse response, Map<String, Object> logBuilder) throws IOException {
        String result = "";
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, UTF_8);
                response.close();
                return result;
            }

            return EMPTY_STR;
        } catch (Exception e) {
            throw e;
        } finally {
            logBuilder.put("result", result);
        }
    }
}
