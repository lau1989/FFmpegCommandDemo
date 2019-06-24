package com.lau.ffmpegcommanddemo.videoselect;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;

public enum ResourceScanManager {

    INSTANCE;

    private static final String[] STORE_PHOTO = {MediaStore.Images.Media.DISPLAY_NAME,       //name
            MediaStore.Images.Media.DATA,               //path
            MediaStore.Images.Media.DATE_MODIFIED,      //modified
            MediaStore.Images.Media.SIZE,               //size
    };

    private static final String[] STORE_VIDEO = {
            MediaStore.Video.Media.DISPLAY_NAME,       //name
            MediaStore.Video.Media.DATA,               //path
            MediaStore.Video.Media.SIZE,               //size
            MediaStore.Video.Media.DURATION,           //duration
//            MediaStore.Video.Media.DATE_MODIFIED,    //modified
    };

    private static final String[] STORE_AUDIO = {MediaStore.Audio.Media.DISPLAY_NAME,       //name
            MediaStore.Audio.Media.DATA,               //path
            MediaStore.Audio.Media.DATE_MODIFIED,      //modified
            MediaStore.Audio.Media.SIZE,                //size
    };

    public interface IVideoScanCompleteCallback {
        void scanComplete(ArrayList<VideoItem> videoList);
    }

    private IVideoScanCompleteCallback mScanCallback;
    private boolean mDestroy = false;

    public void setScanCallback(IVideoScanCompleteCallback scanCallback) {
        mScanCallback = scanCallback;
    }

    public void onDestroy() {
        mDestroy = true;
    }

    public void startScan(final Context context, final IVideoScanCompleteCallback callback) {
        mScanCallback = callback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = context.getContentResolver();
                ArrayList<VideoItem> videoItems = startVideoScan(cr);
                if (callback != null) {
                    callback.scanComplete(videoItems);
                }
            }
        }).start();
    }

    private ArrayList<VideoItem> startVideoScan(ContentResolver cr) {
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = MediaStore.Images.Media.query(cr, videoUri, STORE_VIDEO);
        ArrayList<VideoItem> videoItems = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (mDestroy) {
                    return videoItems;
                }
                String name = cursor.getString(0);
                String path = cursor.getString(1);
                long size = cursor.getLong(2);
                long duration = cursor.getLong(3);

                if (!isFileExist(path)) {
                    continue;
                }

                VideoItem item = new VideoItem(name, path, size, duration);
                videoItems.add(item);
            }
        }
        return videoItems;
    }

    private boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file != null) {
            return file.exists();
        }
        return false;
    }

}
