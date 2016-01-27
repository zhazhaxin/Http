package alien95.cn.http.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import alien95.cn.http.image.callback.DiskCallback;
import alien95.cn.http.request.HttpQueue;
import alien95.cn.http.util.Utils;

/**
 * Created by linlongxin on 2015/12/29.
 */
public class DiskCache {

    private final String IMAGE_CACHE_PATH = "IMAGE_CACHE";
    private DiskLruCache diskLruCache;
    private static DiskCache instance;
    private Handler handler = new Handler();

    private DiskCache() {
        try {
            File cacheDir = Utils.setDiskCacheDir(IMAGE_CACHE_PATH);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            //20MB硬盘缓存
            diskLruCache = DiskLruCache.open(cacheDir, Utils.getAppVersion(), 1, 30 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiskCache getInstance() {
        if (instance == null) {
            synchronized (DiskCache.class) {
                if (instance == null)
                    instance = new DiskCache();
            }
        }
        return instance;
    }

    /**
     * 写入缓存到硬盘
     *
     * @param imageUrl 图片地址
     * @param mBitmap
     */
    public void writeImageToDisk(String imageUrl, final Bitmap mBitmap) {
        final String key = Utils.MD5(imageUrl);
        readImageFromDisk(imageUrl, new DiskCallback() {
            @Override
            public void callback(Bitmap bitmap) {
                if (bitmap != null) {
                    return;
                }
                DiskLruCache.Editor editor;
                try {
                    editor = diskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        boolean success = mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        if (success) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * 读取硬盘缓存
     *
     * @param imageUrl 图片地址
     */
    public void readImageFromDisk(String imageUrl, final DiskCallback callback) {
        final String key = Utils.MD5(imageUrl);
        HttpQueue.getInstance().addQuest(new Runnable() {
            @Override
            public void run() {
                try {
                    DiskLruCache.Snapshot snapShot = diskLruCache.get(key);
                    if (snapShot != null) {
                        InputStream is = snapShot.getInputStream(0);
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.callback(bitmap);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.callback(null);
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
     * 读取输入流到硬盘
     *
     * @param outputStream
     * @return
     */
    public boolean loadImageToStream(InputStream in, OutputStream outputStream) {
        BufferedOutputStream out;
        BufferedInputStream inputStream = new BufferedInputStream(in, 24 * 1024);
        try {
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            byte[] buffer = new byte[1024 * 8];
            while (inputStream.read(buffer, 0, buffer.length) != -1) {
                out.write(buffer);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
