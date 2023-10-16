package com.jingtaoi.yy.control;

public interface MusicSet {
    /**
     * 开始播放
     *
     * @param filePath
     */
    void musicPlay(String filePath, String name, int length);

    /**
     * 暂停播放
     */
    void musicPause();

    /**
     * 恢复播放
     */
    void musicRePlay();
}
