package com.lau.ffmpegcommanddemo.resource.video;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lau.ffmpegcommanddemo.R;
import com.lau.ffmpegcommanddemo.base.BaseActivity;
import com.lau.ffmpegcommanddemo.util.AppToastUtil;
import com.lau.ffmpegcommanddemo.util.DensityUtil;
import com.lau.ffmpegcommanddemo.util.PermissionCheckUtil;
import com.lau.ffmpegcommanddemo.resource.ResourceScanManager;
import com.lau.ffmpegcommanddemo.resource.pojo.VideoItem;
import com.lau.ffmpegcommanddemo.widget.recyclerview.GridItemDecoration;

import java.util.ArrayList;

public class VideoSelectActivity extends BaseActivity {

    public static final String EXT_VIDEO = "ext_video";
    private static final int REQUEST_CODE_SD = 994;

    private RecyclerView mRv;
    private VideoAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_select_activity);
        setTitle("视频选择");
        mAdapter = new VideoAdapter();
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new GridLayoutManager(this, 3));
        GridItemDecoration decoration = new GridItemDecoration(DensityUtil.dip2px(5));
        decoration.setShowLeftRightEdge(true);
        decoration.setShowTopBottomEdge(true);
        mRv.addItemDecoration(decoration);
        mRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoItem videoItem = mAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(EXT_VIDEO, videoItem);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if (PermissionCheckUtil.checkSdPermission(this, REQUEST_CODE_SD)) {
            doScanVideoList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            doScanVideoList();
        } else {
            AppToastUtil.error("需要相关权限才可获取视频列表");
        }
    }

    private void doScanVideoList() {
        ResourceScanManager.INSTANCE.startScan(this, new ResourceScanManager.IVideoScanCompleteCallback() {
            @Override
            public void scanComplete(final ArrayList<VideoItem> videoList) {
                if (isDestroyed()) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setNewData(videoList);
                    }
                });
            }
        });
    }
}
