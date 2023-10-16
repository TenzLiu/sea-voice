package com.jingtaoi.yy.model;

public class GiftAllModel {

    /**
     * code 121 全服礼物通知
     */
    private int code;
//    private List<DataBean> data;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    //    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }

    public class DataBean {
        private String rid;// 房间id
        private String snickname;//送礼物的用户名称
        private String bnickname;//被送礼物的用户名称
        private String img;//礼物图片
        private Integer num;//数量
        private String simg; //送礼物的人的头像
        private String bimg; //被送礼物人的头像

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getSnickname() {
            return snickname;
        }

        public void setSnickname(String snickname) {
            this.snickname = snickname;
        }

        public String getBnickname() {
            return bnickname;
        }

        public void setBnickname(String bnickname) {
            this.bnickname = bnickname;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public String getSimg() {
            return simg;
        }

        public void setSimg(String simg) {
            this.simg = simg;
        }

        public String getBimg() {
            return bimg;
        }

        public void setBimg(String bimg) {
            this.bimg = bimg;
        }
    }
}
