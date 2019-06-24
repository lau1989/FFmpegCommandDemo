package com.lau.ffmpegcommanddemo.util;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.IOException;

public class AppCacheFileUtil {

    private static Context mContext;

    public enum CacheFileType {

        /*
         *  这里参数的意思表示的是SDAndrCache这个路径下面的temp文件夹， SDAndrCache的具体路径看下面注释，第三个参数表示
         *  多媒体文件可否被系统扫描到，true表示不可被扫描
         */
        AndrTempCache("temp", Category.SDAndrCache, true),
        FrescoCache("fresco", Category.SDAndrCache, true),
        Upgrade("upgrade", Category.SDAndrCache, false),
        RecommendApp("apk", Category.SDAndrCache, false),
        AdApk("ad_apk", Category.SDAndrCache, false);

        private String mDirName;
        private Category category;
        private boolean noMedia;

        CacheFileType(String childDirName, Category cacheCategory, boolean nomedia) {
            mDirName = childDirName;
            category = cacheCategory;
            noMedia = nomedia;
        }

        public String getDirName() {
            return mDirName;
        }

        public File cacheDir() {
            return null == category ? null : category.cacheDir();
        }

        public Category getCategory() {
            return category;
        }

        @Override
        public String toString() {
            return mDirName;
        }

        private enum Category {
            /**
             * 6.0 do not need sd permission
             * file_path=/storage/emulated/0/Android/data/pkgName/cache/root/andrCache
             */
            SDAndrCache {
                @Override
                File cacheDir() {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return mContext.getExternalCacheDir();
                    }
                    return null;
                }
            },

            /**
             * 6.0 do not need sd permission
             * file_path=/storage/emulated/0/Android/data/pkgName/files/root/andrFiles
             */
            SDAndrFiles {
                @Override
                File cacheDir() {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return mContext.getExternalFilesDir(null);
                    }
                    return null;
                }
            },

            /**
             * 6.0 do not need sd permission
             * file_path =/data/user/0/pkgName/cache/root/dataCache
             * 这里/data/user应该是/data/data的一个映射
             */
            DataCache {
                @Override
                File cacheDir() {
                    return mContext.getCacheDir();
                }
            },

            /**
             * 6.0 do not need sd permission
             * file_path=/data/user/0/pkgName/files/root/dataFiles
             */
            DataFiles {
                @Override
                File cacheDir() {
                    return mContext.getFilesDir();
                }
            },

            /**
             * 6.0 need sd permission
             * file_path=/storage/emulated/0/root/sdExt
             */
            SDExtStorage {
                @Override
                File cacheDir() {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return Environment.getExternalStorageDirectory();
                    }
                    return null;
                }
            };

            abstract File cacheDir();
        }
    }

    private static final String ROOT_DIR_NAME = "BiVideoWallpaper";

    public static void init(Context context) {
        mContext = context;
    }

    public static File getCacheFile(CacheFileType type) {
        if (null == type.cacheDir()) {
            return null;
        }

        File cache;

        if (type.getCategory() == CacheFileType.Category.SDExtStorage) {
            cache = new File(type.cacheDir(), ROOT_DIR_NAME);
        } else {
            cache = type.cacheDir();
        }

        if (cache != null && !cache.exists()) {
            boolean rest = cache.mkdirs();
            if (!rest) {
                return null;
            }
        }

        File retDir = new File(cache, type.getDirName());
        if (!retDir.exists()) {
            boolean success = retDir.mkdirs();
            if (success) {
                if (type.noMedia) {
                    createNomediaFile(retDir);
                }
            }
        }
        return retDir;
    }

    public static void createNomediaFile(File parent) {
        if (null != parent) {
            File nomedia = new File(parent, ".nomedia");
            if (!nomedia.exists()) {
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static long getSDCacheAvailableSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = mContext.getExternalCacheDir();
            if (cacheDir != null) {
                StatFs statfs = new StatFs(cacheDir.getPath());
                return statfs.getBlockSize() * (long) statfs.getAvailableBlocks() / 1024 / 1024;
            }
        }
        return -1;
    }

    public static File getDCIMDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 刷新系统相册
     */
    public static void refreshSysGallery(Context ctx, File targetFile) {
        try {
            File targetDir = targetFile.getParentFile();
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(targetFile)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                MediaScannerConnection.scanFile(ctx, new String[]{targetDir.getAbsolutePath()}, null, null);
            } else {
                ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(targetDir)));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
