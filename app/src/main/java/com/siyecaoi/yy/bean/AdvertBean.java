package com.siyecaoi.yy.bean;

public class AdvertBean {

    private String msg;
    private int code;
    private DataEntity data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class DataEntity {
        /**
         * imgUrl : http://lvyouyou.oss-cn-beijing.aliyuncs.com/img/437759284fc043aa9a7a3966e6aea1f3.jpg
         * urlType : 1
         * imgTip : 1080*500
         * createTime : 2018-12-27 10:44:51
         * remark : 上线了
         * id : 1
         * content : <p>额鹅鹅鹅</p>
         * urlHtml : http://39.106.203.61/lyy/api/Advertising?id=1
         * <p>
         * content	string	内容
         * createTime	string	时间
         * id	number	id
         * imgTip	string	无
         * imgUrl	string	图片
         * remark	string	无
         * urlHtml	string	调转路径/或者房主 深海号
         * urlType	number	跳转类型1不跳，2外部，3内部，4房主房间
         */
        private String imgUrl;
        private int urlType;
        private String imgTip;
        private String createTime;
        private String remark;
        private int id;
        private String content;
        private String urlHtml;

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setUrlType(int urlType) {
            this.urlType = urlType;
        }

        public void setImgTip(String imgTip) {
            this.imgTip = imgTip;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setUrlHtml(String urlHtml) {
            this.urlHtml = urlHtml;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public int getUrlType() {
            return urlType;
        }

        public String getImgTip() {
            return imgTip;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getRemark() {
            return remark;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getUrlHtml() {
            return urlHtml;
        }
    }
}
