package com.learn.lib_network.nshttp.exception;

import java.io.IOException;

/**
 * 自定义异常类,eCode,eMsg 到业务层
 */
public class OkHttpException extends Exception {

    private int eCode;

    private Object eMsg;

    public OkHttpException(int network_error, Object e) {
        this.eCode = network_error;
        this.eMsg = e;
    }

    public int getECode() {
        return eCode;
    }

    public Object getEMsg() {
        return eMsg;
    }
}
