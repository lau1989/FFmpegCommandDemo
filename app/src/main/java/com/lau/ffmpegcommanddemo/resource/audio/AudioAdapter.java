package com.lau.ffmpegcommanddemo.resource.audio;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lau.ffmpegcommanddemo.FfmpegUtil;
import com.lau.ffmpegcommanddemo.R;
import com.lau.ffmpegcommanddemo.util.FileUtil;
import com.lau.ffmpegcommanddemo.resource.pojo.AudioItem;

public class AudioAdapter extends BaseQuickAdapter<AudioItem, BaseViewHolder> {


    public AudioAdapter() {
        super(R.layout.audio_adapter);
    }

    @Override
    protected void convert(BaseViewHolder helper, AudioItem item) {
        helper.setText(R.id.audio_duration_tv, FfmpegUtil.getDuration(item.duration));
        helper.setText(R.id.audio_size_tv, FileUtil.formetFileSize(item.size));
        helper.setText(R.id.audio_name_tv, item.name);
    }

}
