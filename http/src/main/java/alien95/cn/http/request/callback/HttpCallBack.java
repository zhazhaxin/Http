package alien95.cn.http.request.callback;


import alien95.cn.http.request.HttpConnection;
import alien95.cn.http.util.DebugUtils;
import alien95.cn.http.util.Utils;

/**
 * Created by linlongxin on 2015/12/26.
 */
public abstract class HttpCallBack {


    public abstract void success(String info);

    public void failure(int status, String info) {
        if (status == HttpConnection.NO_NETWORK) {
            Utils.Toast("没有连接网络");
        }
    }

    public void getRequestTimes(int responseCode, String info, int requestNum) {
        if (DebugUtils.isDebug)
            DebugUtils.responseLog(responseCode + "\n" + info, requestNum);
    }

    public void getRequestTimes(String info, int requestNum) {
        if (DebugUtils.isDebug)
            DebugUtils.responseLog(info, requestNum);
    }
}
