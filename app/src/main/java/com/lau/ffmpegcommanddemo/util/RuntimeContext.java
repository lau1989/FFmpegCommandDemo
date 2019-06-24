package com.lau.ffmpegcommanddemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RuntimeContext {

    public enum PhoneType {
        LOWER,
        LOW,
        MIDDLE,
        HIGH
    }

    public static void init(Context ctx) {
        sApplicationContext = ctx;
        sPackageName = ctx.getPackageName();
        sCurProcessName = getProcessName();
        if (TextUtils.equals(sPackageName, getProcessName())) {
            sIsMainProcess = true;
        }
        sEPhoneType = getPhoneType(ctx);
    }

    /**
     * 是否是主进程
     */
    static boolean sIsMainProcess = false;
    static String sPackageName = null;
    static String sCurProcessName = null;
    static long sProcessStartConsumeTime = -1;
    static Context sApplicationContext;
    static boolean sIsDebuggable = false;
    static long sMainActivityStartTime = -1;
    static long sAppOncreateTime = -1;
    static PhoneType sEPhoneType = PhoneType.MIDDLE;

    public static String sAppid;

    public static String getPackageName() {
        return sPackageName;
    }

    public static String getCurProcessName() {
        return sCurProcessName;
    }

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    public static PhoneType getEPhoneType() {
        return sEPhoneType;
    }

    public static boolean isDebuggable() {
        return sIsDebuggable;
    }

    /**
     * 程序启动标志
     */
    public static boolean sIsAppStarted = false;
    /**
     * 程序启动完毕
     */
    public static volatile boolean sIsAppStartFinished = false;
    /**
     * 应用正在退出标志，主要用在业务场景：程序尚未完全终止运行时，用户又再次启动了应用，那么再次启动讲直接终止
     */
    public static boolean sIsAppExisting = false;

    /**
     * 程序启动类型
     */
    public static int sStartType = -1;


    /**
     * 程序前后台标志
     */
    public static boolean sIsForeground = false;
    public static String sVersion = null;

    public static boolean sIsColdLaunch = true;
    public static boolean sIsLaunchByActivity = true;

    public static void onAppExit() {
        sIsColdLaunch = false;
        sMainActivityStartTime = -1;
        sIsLaunchByActivity = true;
    }

    /**
     * @return 获取进程名
     */
    private static String getProcessName() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + android.os.Process.myPid() + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 对手机进行分类，默认为中端机
     * 1 android系统版本为4.x或者内存小于等于1G->超低端机；
     * 2 内存小于1.5G -> 低端机
     * 3 内存小于3G或者系统版本为5.0—>中端机
     * 4 其他高端机
     */
    private static PhoneType getPhoneType(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(mi);
        int memoryLow = 1610612736; //1024*1024*1024 1.5G,一般 1.5G的手机实际内存也就7/800多MB左右
        int memorySuperLow = 1073741824; //1024*1024*1024

        PhoneType phoneType = PhoneType.MIDDLE;
        int sdkVer = Build.VERSION.SDK_INT;
        if (sdkVer < 21 || mi.totalMem <= memorySuperLow) { //系统5.0以下或者内存小于1.0G, 全部认定为低端手机,且为超低端机
            phoneType = RuntimeContext.PhoneType.LOWER;
        } else if (mi.totalMem <= memoryLow) { //系统5.0以下或者内存小于1.5G, 全部认定为低端手机
            phoneType = RuntimeContext.PhoneType.LOW;
        } else if (mi.totalMem <= 2.2f * memoryLow || sdkVer == 21) {
            phoneType = RuntimeContext.PhoneType.MIDDLE;
        } else {
            phoneType = RuntimeContext.PhoneType.HIGH;
        }
        return  phoneType;
    }

}
