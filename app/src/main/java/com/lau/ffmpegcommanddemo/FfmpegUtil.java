package com.lau.ffmpegcommanddemo;

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

}
