package com.lau.ffmpegcommanddemo.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * @ClassName :  DensityUtil
 * @Description :
 * @Author Stephen Zhu (zfclub@163.com)
 * @Date Jan 31, 2013 12:40:44 AM
 */
public class DensityUtil {

    private static float scale;

    public static int dip2px(float dpValue) {
        return dip2px(RuntimeContext.getApplicationContext(), dpValue);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (scale == 0) scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dp2px(float dpValue) {
        return dp2px(RuntimeContext.getApplicationContext(), dpValue);
    }

    public static int dp2px(Context context, float dpValue) {
        if (scale == 0) scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(float dpValue) {
        return px2dip(RuntimeContext.getApplicationContext(), dpValue);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (scale == 0) scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float sp2px(float sp) {
        return sp2px(RuntimeContext.getApplicationContext().getResources(), sp);
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = RuntimeContext.getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = RuntimeContext.getApplicationContext().getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}
