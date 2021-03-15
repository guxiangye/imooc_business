package com.learn.lib_network.nshttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.learn.lib_network.nshttp.exception.OkHttpException;
import com.learn.lib_network.nshttp.listener.DisposeDataHandle;
import com.learn.lib_network.nshttp.listener.DisposeDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理 file 类型的响应
 */
public class CommonFileCallback implements Callback {

    protected final String EMPTY_MSG  = "";
    protected final int NETWORK_ERROR = -1;
    protected final int IO_ERROR      = -2;
    protected final int OTHER_ERROR   = -3;
    /**
     * 将其它线程的数据转发到UI线程
     */
    private static final int PROGRESS_MESSAGE = 0x01;
    private Handler mDeliveryHandler;
    private DisposeDownloadListener mListener;
    private String mFilePath;
    private double mProgress;

    public CommonFileCallback(DisposeDataHandle handle) {
        this.mListener = (DisposeDownloadListener) handle.mListener;
        this.mFilePath = handle.mSource;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case PROGRESS_MESSAGE:
                        mListener.onProgress((Integer) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
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
    public void onResponse(Call call, Response response) throws IOException {
        final File file = handleResponse(response);
        this.mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (file != null) {
                    mListener.onSuccess(file);
                } else {
                    mListener.onFailure(new OkHttpException(IO_ERROR, EMPTY_MSG));
                }
            }
        });
    }

    /**
     * @param response
     * @return
     */
    private File handleResponse(Response response) {
        if (response == null) {
            return null;
        }
        InputStream inputStream = null;
        File file = null;
        FileOutputStream fileOutputStream = null;
        byte[] buffer = new byte[2048];
        int length;
        double currentLength = 0, sumLength = 0;
        try {
            checkLocalFilePath(mFilePath);
            file = new File(mFilePath);
            fileOutputStream = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = response.body().contentLength();
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, buffer.length);
                currentLength += length;
                mProgress = currentLength / sumLength * 100;
                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, mProgress).sendToTarget();
            }
            fileOutputStream.flush();
        } catch (Exception e) {
            file = null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                file = null;
            }
        }
        return file;
    }

    private void checkLocalFilePath(String filePath) {
        File path = new File(filePath.substring(0, filePath.lastIndexOf("/") + 1));
        File file = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
