package alien95.cn.http.request.rest;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import alien95.cn.http.util.Utils;

/**
 * Created by linlongxin on 2015/12/27.
 */
public class RestHttpQueue{
    private LinkedBlockingDeque<Callable> requestQueue;
    private ExecutorService threadPool; //线程池

    private RestHttpQueue() {
        requestQueue = new LinkedBlockingDeque<>();
        if(Utils.getNumberOfCPUCores() != 0){
            threadPool = Executors.newFixedThreadPool(Utils.getNumberOfCPUCores());
        }else
            threadPool = Executors.newFixedThreadPool(4);
    }

    private static class HttpQueueHolder{
        private static final RestHttpQueue instance= new RestHttpQueue();
    }

    public static RestHttpQueue getInstance() {
        return HttpQueueHolder.instance;
    }

    public synchronized <T> Future<T> addQuest(Callable<T> callable) {
        Future<T> result = threadPool.submit(callable);
        return result;
    }

}
