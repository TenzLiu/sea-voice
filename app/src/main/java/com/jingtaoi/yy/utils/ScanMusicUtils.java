package com.jingtaoi.yy.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.jingtaoi.yy.bean.HotMusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地音乐文件处理类
 */
public class ScanMusicUtils {
    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static List<HotMusicBean.DataBean> getMusicData(Context context) {
        List<HotMusicBean.DataBean> list = new ArrayList<>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HotMusicBean.DataBean dataBean = new HotMusicBean.DataBean();
                String song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
//                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                if (song.contains("-")) {
                    String[] str = song.split("-");
                    singer = str[1];
                    song = str[0];
                } else if (song.contains("_")) {
                    String[] str = song.split("_");
                    singer = str[1];
                    song = str[0];
                }
                dataBean.setMusicName(song);
                dataBean.setGsNane(singer);
                dataBean.setUrl(path);
                dataBean.setDataLenth(duration);
                list.add(dataBean);
            }
            // 释放资源
            cursor.close();
        }
        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间(毫秒)
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }
}
