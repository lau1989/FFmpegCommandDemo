package com.lau.ffmpegcommanddemo;

import com.lau.ffmpegcommanddemo.resource.pojo.VideoItem;

public class FfmpegUtil {

    /**
     * 视频裁剪指令
     *
     * @param outputPath
     * @param videoPath
     * @param startTime
     * @param endTime
     * @return
     */
    public static CmdList getCropVideoCmd(String outputPath, String videoPath, long startTime, long endTime) {
        long duration = endTime - startTime;
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-y");
        cmd.append("-ss").append(startTime / 1000).append("-t").append(duration / 1000).append("-accurate_seek");
        cmd.append("-i").append(videoPath);
        cmd.append("-codec").append("copy").append(outputPath);
        return cmd;
    }

    /**
     * 提取音频指令
     *
     * @param inputPath
     * @param outputAudio
     * @return
     */
    public static CmdList getExtractAudioCmd(String inputPath, String outputAudio) {
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i");
        cmd.append(inputPath);
        cmd.append("-vn");
        cmd.append("-y");
        cmd.append("-acodec");
        cmd.append("copy");
        cmd.append(outputAudio);
        return cmd;
    }

    /**
     * 提取视频指令
     *
     * @param inputPath
     * @param outputAudio
     * @return
     */
    public static CmdList getExtractVideoCmd(String inputPath, String outputAudio) {
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i");
        cmd.append(inputPath);
        cmd.append("-vcodec");
        cmd.append("copy");
        cmd.append("-an");
        cmd.append(outputAudio);
        return cmd;
    }

    /**
     * 获取视频信息指令
     *
     * @param inputPath
     * @return
     */
    public static CmdList getVideoInfoCmd(String inputPath) {
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i");
        cmd.append(inputPath);
        return cmd;
    }

    public static String getDuration(long duration) {
        int sec = (int) (duration / 1000) % 60;
        int min = (int) (duration / 1000) / 60;
        StringBuilder sb = new StringBuilder();
        if (min > 9) {
            sb.append(min);
        } else {
            sb.append("0").append(min);
        }
        sb.append(":");
        if (sec > 9) {
            sb.append(sec);
        } else {
            sb.append("0").append(sec);
        }
        return sb.toString();
    }

    /**
     * 拼接音视频
     * 这里需要注意的是保存的视频路径必须是新的，不可覆盖
     *
     * @param savePath
     * @param videoPath
     * @param audioPath
     * @return
     */
    public static CmdList getCombineVideoAudioCmd(String savePath, String videoPath, String audioPath) {
        CmdList cmd = new CmdList();
        cmd.append("ffmpeg");
        cmd.append("-i");
        cmd.append(videoPath);
        cmd.append("-i");
        cmd.append(audioPath);
        cmd.append("-vcodec");
        cmd.append("copy");
        cmd.append("-acodec");
        cmd.append("copy");
        cmd.append(savePath);
        return cmd;
    }

    /**
     * 给视频添加额外的音频
     *
     * @param savePath
     * @param videoPath
     * @param audioPath
     * @return
     */
    public static CmdList getAddAudioCmd(String savePath, String videoPath, String audioPath) {
        return null;
    }

    /**
     * 音频裁剪
     *
     * @param savePath
     * @param audioPath
     * @return
     */
    public static CmdList getCropAudioCmd(String savePath, String audioPath) {
        return null;
    }

    /**
     * 两个视频拼接
     *
     * @param savePath
     * @param videoPath
     * @param videoPath1
     * @return
     */
    public static CmdList getSplicingVideoCmd(String savePath, String videoPath, String videoPath1) {
        return null;
    }

    /**
     * 两个音频拼接
     *
     * @param savePath
     * @param audioPath
     * @param audioPath1
     * @return
     */
    public static CmdList getSplicingAudioCmd(String savePath, String audioPath, String audioPath1) {
        return null;
    }

    /**
     * 裁剪视频画面
     *
     * @param savePath
     * @param videoPath
     * @param width
     * @param height
     * @return
     */
    public static CmdList getCropVideoFrameCmd(String savePath, String videoPath, int width, int height) {
        return null;
    }

    /**
     * 拼接视频画面
     *
     * @param savePath
     * @param videoPath
     * @param videoPath1
     * @return
     */
    public static CmdList getCombineVideoFrameCmd(String savePath, String videoPath, String videoPath1) {
        return null;
    }

    /**
     * 获取视频帧
     *
     * @param savePath
     * @param videoPath
     * @return
     */
    public static CmdList getExtractVideoFrameCmd(String savePath, String videoPath) {
        return null;
    }

    /**
     * 图片转视频
     *
     * @param savePath
     * @param videoFrameDir
     * @return
     */
    public static CmdList getCombineFrameToVideoCmd(String savePath, String videoFrameDir) {
        return null;
    }

    /**
     * 增加水印
     *
     * @param savePath
     * @param inputVideoItem
     * @param inputImage
     * @return
     */
    public static CmdList getAddWatermarkCmd(String savePath, VideoItem inputVideoItem, String inputImage) {
        return null;
    }

    /**
     * 移除水印
     *
     * @param savePath
     * @param inputVideoItem
     * @return
     */
    public static CmdList getRemoveWatermarkCmd(String savePath, VideoItem inputVideoItem) {
        return null;
    }

    /**
     * 视频缩放
     *
     * @param savePath
     * @param inputVideoItem
     * @return
     */
    public static CmdList getScaleVideoCmd(String savePath, VideoItem inputVideoItem) {
        return null;
    }
}
