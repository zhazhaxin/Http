package alien95.cn.http.image;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by linlongxin on 2015/12/29.
 */
public class MemoryCache {

    private final String TAG = "MemoryCache";

    private LruCache<String, Bitmap> lruCache;

    private static MemoryCache instance;

    int maxMemory;

    private MemoryCache() {
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        malloc(maxMemory / 8);
    }

    public static MemoryCache getInstance() {
        if (instance == null) {
            synchronized (MemoryCache.class) {
                if (instance == null)
                    instance = new MemoryCache();
            }
        }
        return instance;
    }

    /**
     * @param size 单位：KB，内存分配大小
     */
    private void malloc(int size) {

        lruCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void putBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            lruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return lruCache.get(key);
    }


}
