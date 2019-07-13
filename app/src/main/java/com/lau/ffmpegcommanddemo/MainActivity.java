package com.lau.ffmpegcommanddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lau.ffmpegcommanddemo.resource.audio.AudioSelectActivity;
import com.lau.ffmpegcommanddemo.resource.pojo.AudioItem;
import com.lau.ffmpegcommanddemo.resource.pojo.VideoItem;
import com.lau.ffmpegcommanddemo.resource.video.VideoSelectActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FFMPEG";
    private static final int REQUEST_CODE_VIDEO = 0;
    private static final int REQUEST_CODE_AUDIO = 1;

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

    private TextView mVideoInputTv;
    private TextView mAudioInputTv;
    private TextView mOutputTv;

    private VideoItem mInputVideoItem;
    private AudioItem mInputAudioItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoInputTv = findViewById(R.id.video_input_tv);
        mAudioInputTv = findViewById(R.id.audio_input_tv);
        mOutputTv = findViewById(R.id.output_tv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_VIDEO:
                VideoItem videoItem = (VideoItem) data.getSerializableExtra(VideoSelectActivity.EXT_VIDEO);
                setInputVideo(videoItem);
                break;
            case REQUEST_CODE_AUDIO:
                AudioItem audioItem = (AudioItem) data.getSerializableExtra(AudioSelectActivity.EXT_AUDIO);
                setAudioItem(audioItem);
                break;
        }
    }

    private void setAudioItem(AudioItem audioItem) {
        if (mInputAudioItem == null) {
            return;
        }
        mInputAudioItem = audioItem;
        mAudioInputTv.setText(audioItem.path);
    }

    private void setInputVideo(VideoItem videoItem) {
        if (videoItem == null) {
            return;
        }
        mInputVideoItem = videoItem;
        mVideoInputTv.setText(videoItem.path);
    }

    private void setOutputPath(String outputPath) {
        mOutputTv.setText(outputPath);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_video_input_tv:
                startActivityForResult(new Intent(this, VideoSelectActivity.class), REQUEST_CODE_VIDEO);
                break;
            case R.id.btn_audio_input_tv:
                startActivityForResult(new Intent(this, AudioSelectActivity.class), REQUEST_CODE_AUDIO);
                break;
            case R.id.btn_clear_tv:
                mInputVideoItem = null;
                mVideoInputTv.setText("");
                mAudioInputTv.setText("");
                mOutputTv.setText("");
                break;
            case R.id.item_extract_audio:
                if (mInputVideoItem != null) {
                    String path = getOutputVideoPath();
                    execCmd(FfmpegUtil.getExtractAudioCmd(mInputAudioItem.path, getOutputAudioPath()));
                    setOutputPath(path);
                }
                break;
            case R.id.item_extract_video:
                if (mInputVideoItem != null) {
                    String path = getOutputVideoPath();
                    execCmd(FfmpegUtil.getExtractVideoCmd(mInputVideoItem.path, path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_crop:
                if (mInputVideoItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getCropVideoCmd(getSavePath(), mInputVideoItem.path, 2000, 5000));
                    setOutputPath(path);
                }
                break;
        }
    }

    private static String getSavePath() {
        return "/mnt/sdcard/ffmpeg_save.mp4";
    }

    private String getOutputAudioPath() {
        return "/mnt/sdcard/output_audio.aac";
    }

    private String getOutputVideoPath() {
        return "/mnt/sdcard/output_video.mp4";
    }

    private void execCmd(CmdList cmd) {
        String[] cmds = cmd.toArray(new String[cmd.size()]);
        String cmdLog = "";
        for (String ss : cmds) {
            cmdLog = cmdLog + " " + ss;
        }
        Log.i(TAG, "cmd:" + cmdLog);
        exec(cmds);
    }

    private void exec(String[] cmds) {
        ffmpegExec(cmds.length, cmds);
    }
}
