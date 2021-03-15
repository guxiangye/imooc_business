package com.learn.lib_network.nshttp;

import com.learn.lib_network.nshttp.cookie.SimpleCookieJar;
import com.learn.lib_network.nshttp.https.HttpsUtils;
import com.learn.lib_network.nshttp.listener.DisposeDataHandle;
import com.learn.lib_network.nshttp.request.CommonRequest;
import com.learn.lib_network.nshttp.request.RequestParams;
import com.learn.lib_network.nshttp.response.CommonFileCallback;
import com.learn.lib_network.nshttp.response.CommonJsonCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用来发送 get, post 请求的工具类, 包括设置一些请求的共用参数
 */
public class CommonNSHttpClient {
    private static final int TIME_OUT = 30;
    private static OkHttpClient mOKHttpClient;

    // 完成对 okhttpClient 的初始化
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // 域名白名单
                return true;
            }
        });

        // 拦截器, 为所有请求添加请求头
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("User-Agent", "imooc-mobile")
                        .build();
                return chain.proceed(request);
            }
        });
        okHttpClientBuilder
                .cookieJar(new SimpleCookieJar())
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .followRedirects(true);

        // https 配置
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(),
                HttpsUtils.initTrustManager());

        mOKHttpClient = okHttpClientBuilder.build();
    }

    public static OkHttpClient getOKHttpClient() {
        return mOKHttpClient;
    }

    /**
     * @param url url
     * @param params params
     * @param handle handle 请求回调
     * @return call
     */
    public static Call get(String url, RequestParams params, DisposeDataHandle handle) {
        Request request = CommonRequest.createGetRequest(url, params);
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * @param url url
     * @param params params
     * @param header header
     * @param handle handle 请求回调
     * @return call
     */
    public static Call get(String url, RequestParams params, RequestParams header, DisposeDataHandle handle) {
        Request request = CommonRequest.createGetRequest(url, params, header);
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * @param url url
     * @param params params
     * @param handle handle 请求回调
     * @return call
     */
    public static Call post(String url, RequestParams params, DisposeDataHandle handle) {
        Request request = CommonRequest.createPostRequest(url, params);
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * @param url url
     * @param params params
     * @param header header
     * @param handle handle 请求回调
     * @return call
     */
    public static Call post(String url, RequestParams params, RequestParams header, DisposeDataHandle handle) {
        Request request = CommonRequest.createPostRequest(url, params, header);
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * @param url  url
     * @param params params
     * @param handle handle 请求回调
     * @return call
     */
    public static Call download(String url, RequestParams params, DisposeDataHandle handle) {
        Request request = CommonRequest.createMultiPostRequest(url, params);
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}
