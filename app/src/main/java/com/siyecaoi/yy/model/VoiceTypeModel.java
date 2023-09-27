package com.siyecaoi.yy.model;

import java.util.Objects;

public class VoiceTypeModel {
    private int type; //待处理消息类型  1关注消息(显示关注消息) 2 表情消息（关闭表情消息） 3是否说话中（关闭说话中状态）
    private int position;//麦位   (0-8)  8代表房主或当前用户
    private int time;//待处理时间  单位s

    /**
     *
     * @param type 消息类型
     * @param position 麦位  8代表房主或当前用户
     * @param time 时间 s
     */
    public VoiceTypeModel(int type, int position, int time) {
        this.type = type;
        this.position = position;
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoiceTypeModel that = (VoiceTypeModel) o;
        return type == that.type &&
                position == that.position;
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, position);
    }

    @Override
    public String toString() {
        return "VoiceTypeModel{" +
                "type=" + type +
                ", position=" + position +
                ", time=" + time +
                '}';
    }
}
