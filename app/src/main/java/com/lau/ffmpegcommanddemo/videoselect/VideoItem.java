package com.lau.ffmpegcommanddemo.videoselect;

import java.io.Serializable;

public class VideoItem implements Serializable {

    public String name;
    public String path;
    public long size;
    public long duration;

    public VideoItem(String name, String path, long size, long duration) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.duration = duration;
    }
}
