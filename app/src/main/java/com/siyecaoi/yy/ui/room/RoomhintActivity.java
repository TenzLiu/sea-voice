package com.siyecaoi.yy.ui.room;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.siyecaoi.yy.R;
import com.siyecaoi.yy.base.MyBaseActivity;
import com.siyecaoi.yy.bean.BaseBean;
import com.siyecaoi.yy.netUtls.Api;
import com.siyecaoi.yy.netUtls.HttpManager;
import com.siyecaoi.yy.netUtls.MyObserver;
import com.siyecaoi.yy.utils.ActivityCollector;
import com.siyecaoi.yy.utils.Const;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 设置进入房间提示
 */
public class RoomhintActivity extends MyBaseActivity {
    @BindView(R.id.edt_show_roomhint)
    EditText edtShowRoomhint;
    String roomHint, roomId;

    @Override
    public void initData() {
        roomHint = getBundleString(Const.ShowIntent.DATA);
        roomId = getBundleString(Const.ShowIntent.ROOMID);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_sethint);
    }

    @Override
    public void initView() {
        setTitleText(R.string.title_roomhint);
        setRightText(R.string.tv_save);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCall();
            }
        });
        edtShowRoomhint.setText(roomHint);
    }

    private void getCall() {
        showDialog();
        roomHint = edtShowRoomhint.getText().toString();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", userToken);
        map.put("roomHint", roomHint);
        HttpManager.getInstance().post(Api.getUpRoom, map, new MyObserver(this) {
            @Override
            public void success(String responseString) {
                BaseBean baseBean = JSON.parseObject(responseString, BaseBean.class);
                if (baseBean.getCode() == 0) {
                    showToast(getString(R.string.hint_save_topic));
                    ActivityCollector.getActivityCollector().finishActivity();
                } else {
                    showToast(baseBean.getMsg());
                }
            }
        });
    }

    @Override
    public void setResume() {

    }

    @Override
    public void setOnclick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
