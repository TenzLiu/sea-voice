package com.jingtaoi.yy.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultBitmapMemoryCacheParamsSupplier;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2019/3/18.
 */

public class ImageLoaderConfig {
    private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";

    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "image_small_cache";

    private static final int MAX_DISK_SMALL_CACHE_SIZE = 8 * ByteConstants.MB;

    private static final int MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE = 4 * ByteConstants.MB;

    private static ImagePipelineConfig sImagePipelineConfig;

    /**
     * Creates config using android http stack as network backend.
     */
    public static ImagePipelineConfig getImagePipelineConfig(final Context context) {
        if (sImagePipelineConfig == null) {
            /**
             * 推荐缓存到应用本身的缓存文件夹，这么做的好处是:
             * 1、当应用被用户卸载后能自动清除缓存，增加用户好感（可能以后用得着时，还会想起我）
             * 2、一些内存清理软件可以扫描出来，进行内存的清理
             */
            File fileCacheDir = context.getApplicationContext().getCacheDir();
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                fileCacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Fresco");
//            }

            DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                    .setBaseDirectoryPath(fileCacheDir)
                    .build();

            DiskCacheConfig smallDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(fileCacheDir)
                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                    .setMaxCacheSize(MAX_DISK_SMALL_CACHE_SIZE)
                    .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_SMALL_ONLOWDISKSPACE_CACHE_SIZE)
                    .build();

            FLog.setMinimumLoggingLevel(FLog.VERBOSE);
            Set<RequestListener> requestListeners = new HashSet<>();
            requestListeners.add(new RequestLoggingListener());

            // 当内存紧张时采取的措施
            MemoryTrimmableRegistry memoryTrimmableRegistry = NoOpMemoryTrimmableRegistry.getInstance();
            memoryTrimmableRegistry.registerMemoryTrimmable(new MemoryTrimmable() {
                @Override
                public void trim(MemoryTrimType trimType) {
                    final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();
                    LogUtils.i(String.format("Fresco onCreate suggestedTrimRatio : %d", suggestedTrimRatio));

                    if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                            || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                            ) {
                        // 清除内存缓存
                        Fresco.getImagePipeline().clearMemoryCaches();
                    }
                }
            });



            sImagePipelineConfig = ImagePipelineConfig.newBuilder(context)
                    .setBitmapsConfig(Bitmap.Config.RGB_565) // 若不是要求忒高清显示应用，就用使用RGB_565吧（默认是ARGB_8888)
                    .setDownsampleEnabled(true) // 在解码时改变图片的大小，支持PNG、JPG以及WEBP格式的图片，与ResizeOptions配合使用
                    // 设置Jpeg格式的图片支持渐进式显示
                    .setProgressiveJpegConfig(new ProgressiveJpegConfig() {
                        @Override
                        public int getNextScanNumberToDecode(int scanNumber) {
                            return scanNumber + 2;
                        }

                        public QualityInfo getQualityInfo(int scanNumber) {
                            boolean isGoodEnough = (scanNumber >= 5);
                            return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
                        }
                    })
                    .setRequestListeners(requestListeners)
                    .setDownsampleEnabled(true)//设置向下采样 如果开启该选项，pipeline 会向下采样你的图片，代替 resize 操作
                    .setMemoryTrimmableRegistry(memoryTrimmableRegistry) // 报内存警告时的监听
                    // 设置内存配置
                    .setBitmapMemoryCacheParamsSupplier(new DefaultBitmapMemoryCacheParamsSupplier(
                            (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)))
                    .setMainDiskCacheConfig(mainDiskCacheConfig) // 设置主磁盘配置
                    .setSmallImageDiskCacheConfig(smallDiskCacheConfig) // 设置小图的磁盘配置
                    .build();
        }
        return sImagePipelineConfig;
    }

}
