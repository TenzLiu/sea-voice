package com.jingtaoi.yy.bean;

import java.util.List;

public class HomeDataBean {
    /**
     * msg : 获取成功
     * code : 0
     * data : [{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"小小米","uid":101,"userName":"小小米","usercoding":"89502341"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"连麦睡","roomName":"谢铞de闺房!??从巴登巴登呵呵呵呵呵呵呵呵呵的呵呵呵呵呵呵","uid":102,"userName":"Abitofcomedy","usercoding":"24379651"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"小小米","uid":147,"userName":"小小米","usercoding":"85790631"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"Marc","uid":148,"userName":"Marc","usercoding":"43706529"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"小小米","uid":149,"userName":"小小米","usercoding":"74125936"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"小小米","uid":150,"userName":"小小米","usercoding":"90768154"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"张小号的痘痘房间","uid":151,"userName":"18382413333","usercoding":"32918405"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"小小米","uid":152,"userName":"小小米","usercoding":"18756429"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"小小米","uid":153,"userName":"小小米","usercoding":"51468023"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"18382412223","uid":154,"userName":"18382414444","usercoding":"08345721"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"18382412222","uid":155,"userName":"18382412222","usercoding":"72634891"},{"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/3B4C5516-A5AA-49D1-9BAA-6AB4FCE88D22.jpeg","num":1,"roomLabel":"聊天","roomName":"Alex","uid":156,"userName":"Abitofcomedy","usercoding":"46703182"},{"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","num":1,"roomLabel":"聊天","roomName":"昵称","uid":157,"userName":"昵称","usercoding":"61753429"},{"img":"http://thirdqq.qlogo.cn/qqapp/1107759765/50AC222D6A68FD8301B811C885079F9C/100","num":1,"roomLabel":"聊天","roomName":"安安","uid":163,"userName":"安安","usercoding":"71453290"},{"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/7036e135-63ca-4e30-8f96-1b3f610bc262","num":9,"roomLabel":"陪玩","roomName":"房间里有人","uid":170,"userName":"哈罗","usercoding":"14326798"}]
     * sys : 1556095424961
     */

    private String msg;
    private int code;
    private String sys;
    private List<HomeItemData> data;

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

    public List<HomeItemData> getData() {
        return data;
    }

    public void setData(List<HomeItemData> data) {
        this.data = data;
    }
}
