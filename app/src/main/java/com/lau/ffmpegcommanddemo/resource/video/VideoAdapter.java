package com.lau.ffmpegcommanddemo.resource.video;

import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lau.ffmpegcommanddemo.FfmpegUtil;
import com.lau.ffmpegcommanddemo.R;
import com.lau.ffmpegcommanddemo.fresco.FrescoManager;
import com.lau.ffmpegcommanddemo.util.DensityUtil;
import com.lau.ffmpegcommanddemo.util.FileUtil;
import com.lau.ffmpegcommanddemo.resource.pojo.VideoItem;

public class VideoAdapter extends BaseQuickAdapter<VideoItem, BaseViewHolder> {

    final int ITEM_LEN;

    public VideoAdapter() {
        super(R.layout.video_adapter);
        ITEM_LEN = (DensityUtil.getScreenWidth() - DensityUtil.dip2px(5) * 4) / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoItem item) {
        SimpleDraweeView sdv = helper.getView(R.id.cover_sdv);
        FrescoManager.loadImage(sdv, item.path);

        RelativeLayout containerLayout = helper.getView(R.id.container_layout);
        ViewGroup.LayoutParams lp = containerLayout.getLayoutParams();
        lp.height = ITEM_LEN;
        containerLayout.setLayoutParams(lp);

        helper.setText(R.id.video_duration_tv, FfmpegUtil.getDuration(item.duration));
        helper.setText(R.id.video_size_tv, FileUtil.formetFileSize(item.size));

    }

}
