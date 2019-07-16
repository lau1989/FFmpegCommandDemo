package com.lau.ffmpegcommanddemo.resource.image;

import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lau.ffmpegcommanddemo.R;
import com.lau.ffmpegcommanddemo.fresco.FrescoManager;
import com.lau.ffmpegcommanddemo.util.DensityUtil;

public class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    final int ITEM_LEN;

    public ImageAdapter() {
        super(R.layout.image_adapter);
        ITEM_LEN = (DensityUtil.getScreenWidth() - DensityUtil.dip2px(5) * 4) / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        SimpleDraweeView sdv = helper.getView(R.id.image_sdv);
        FrescoManager.loadImage(sdv, item);

        RelativeLayout containerLayout = helper.getView(R.id.container_layout);
        ViewGroup.LayoutParams lp = containerLayout.getLayoutParams();
        lp.height = ITEM_LEN;
        containerLayout.setLayoutParams(lp);
    }

}