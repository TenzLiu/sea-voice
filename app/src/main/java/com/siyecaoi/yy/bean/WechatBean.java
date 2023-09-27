package com.siyecaoi.yy.bean;

import com.google.gson.annotations.SerializedName;

public class WechatBean {

    private long sys;
    private DataBean data;
    private int code;
    private String msg;

    public long getSys() {
        return sys;
    }

    public void setSys(long sys) {
        this.sys = sys;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * sign : F5DB0CB21A2181A53A74C716DE098175
         * timeStamp : 1511858574
         * partnerId : 1492971582
         * package : Sign=WXPay
         * appid : wxa175fc902a047677
         * nonceStr : gs2rv1ggp2klwfb549qrqgho3x5b3spb
         * prepayId : wx20171128164252a89dee9d3a0794307062
         */

        private String sign;
        private String timeStamp;
        private String partnerId;
        @SerializedName("package")
        private String packageX;
        private String appid;
        private String nonceStr;
        private String prepayId;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }
    }
}
