package com.learn.imooc_voice.api;

import com.learn.lib_network.nshttp.CommonNSHttpClient;
import com.learn.lib_network.nshttp.listener.DisposeDataHandle;
import com.learn.lib_network.nshttp.listener.DisposeDataListener;
import com.learn.lib_network.nshttp.request.RequestParams;

/**
 * 请求中心
 */
public class RequestCenter {
    static class HttpConstants {
        // private static final String ROOT_URL = "http://imooc.com/api";
        private static final String ROOT_URL = "http://39.97.122.129";

        // 首页请求接口
        private static String HOME_RECOMMAND = ROOT_URL + "/module_voice/home_recommand";

        private static String HOME_RECOMMAND_MORE = ROOT_URL + "/module_voice/home_recommand_more";

        private static String HOME_FRIEND = ROOT_URL + "/module_voice/home_friend";

        // 登陆接口
        public static String LOGIN = ROOT_URL + "/module_voice/login_phone";
    }

    /**
     * 登录
     * @param params params
     * @param listener listener 回调
     */
    public static void login(RequestParams params, DisposeDataListener listener) {
        CommonNSHttpClient.post(HttpConstants.LOGIN, params, new DisposeDataHandle(listener));
    }

    public static void requestRecommandData(RequestParams params, DisposeDataListener listener) {
        CommonNSHttpClient.get(HttpConstants.HOME_RECOMMAND, params, new DisposeDataHandle(listener));
    }

    public static void requestRecommandMore(RequestParams params, DisposeDataListener listener) {
        CommonNSHttpClient.get(HttpConstants.HOME_RECOMMAND_MORE, params, new DisposeDataHandle(listener));
    }

    public static void requestFriendData(RequestParams params, DisposeDataListener listener) {
        CommonNSHttpClient.get(HttpConstants.HOME_FRIEND, params, new DisposeDataHandle(listener));
    }
}
