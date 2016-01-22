package alien95.cn.httplibrary.app;

import android.app.Application;

import alien95.cn.http.request.HttpRequest;
import alien95.cn.httplibrary.BuildConfig;
import alien95.cn.util.Utils;

/**
 * Created by linlongxin on 2016/1/22.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Utils.initialize(this);
        if(BuildConfig.DEBUG){
            HttpRequest.setDebug(true,"NetWork");
        }
    }
}
