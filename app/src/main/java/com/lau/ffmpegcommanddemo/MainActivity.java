package com.lau.ffmpegcommanddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lau.ffmpegcommanddemo.videoselect.VideoSelectActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FFMPEG";
    private static final int REQUEST_CODE_VIDEO = 0;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("ffmpeg-cmd");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public static native int ffmpegExec(int num, String[] cmdArr);

    public static void onExecuted(int ret) {
        Log.i(TAG, "ffmpeg executed:" + ret);
    }

    public static void onProgress(float progress) {
        Log.i(TAG, "ffmpeg onProgress:" + progress);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_VIDEO:
                break;
        }
    }

    public static void cropVideo(String videoPath, long startTime, long endTime) {
        long duration = endTime - startTime;
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-y");
        cmd.append("-ss").append(startTime / 1000).append("-t").append(duration / 1000).append("-accurate_seek");
        cmd.append("-i").append(videoPath);
        cmd.append("-codec").append("copy").append(getSavePath());

        execCmd(cmd, duration);
    }

    private static String getSavePath() {
        return "/mnt/sdcard/ffmpeg_save.mp4";
    }

    private static void execCmd(CmdList cmd, long duration) {
        String[] cmds = cmd.toArray(new String[cmd.size()]);
        String cmdLog = "";
        for (String ss : cmds) {
            cmdLog = cmdLog + " " + ss;
        }
        Log.i(TAG, "cmd:" + cmdLog);
        exec(cmds, duration);
    }

    public static void exec(String[] cmds, long duration) {
        ffmpegExec(cmds.length, cmds);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_crop:
//                cropVideo("/mnt/sdcard/test.mp4", 2000, 5000);
                startActivityForResult(new Intent(this, VideoSelectActivity.class), REQUEST_CODE_VIDEO);
                break;
        }
    }
}
