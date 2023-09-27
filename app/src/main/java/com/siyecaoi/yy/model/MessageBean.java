package com.siyecaoi.yy.model;

public class MessageBean {

    /**
     * 100 普通消息 ，101 礼物消息 ,102 平台话语  ，103 欢迎语,
     * 104 红包消息，105 设置管理员，106 等级提升, 107  踢出房间
     * 108 表情消息  109 关注房主，110 下麦 ,111 禁麦 ，112加入黑名单
     * 113 广播消息  114 抱他上麦 ,115送礼物全屏通知 ,116 座驾消息（进入房间）
     * 117 进房通知消息（10级及以上）,118 房间属性变化,119 全平台消息
     * 121 全频礼物通知,122 探险全屏通知消息
     * 888 更新 房间信息
     * 999 更新 麦位信息
     */
    private int code;
    private Object data;

    public MessageBean(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public MessageBean() {

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
