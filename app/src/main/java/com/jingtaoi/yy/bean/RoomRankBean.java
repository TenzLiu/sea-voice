package com.jingtaoi.yy.bean;

import android.text.TextUtils;

import java.util.List;

public class RoomRankBean {
    /**
     * msg : 获取成功
     * code : 0
     * data : [{"grade":11,"id":189,"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/3b053c9e-3b91-4f67-9708-f7f13690b14f","name":"母贝1号","num":0,"sex":1,"usercoding":"54068239"},{"grade":11,"id":172,"img":"http://thirdqq.qlogo.cn/g?b=oidb&k=1tb7tEdrLaPniasRgbz4yyw&s=100","name":"家有逗比小花喵！123456","num":207785,"sex":1,"usercoding":"61420758"},{"grade":10,"id":181,"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/9c20e245-f9a4-46ba-af1b-b875da213767","name":"木木","num":622,"sex":1,"usercoding":"61729438"},{"grade":11,"id":197,"img":"http://thirdqq.qlogo.cn/g?b=oidb&k=E9b9AKjveV5PQKPG3gYFRg&s=100","name":"Game is over","num":1612,"sex":2,"usercoding":"67302495"},{"grade":5,"id":179,"img":"http://cyqqmeitu.oss-cn-shenzhen.aliyuncs.com/img/249d6336e2d84973b546706a7504af2f.png","name":"偶偶也我呀要找我们玩哭了哦我哦哟哟哟哦木哭哭哭他哭","num":387,"sex":1,"usercoding":"09751823"},{"grade":11,"id":170,"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/7036e135-63ca-4e30-8f96-1b3f610bc262","name":"哈罗","num":345,"sex":2,"usercoding":"14326798"},{"grade":11,"id":178,"img":"http://thirdqq.qlogo.cn/g?b=oidb&k=E5MyF8G0ddxnNylYia45t1w&s=100","name":"母贝","num":1456,"sex":2,"usercoding":"57408361"},{"grade":11,"id":180,"img":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/582096a1-1dd1-468d-adc6-fc6147cd12cb","name":"gown可以可以","num":0,"sex":1,"usercoding":"70326485"}]
     * sys : 1555662413571
     */

    private String msg;
    private int code;
    private String sys;
    public List<DataBean> data;


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



    public static class DataBean {
        /**
         * grade : 11
         * id : 189
         * img : http://yoyovoice.oss-cn-shanghai.aliyuncs.com/3b053c9e-3b91-4f67-9708-f7f13690b14f
         * name : 母贝1号
         * num : 0
         * sex : 1
         * usercoding : 54068239
         */

        private int grade;
        private int id;
        private String img;
        private String imgTx;
        private String name;
        public int rankNum;
        private int num;
        private int sex;
        private String usercoding;
        private int isliang;
        private String liang;
        private String nickname;
        private int uid;


        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getId() {
            if (id==0)
                id=uid;
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }



        public String getImg() {
            if (TextUtils.isEmpty(img))
                img=imgTx;
            return img;
        }

        public void setImg(String img) {

            this.img = img;
        }

        public String getName() {
            if (TextUtils.isEmpty(name))
                name=nickname;
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getUsercoding() {
            return usercoding;
        }

        public void setUsercoding(String usercoding) {
            this.usercoding = usercoding;
        }

        public String getImgTx() {
            return imgTx;
        }

        public void setImgTx(String imgTx) {
            this.imgTx = imgTx;
        }

        public int getIsliang() {
            return isliang;
        }

        public void setIsliang(int isliang) {
            this.isliang = isliang;
        }

        public String getLiang() {
            return liang;
        }

        public void setLiang(String liang) {
            this.liang = liang;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
