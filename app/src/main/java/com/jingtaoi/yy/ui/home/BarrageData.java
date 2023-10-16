package com.jingtaoi.yy.ui.home;

import com.orient.tea.barragephoto.model.DataSource;

/**
 * 弹幕数据
 *
 * Created by wangjie on 2019/3/22.
 */

@SuppressWarnings("ALL")
public class BarrageData implements DataSource {

    private  String rid;
    private String content;
    private String imgTx;
    private int type;
    private int pos;

    public BarrageData(String content, int type, int pos,String imgTx) {
        this.content = content;
        this.type = type;
        this.pos = pos;
        this.imgTx=imgTx;
    }

    public BarrageData(String content, int type, int pos, String imgTx, String rid) {
        this.content = content;
        this.type = type;
        this.imgTx=imgTx;
        this.pos = pos;
        this.rid=rid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getType() {
        return type;
    }

    public String getImgTx() {
        return imgTx;
    }

    public void setImgTx(String imgTx) {
        this.imgTx = imgTx;
    }
}
