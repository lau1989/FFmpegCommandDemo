package com.lau.ffmpegcommanddemo.videoselect;

import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lau.ffmpegcommanddemo.R;
import com.lau.ffmpegcommanddemo.fresco.FrescoManager;
import com.lau.ffmpegcommanddemo.util.DensityUtil;

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

        helper.setText(R.id.video_duration_tv, getDuration(item.duration));

    }

    private String getDuration(long duration) {
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
