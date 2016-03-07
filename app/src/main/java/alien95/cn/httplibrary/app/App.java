package alien95.cn.httplibrary.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import alien95.cn.http.request.Http;
import alien95.cn.httplibrary.BuildConfig;

/**
 * Created by linlongxin on 2016/1/22.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);
        Http.initialize(this);
        if(BuildConfig.DEBUG){
            Http.setDebug(true,"NetWork");
        }
    }
}
