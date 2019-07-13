package com.lau.ffmpegcommanddemo.resource.pojo;

import java.io.Serializable;

public class AudioItem implements Serializable {

    public String name;
    public String path;
    public long size;
    public long duration;

    public AudioItem(String name, String path, long size, long duration) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.duration = duration;
    }
}
