package com.siyecaoi.yy.utils;

import androidx.annotation.NonNull;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class SvgaUtils {
    private static ConcurrentHashMap<String, SVGAVideoEntity> concurrentHashMap = new ConcurrentHashMap<>();

    /**
     * SVGA播放网络
     *
     * @param imgShow
     * @param mSVGAImageViewGift
     */
    public static void playSvgaFromUrl(String imgShow, SVGAImageView mSVGAImageViewGift, SvgaDecodeListener svgaDecodeListener) {
        try {
            //先从缓存里面拿,每次都去解析很费性能
            SVGAVideoEntity mSvgaVideoEntity = concurrentHashMap.get(imgShow);
            if (mSvgaVideoEntity != null) {
                play(mSVGAImageViewGift, mSvgaVideoEntity);
            } else {
                SVGAParser.Companion.shareParser().decodeFromURL(new URL(imgShow), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                        concurrentHashMap.put(imgShow, svgaVideoEntity);
                        play(mSVGAImageViewGift, svgaVideoEntity);
                    }

                    @Override
                    public void onError() {
                        svgaDecodeListener.onError();
                    }
                }, list -> {

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            svgaDecodeListener.onError();
        }


    }

    /**
     * SVGA播放本地
     *
     * @param imgShow
     * @param mSVGAImageViewGift
     */
    public static void playSvgaFromAssets(String imgShow, SVGAImageView mSVGAImageViewGift, SvgaDecodeListener svgaDecodeListener) {
        try {
            //先从缓存里面拿,每次都去解析很费性能
            SVGAVideoEntity mSvgaVideoEntity = concurrentHashMap.get(imgShow);
            if (mSvgaVideoEntity != null) {
                play(mSVGAImageViewGift, mSvgaVideoEntity);
            } else {
                SVGAParser.Companion.shareParser().decodeFromAssets(imgShow, new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                        concurrentHashMap.put(imgShow, svgaVideoEntity);
                        play(mSVGAImageViewGift, svgaVideoEntity);
                    }

                    @Override
                    public void onError() {
                        svgaDecodeListener.onError();
                    }
                }, list -> {

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            svgaDecodeListener.onError();
        }


    }

    /**
     * 渲染
     *
     * @param mSVGAImageViewGift
     * @param mSvgaVideoEntity
     */
    private static void play(SVGAImageView mSVGAImageViewGift, SVGAVideoEntity mSvgaVideoEntity) {
        if (mSVGAImageViewGift != null) {
            SVGADrawable drawable = new SVGADrawable(mSvgaVideoEntity);
            mSVGAImageViewGift.setImageDrawable(drawable);
            mSVGAImageViewGift.startAnimation();
        }

    }

    public interface SvgaDecodeListener {
        void onError();
    }
}
