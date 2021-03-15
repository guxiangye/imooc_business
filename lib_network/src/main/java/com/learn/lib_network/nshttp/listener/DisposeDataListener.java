package com.learn.lib_network.nshttp.listener;

public interface DisposeDataListener {

    /**
     * 成功事件回调
     */
    void onSuccess(Object responseObj);

    /**
     * 失败事件回调
     */
    void onFailure(Object reasonObj);
}
