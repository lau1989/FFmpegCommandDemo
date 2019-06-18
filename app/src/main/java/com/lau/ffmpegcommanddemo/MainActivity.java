package com.lau.ffmpegcommanddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FFMPEG";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("ffmpeg-cmd");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropVideo("/mnt/sdcard/test.mp4", 2000, 5000);
            }
        });
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

    public static void onExecuted(int ret) {
        Log.i(TAG, "ffmpeg executed:" + ret);
    }

    public static void onProgress(float progress) {
        Log.i(TAG, "ffmpeg onProgress:" + progress);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public static native int ffmpegExec(int num, String[] cmdArr);

}
