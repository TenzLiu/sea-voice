package com.jingtaoi.yy.bean;

import java.io.Serializable;

public class ImgEntity implements Serializable {
    /**
     * uid : 170
     * isDelete : 1
     * id : 1
     * relevanceId : 170
     * type : 1
     * url : http://yoyovoice.oss-cn-shanghai.aliyuncs.com/img/6c389cb5355544b5976dc6b4daf66ffd.jpg
     * createDate : 2019-03-20 14:50:58
     * <p>
     * createDate	string	时间
     * id	number	时间id
     * isDelete	number	无
     * relevanceId	number	用户id
     * type	number	类型
     * uid	number	用户id
     * url	string	图片url
     */
    private int uid;
    private int isDelete;
    private int id;
    private int relevanceId;
    private int type;
    private String url;
    private String createDate;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRelevanceId(int relevanceId) {
        this.relevanceId = relevanceId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getUid() {
        return uid;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public int getId() {
        return id;
    }

    public int getRelevanceId() {
        return relevanceId;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getCreateDate() {
        return createDate;
    }
}
