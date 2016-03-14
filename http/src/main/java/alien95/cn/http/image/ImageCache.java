package alien95.cn.http.image;

import android.graphics.Bitmap;

import alien95.cn.http.image.callback.DiskCallback;

/**
 * Created by linlongxin on 2016/3/14.
 */
public interface ImageCache {

    void putBitmapToCache(String key, Bitmap bitmap);
    Bitmap getBitmapFromCache(String key);
    void getBitmapFromCacheAsync(String imageUrl, DiskCallback callback);
}
