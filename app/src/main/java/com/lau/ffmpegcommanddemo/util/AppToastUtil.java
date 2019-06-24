package com.lau.ffmpegcommanddemo.util;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lau.ffmpegcommanddemo.R;


public class AppToastUtil {

    private static Toast mToast;
    private static TextView mToastText;
    private static Handler mMainHandler;

    private static void checkInit() {
        if (mToast == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
            mToast = new Toast(RuntimeContext.getApplicationContext());
            View toastLayout = LayoutInflater.from(RuntimeContext.getApplicationContext()).inflate(R.layout.app_toast_layout, null);
            mToastText = toastLayout.findViewById(R.id.toast_text_tv);
            mToast.setView(toastLayout);
        }
    }

    public static void error(int contentResId) {
        checkInit();
        showToast(contentResId, Toast.LENGTH_LONG);
    }

    public static void error(String content) {
        checkInit();
        showToast(content, Toast.LENGTH_LONG);
    }

    public static void show(int contentResId) {
        checkInit();
        showToast(contentResId, Toast.LENGTH_LONG);
    }

    public static void show(String content) {
        checkInit();
        showToast(content, Toast.LENGTH_LONG);
    }

    public static void success(int contentResId) {
        checkInit();
        showToast(contentResId, Toast.LENGTH_LONG);
    }

    public static void success(String content) {
        checkInit();
        showToast(content, Toast.LENGTH_LONG);
    }

    private static void showToast(int contentResId, int duration) {
        mToastText.setText(contentResId);
        mToast.setDuration(duration);
        mToast.show();
    }

    private static void showToast(String content, int duration) {
        mToastText.setText(content);
        mToast.setDuration(duration);
        mToast.show();
    }


}
