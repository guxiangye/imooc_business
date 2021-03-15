package com.learn.lib_network.nshttp.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.learn.lib_network.nshttp.exception.OkHttpException;
import com.learn.lib_network.nshttp.listener.DisposeDataHandle;
import com.learn.lib_network.nshttp.listener.DisposeDataListener;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理 json 类型的响应
 */
public class CommonJsonCallback implements Callback {

    protected final String EMPTY_MSG  = "";
    protected final int NETWORK_ERROR = -1;
    protected final int JSON_ERROR    = -2;
    protected final int OTHER_ERROR   = -3;
    /**
     * 将其它线程的数据转发到UI线程
     */
    private DisposeDataListener mListener;
    private Class<?> mClass;
    private Handler mDeliveryHandler;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        this.mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse (final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        this.mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(Object responseObj) {
        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            if (mClass == null) {
                mListener.onSuccess(responseObj);
            } else {
                Object obj = new Gson().fromJson(responseObj.toString(), mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, EMPTY_MSG));
            e.printStackTrace();
        }
    }
}
