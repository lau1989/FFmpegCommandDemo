package com.lau.ffmpegcommanddemo.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lau on 18/3/23.
 */

public class FileUtil {

    public static boolean copyFromAssertToSd(Context ctx, String fileName, String outPath) {
//        boolean ret = false;
        AssetManager assetManager = ctx.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (files != null) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fileName);
            File outFile = new File(outPath);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        InputStream in = new FileInputStream(srcFile);
        OutputStream out = new FileOutputStream(destFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static String getImageName() {
        return getImageName(".jpg");
    }

    public static String getImageName(String suffix) {
        String PATTERN = "yyyyMMddHHmmss";
        return new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date()) + suffix;
    }

    // 递归删除文件夹
    public static void deleteDir(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                deleteFile(file);
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i]);
                }
                file.delete();
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean isDelete = false;
        if (file.exists()) {
            isDelete = file.delete();
        }

        if (!isDelete && file.isFile() && file.exists()) {
            File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
            file.renameTo(to);
            isDelete = to.delete();
        }
        return isDelete;
    }


    //删除文件后更新数据库  通知媒体库更新文件夹
    public static void updateFileFromDatabase(Context context, File file) {
        if (context == null || file == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
                MediaScannerConnection.scanFile(context, paths, null, null);
                MediaScannerConnection.scanFile(context, new String[]{
                                file.getAbsolutePath()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
        }catch (Exception ignored){}
    }

    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#0.0");
        String fileSizeString = "";
        String wrongSize = "0M";
        if (fileS == 0) {
            return wrongSize;
        }
//        if (fileS < 1024) {
//            fileSizeString = df.format((double) fileS) + "B";
//            fileSizeString = "0MB";
//        } else if (fileS < 1048576) {
//            fileSizeString = df.format((double) fileS / 1024) + "KB";
//        } else if (fileS < 1073741824) {
//            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        if (fileS < 1073741824) {
            double size = (double) fileS / 1048576;
            if (size < 0.1d) {
                fileSizeString = "0.1MB";
            } else {
                fileSizeString = df.format(size) + "MB";
            }
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static long getFileOrDirSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockSize;
    }

    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        if (f != null && f.listFiles() != null) {
            File fileArr[] = f.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                if (fileArr[i].isDirectory()) {
                    size = size + getFileSizes(fileArr[i]);
                } else {
                    size = size + getFileSize(fileArr[i]);
                }
            }
        }
        return size;
    }

    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file != null && file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 刷新系统相册
     */
    public static void refreshSysGallery(Context ctx, File targetFile) {
        try {
            File targetDir = targetFile.getParentFile();
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(targetFile)));
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(ctx, new String[]{targetDir.getAbsolutePath()}, null, null);
            } else {
                ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(targetDir)));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
