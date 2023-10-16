package com.jingtaoi.yy.bean;


import java.util.List;

public class HomeRankBean {
    /**
     * msg : 获取成功
     * code : 0
     * data : [{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","roomLabel":"聊天","roomName":"18382412222","uid":155,"userName":"18382412222","usercoding":"72634891"},{"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/3B4C5516-A5AA-49D1-9BAA-6AB4FCE88D22.jpeg","roomLabel":"聊天","roomName":"Alex","uid":156,"userName":"Abitofcomedy","usercoding":"46703182"},{"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/ce804959-f525-4797-b6f5-b2c1d49786d0","roomLabel":"聊天","roomName":"肉","uid":195,"userName":"肉","usercoding":"65987014"},{"img":"http://thirdqq.qlogo.cn/g?b=oidb&k=1tb7tEdrLaPniasRgbz4yyw&s=100","roomLabel":"吃鸡","roomName":"家有逗比小花喵！123456","uid":172,"userName":"家有逗比小花喵！123456","usercoding":"61420758"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","roomLabel":"聊天","roomName":"aaaaaaa1","uid":173,"userName":"aaaaaaa1","usercoding":"95730481"},{"img":"http://lvyouyou.oss-cn-beijing.aliyuncs.com/9a376757-0322-466d-9f21-a648d6cd3307","roomLabel":"聊天","roomName":"大大","uid":174,"userName":"大大","usercoding":"29417853"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","roomLabel":"聊天","roomName":"aaaaaaa1","uid":173,"userName":"aaaaaaa1","usercoding":"95730481"},{"img":"http://thirdqq.qlogo.cn/g?b=oidb&k=E5MyF8G0ddxnNylYia45t1w&s=100","roomLabel":"聊天","roomName":"母贝","uid":178,"userName":"母贝","usercoding":"57408361"}]
     * sys : 1556093050121
     */

    private String msg;
    private int code;
    private String sys;
    private HotRankData data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public HotRankData getData() {
        return data;
    }

    public void setData(HotRankData data) {
        this.data = data;
    }

    public static class HotRankData{
        public List<RankBean.DataBean> list;
        public List<RankBean.DataBean> roomList;
    }
}
