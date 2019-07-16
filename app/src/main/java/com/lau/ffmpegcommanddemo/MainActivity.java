package com.lau.ffmpegcommanddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lau.ffmpegcommanddemo.resource.audio.AudioSelectActivity;
import com.lau.ffmpegcommanddemo.resource.image.ImageSelectActivity;
import com.lau.ffmpegcommanddemo.resource.pojo.AudioItem;
import com.lau.ffmpegcommanddemo.resource.pojo.VideoItem;
import com.lau.ffmpegcommanddemo.resource.video.VideoSelectActivity;
import com.lau.ffmpegcommanddemo.util.PermissionCheckUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FFMPEG";
    private static final int REQUEST_CODE_SD = 30;
    private static final int REQUEST_CODE_VIDEO = 0;
    private static final int REQUEST_CODE_VIDEO1 = 1;
    private static final int REQUEST_CODE_AUDIO = 10;
    private static final int REQUEST_CODE_AUDIO1 = 11;
    private static final int REQUEST_CODE_IMAGE = 20;

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
    private TextView mVideoInputTv1;
    private TextView mAudioInputTv;
    private TextView mAudioInputTv1;
    private TextView mImageInputTv;
    private TextView mOutputTv;
    private LinearLayout mVideoInputLayout;
    private LinearLayout mVideoInputLayout1;
    private LinearLayout mAudioInputLayout;
    private LinearLayout mAudioInputLayout1;
    private LinearLayout mImageInputLayout;
    private LinearLayout mOutputLayout;

    private String mVideoFrameDir;
    private String mInputImage;
    private VideoItem mInputVideoItem;
    private VideoItem mInputVideoItem1;
    private AudioItem mInputAudioItem;
    private AudioItem mInputAudioItem1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("FFMPEG");
        mVideoInputTv = findViewById(R.id.video_input_tv);
        mVideoInputTv1 = findViewById(R.id.video1_input_tv);
        mAudioInputTv = findViewById(R.id.audio_input_tv);
        mAudioInputTv1 = findViewById(R.id.audio1_input_tv);
        mImageInputTv = findViewById(R.id.image_input_tv);

        mVideoInputLayout = findViewById(R.id.video_input_layout);
        mVideoInputLayout1 = findViewById(R.id.video_input_layout1);
        mAudioInputLayout = findViewById(R.id.audio_input_layout);
        mAudioInputLayout1 = findViewById(R.id.audio_input_layout1);
        mImageInputLayout = findViewById(R.id.image_input_layout);
        mOutputLayout = findViewById(R.id.output_layout);

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
            case REQUEST_CODE_VIDEO1:
                VideoItem videoItem1 = (VideoItem) data.getSerializableExtra(VideoSelectActivity.EXT_VIDEO);
                setInputVideo1(videoItem1);
                break;
            case REQUEST_CODE_AUDIO:
                AudioItem audioItem = (AudioItem) data.getSerializableExtra(AudioSelectActivity.EXT_AUDIO);
                setAudioItem(audioItem);
                break;
            case REQUEST_CODE_AUDIO1:
                AudioItem audioItem1 = (AudioItem) data.getSerializableExtra(AudioSelectActivity.EXT_AUDIO);
                setAudioItem1(audioItem1);
                break;
            case REQUEST_CODE_IMAGE:
                String image = data.getStringExtra(ImageSelectActivity.EXT_IMAGE);
                setImage(image);
                break;
        }
    }

    private void setImage(String image) {
        if (image == null) {
            return;
        }
        mInputImage = image;
        mImageInputTv.setText(mInputImage);
        mImageInputLayout.setVisibility(View.VISIBLE);
    }

    private void setAudioItem(AudioItem audioItem) {
        if (audioItem == null) {
            return;
        }
        mInputAudioItem = audioItem;
        mAudioInputTv.setText(audioItem.path);
        mAudioInputLayout.setVisibility(View.VISIBLE);
    }

    private void setAudioItem1(AudioItem audioItem) {
        if (audioItem == null) {
            return;
        }
        mInputAudioItem1 = audioItem;
        mAudioInputTv1.setText(audioItem.path);
        mAudioInputLayout1.setVisibility(View.VISIBLE);
    }

    private void setInputVideo(VideoItem videoItem) {
        if (videoItem == null) {
            return;
        }
        mInputVideoItem = videoItem;
        mVideoInputTv.setText(videoItem.path);
        mVideoInputLayout.setVisibility(View.VISIBLE);
    }

    private void setInputVideo1(VideoItem videoItem) {
        if (videoItem == null) {
            return;
        }
        mInputVideoItem1 = videoItem;
        mVideoInputTv1.setText(videoItem.path);
        mVideoInputLayout1.setVisibility(View.VISIBLE);
    }

    private void setOutputPath(String outputPath) {
        mOutputLayout.setVisibility(View.VISIBLE);
        mOutputTv.setText(outputPath);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_video_input_tv:
                if (PermissionCheckUtil.checkSdPermission(this, REQUEST_CODE_SD)) {
                    startActivityForResult(new Intent(this, VideoSelectActivity.class), REQUEST_CODE_VIDEO);
                }
                break;
            case R.id.btn_video_input_tv1:
                if (PermissionCheckUtil.checkSdPermission(this, REQUEST_CODE_SD)) {
                    startActivityForResult(new Intent(this, VideoSelectActivity.class), REQUEST_CODE_VIDEO1);
                }
                break;
            case R.id.btn_audio_input_tv:
                if (PermissionCheckUtil.checkSdPermission(this, REQUEST_CODE_SD)) {
                    startActivityForResult(new Intent(this, AudioSelectActivity.class), REQUEST_CODE_AUDIO);
                }
                break;
            case R.id.btn_audio_input_tv1:
                if (PermissionCheckUtil.checkSdPermission(this, REQUEST_CODE_SD)) {
                    startActivityForResult(new Intent(this, AudioSelectActivity.class), REQUEST_CODE_AUDIO1);
                }
                break;
            case R.id.btn_image_input_tv:
                if (PermissionCheckUtil.checkSdPermission(this, REQUEST_CODE_SD)) {
                    startActivityForResult(new Intent(this, ImageSelectActivity.class), REQUEST_CODE_IMAGE);
                }
                break;
            case R.id.btn_clear_tv:
                mInputVideoItem = null;
                mInputVideoItem1 = null;
                mInputAudioItem = null;
                mInputAudioItem1 = null;
                mInputImage = null;
                mVideoInputTv.setText("");
                mVideoInputTv1.setText("");
                mAudioInputTv.setText("");
                mAudioInputTv1.setText("");
                mImageInputTv.setText("");
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
            case R.id.item_combine_audio_and_video:
                if (mInputVideoItem != null && mInputAudioItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getCombineVideoAudioCmd(getSavePath(), mInputVideoItem.path, mInputAudioItem.path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_add_audio:
                if (mInputVideoItem != null && mInputAudioItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getAddAudioCmd(getSavePath(), mInputVideoItem.path, mInputAudioItem.path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_crop_audio:
                if (mInputAudioItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getCropAudioCmd(getSavePath(), mInputAudioItem.path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_splicing_video:
                if (mInputVideoItem != null && mInputVideoItem1 != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getSplicingVideoCmd(getSavePath(), mInputVideoItem.path, mInputVideoItem1.path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_splicing_audio:
                if (mInputAudioItem != null && mInputAudioItem1 != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getSplicingAudioCmd(getSavePath(), mInputAudioItem.path, mInputAudioItem1.path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_crop_video_frame:
                if (mInputVideoItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getCropVideoFrameCmd(getSavePath(), mInputVideoItem.path, 1, 1));
                    setOutputPath(path);
                }
                break;
            case R.id.item_combine_video_frame:
                if (mInputVideoItem != null && mInputVideoItem1 != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getCombineVideoFrameCmd(getSavePath(), mInputVideoItem.path, mInputVideoItem1.path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_extract_video_frame:
                if (mInputVideoItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getExtractVideoFrameCmd(getSavePath(), mInputVideoItem.path));
                    setOutputPath(path);
                }
                break;
            case R.id.item_combine_frame_to_video:
                if (mVideoFrameDir != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getCombineFrameToVideoCmd(getSavePath(), mVideoFrameDir));
                    setOutputPath(path);
                }
                break;
            case R.id.item_add_watermark:
                if (mInputVideoItem != null && mInputImage != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getAddWatermarkCmd(getSavePath(), mInputVideoItem, mInputImage));
                    setOutputPath(path);
                }
                break;
            case R.id.item_remove_watermark:
                if (mInputVideoItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getRemoveWatermarkCmd(getSavePath(), mInputVideoItem));
                    setOutputPath(path);
                }
                break;
            case R.id.item_add_text:
                break;
            case R.id.item_scale_video:
                if (mInputVideoItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getScaleVideoCmd(getSavePath(), mInputVideoItem));
                    setOutputPath(path);
                }
                break;
            case R.id.item_crop_video:
                if (mInputVideoItem != null) {
                    String path = getSavePath();
                    execCmd(FfmpegUtil.getCropVideoCmd(getSavePath(), mInputVideoItem.path, 2000, 5000));
                    setOutputPath(path);
                }
                break;
        }
    }

    private static String getSavePath() {
        File saveFile = new File("/mnt/sdcard/zzzzzz_ffmpeg_save.mp4");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        return saveFile.getAbsolutePath();
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
