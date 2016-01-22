package alien95.cn.http.request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by linlongxin on 2015/12/27.
 */
public class HttpQueue {
    private LinkedBlockingDeque<Runnable> requestQueue;
    private static HttpQueue instance;
    private ExecutorService threadPool;

    private HttpQueue() {
        requestQueue = new LinkedBlockingDeque<>();
        threadPool = Executors.newFixedThreadPool(4);
    }

    public static HttpQueue getInstance() {
        if (instance == null) {
            synchronized (HttpQueue.class) {
                if (instance == null) {
                    instance = new HttpQueue();
                }
            }
        }
        return instance;
    }

    public void addQuest(Runnable runnable) {
        requestQueue.push(runnable);
        start();
    }

    private void start() {
        while (requestQueue.peek() != null) {
            threadPool.execute(requestQueue.poll());
        }
    }


}
