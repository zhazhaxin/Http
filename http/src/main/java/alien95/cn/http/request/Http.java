package alien95.cn.http.request;

import java.util.Map;

/**
 * Created by linlongxin on 2015/12/26.
 */
public interface Http {
    void get(String url, HttpCallBack callBack);

    void post(String url, Map<String, String> params, HttpCallBack callBack);
}
