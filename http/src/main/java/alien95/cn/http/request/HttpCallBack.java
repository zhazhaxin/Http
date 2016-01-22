package alien95.cn.http.request;


import alien95.cn.http.util.DebugUtils;

/**
 * Created by linlongxin on 2015/12/26.
 */
public abstract class HttpCallBack {

    public void error(){}

    public abstract void success(String info);

    public void failure(int status, String info){}

    public void getRequestTimes(int responseCode, String info, int requestNum){
        if(DebugUtils.isDebug)
        DebugUtils.responseLog(responseCode + "\n" + info,requestNum);
    }

    public void getRequestTimes(String info, int requestNum){
        if(DebugUtils.isDebug)
            DebugUtils.responseLog(info,requestNum);
    }
}
