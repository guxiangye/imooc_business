package com.learn.lib_network.nshttp.listener;

/**
 * 监听下载进度
 */
public interface DisposeDownloadListener extends DisposeDataListener {
    void onProgress(int progress);
}
