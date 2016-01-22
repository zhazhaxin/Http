package alien95.cn.http.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import alien95.cn.http.request.HttpQueue;
import alien95.cn.http.util.DebugUtils;


/**
 * Created by linlongxin on 2015/12/26.
 */
public class HttpRequestImage {

    private final String TAG = "HttpRequestImage";

    private static HttpRequestImage instance;
    private Handler handler;

    private HttpRequestImage() {
        handler = new Handler();
    }

    public static HttpRequestImage getInstance() {
        if (instance == null) {
            synchronized (HttpRequestImage.class) {
                if (instance == null) {
                    instance = new HttpRequestImage();
                }
            }
        }
        return instance;
    }

    public void requestImage(String url, ImageCallBack callBack) {
        if (loadImageFromMemory(url) != null) {
            Log.i(TAG, "Get Picture from memoryCache");
            callBack.success(loadImageFromMemory(url));
        } else if (loadImageFromDisk(url) != null) {
            Log.i(TAG, "Get Picture from diskCache");
            callBack.success(loadImageFromDisk(url));
        } else {
            Log.i(TAG, "Get Picture from the network");
            loadImageFromNet(url, callBack);
        }
    }

    /**
     * 图片网络请求压缩处理
     * 图片压缩处理的时候内存缓存和硬盘缓存的key是通过url+inSampleSize 通过MD5加密的
     *
     * @param url
     * @param callBack
     */
    public synchronized void requestImageWithCompress(String url, int inSampleSize, ImageCallBack callBack) {
        if (inSampleSize <= 1) {
            requestImage(url, callBack);
            return;
        }
        if (loadImageFromMemory(url + inSampleSize) != null) {
            Log.i(TAG, "Compress Get Picture from memoryCache");
            callBack.success(loadImageFromMemory(url + inSampleSize));
        } else if (loadImageFromDisk(url + inSampleSize) != null) {
            Log.i(TAG, "Compress Get Picture from diskCache");
            callBack.success(loadImageFromDisk(url + inSampleSize));
        } else {
            Log.i(TAG, "Compress Get Picture from the network");
            loadImageFromNetWithCompress(url, inSampleSize, callBack);
        }
    }

    /**
     * 从内存缓存中获取已经从网络获取过的图片
     *
     * @param key
     * @return
     */
    public Bitmap loadImageFromMemory(String key) {
        return MemoryCache.getInstance().getBitmapFromMemCache(key);
    }

    /**
     * 从硬盘缓存中读取图片
     *
     * @param imageUrl
     * @return
     */
    public Bitmap loadImageFromDisk(String imageUrl) {
        return DiskCache.getInstance().readImageFromDisk(imageUrl);
    }

    public HttpURLConnection getHttpUrlConnection(String url) {
        DebugUtils.requestImageLog(url);
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(url).openConnection();
            urlConnection.setRequestMethod("GET");
        } catch (IOException e) {
            e.printStackTrace();
        }
//                urlConnection.setDoOutput(true);   //沃日，为毛请求图片不能添加这句
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(10 * 1000);
        urlConnection.setReadTimeout(10 * 1000);
        //对HttpURLConnection对象的一切配置都必须要在connect()函数执行之前完成。
        return urlConnection;
    }

    /**
     * 从网络加载图片
     *
     * @param url
     * @param callBack
     */
    private synchronized void loadImageFromNet(final String url, final ImageCallBack callBack) {
        HttpQueue.getInstance().addQuest(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = getHttpUrlConnection(url);
                int respondCode;
                try {
                    urlConnection.connect();
                    final InputStream inputStream = urlConnection.getInputStream();
                    respondCode = urlConnection.getResponseCode();
                    if (respondCode == HttpURLConnection.HTTP_OK) {
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.success(bitmap);
                                if (bitmap != null) {
                                    MemoryCache.getInstance().putBitmapToCache(url, bitmap);
                                    DiskCache.getInstance().writeImageToDisk(url, bitmap);
                                }

                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 从网络加载并压缩图片
     *
     * @param url
     * @param inSampleSize
     * @param callBack
     */
    public synchronized void loadImageFromNetWithCompress(final String url, final int inSampleSize, final ImageCallBack callBack) {
        HttpQueue.getInstance().addQuest(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = getHttpUrlConnection(url);
                int respondCode;
                try {
                    final InputStream inputStream = urlConnection.getInputStream();
                    respondCode = urlConnection.getResponseCode();
                    if (respondCode == HttpURLConnection.HTTP_OK) {
                        final Bitmap compressBitmap = ImageUtils.compressBitmapFromInputStream(inputStream, inSampleSize);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.success(compressBitmap);
                                if (compressBitmap != null) {
                                    MemoryCache.getInstance().putBitmapToCache(url + inSampleSize, compressBitmap);
                                    DiskCache.getInstance().writeImageToDisk(url + inSampleSize, compressBitmap);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
