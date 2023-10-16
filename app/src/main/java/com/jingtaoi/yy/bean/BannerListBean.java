package com.jingtaoi.yy.bean;

import java.util.List;

public class BannerListBean {

    /**
     * msg : 获取成功
     * code : 0
     * data : [{"content":"","createTime":"2019-04-03 14:48:13","id":2,"imgUrl":"http://yoyovoice.oss-cn-shanghai.aliyuncs.com/img/8ffa6cc8838d4df98aba61d9de05dab5.jpg","isDelete":0,"isSue":1,"orderby":2,"remark":"","title":"嗯嗯","urlHtml":"www.baidu.com","urlType":2}]
     * sys : 1556091628043
     */

    private String msg;
    private int code;
    private String sys;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content :
         * createTime : 2019-04-03 14:48:13
         * id : 2
         * imgUrl : http://yoyovoice.oss-cn-shanghai.aliyuncs.com/img/8ffa6cc8838d4df98aba61d9de05dab5.jpg
         * isDelete : 0
         * isSue : 1
         * orderby : 2
         * remark :
         * title : 嗯嗯
         * urlHtml : www.baidu.com
         * urlType : 2
         */

        private String content;
        private String createTime;
        private int id;
        private String imgUrl;
        private int isDelete;
        private int isSue;
        private int orderby;
        private String remark;
        private String title;
        private String urlHtml;
        private int urlType;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public int getIsSue() {
            return isSue;
        }

        public void setIsSue(int isSue) {
            this.isSue = isSue;
        }

        public int getOrderby() {
            return orderby;
        }

        public void setOrderby(int orderby) {
            this.orderby = orderby;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrlHtml() {
            return urlHtml;
        }

        public void setUrlHtml(String urlHtml) {
            this.urlHtml = urlHtml;
        }

        public int getUrlType() {
            return urlType;
        }

        public void setUrlType(int urlType) {
            this.urlType = urlType;
        }
    }
}
