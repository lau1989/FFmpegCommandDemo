package com.lau.ffmpegcommanddemo.fresco;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lau.ffmpegcommanddemo.R;
import com.lau.ffmpegcommanddemo.util.AppCacheFileUtil;
import com.lau.ffmpegcommanddemo.util.RuntimeContext;

import java.io.File;


/**
 *
 */
public class FrescoManager {

    public static void init(Context context) {
        DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context).setBaseDirectoryPath(AppCacheFileUtil.getCacheFile(AppCacheFileUtil.CacheFileType.FrescoCache)).build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
//                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
//                .setCacheKeyFactory(cacheKeyFactory)
                .setDownsampleEnabled(true)
//                .setWebpSupportEnabled(true)
//                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//                .setExecutorSupplier(executorSupplier)
//                .setImageCacheStatsTracker(imageCacheStatsTracker)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .setNetworkFetchProducer(networkFetchProducer)
//                .setPoolFactory(poolFactory)
//                .setProgressiveJpegConfig(progressiveJpegConfig)
//                .setRequestListeners(requestListeners)
//                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .build();

        Fresco.initialize(context, config);
    }

    public static String getResourceUri(@DrawableRes int resourceId) {
        return String.format("res://%1$s/%2$d", RuntimeContext.getPackageName(), resourceId);
    }

    public static void loadImage(final SimpleDraweeView sdv, final int resourceId) {
        loadImage(sdv, Uri.parse(String.format("res://%1$s/%2$d", RuntimeContext.getPackageName(), resourceId)));
    }

    public static void loadImage(final SimpleDraweeView sdv, String uri) {
        loadImage(sdv, uri, null);
    }

    public static void loadImage(final SimpleDraweeView sdv, String uri, ControllerListener controllerListener) {
        loadImage(sdv, uri, R.drawable.app_image_default_bg, controllerListener);
    }

    public static void loadImage(final SimpleDraweeView sdv, String uri, int defaultAndFailureImg) {
        loadImage(sdv, uri, defaultAndFailureImg, null);
    }

    public static void loadImage(final SimpleDraweeView sdv, String uri, int defaultAndFailureImg, ControllerListener controllerListener) {
        uri = uri == null ? "" : uri;
        Uri u;
        if (uri.startsWith("/")) {
            u = Uri.fromFile(new File(uri));
        } else {
            u = Uri.parse(uri);
        }
        loadImage(sdv, u, defaultAndFailureImg, controllerListener);
    }

    public static void loadImage(final SimpleDraweeView sdv, final Uri uri) {
        loadImage(sdv, uri, R.drawable.app_image_default_bg, null);
    }

    public static void loadImage(final SimpleDraweeView sdv, final Uri uri, int defaultAndFailureImg, ControllerListener controllerListener) {
        loadImage(sdv, uri, defaultAndFailureImg, defaultAndFailureImg, true, controllerListener);
    }

    public static void loadImage(final SimpleDraweeView sdv, final Uri uri, int defaultImg, int failureImg, final boolean autoPlayAnim, final ControllerListener controllerListener) {
        if (sdv == null) {
            return;
        }
        GenericDraweeHierarchy hierarchy = sdv.getHierarchy();
        hierarchy.setFadeDuration(300);
        if (defaultImg > 0) {
            hierarchy.setPlaceholderImage(defaultImg, ScalingUtils.ScaleType.CENTER_INSIDE);
        }
        if (failureImg > 0) {
            hierarchy.setFailureImage(failureImg);
        }
        sdv.setHierarchy(hierarchy);

        SizeDeterminer determiner = new SizeDeterminer(sdv);
        determiner.getSize(new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {
                ResizeOptions resizeOptions = ResizeOptions.forDimensions(width, height);
                ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(resizeOptions).build();

                PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder().setImageRequest(imageRequest).setOldController(sdv.getController()).setControllerListener(controllerListener);
                controllerBuilder.setAutoPlayAnimations(autoPlayAnim);

                DraweeController controller = controllerBuilder.build();
                sdv.setController(controller);
            }
        });
    }

}
