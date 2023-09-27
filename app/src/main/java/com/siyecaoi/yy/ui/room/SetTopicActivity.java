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
import cn.sinata.xldutils.utils.StringUtils;

/**
 * 设置房间话题页面
 */
public class SetTopicActivity extends MyBaseActivity {
    @BindView(R.id.edt_title_topic)
    EditText edtTitleTopic;
    @BindView(R.id.edt_content_topic)
    EditText edtContentTopic;

    String roomTopic, topicCount;//房间话题及内容
    String roomId;

    @Override
    public void initData() {
        roomTopic = getBundleString(Const.ShowIntent.TOPIC);
        topicCount = getBundleString(Const.ShowIntent.DATA);
        roomId = getBundleString(Const.ShowIntent.ROOMID);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_settopic);
    }

    @Override
    public void initView() {


        setTitleText(R.string.title_topic);
        setRightText(R.string.tv_save);
        setRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCall();
            }
        });

        if (!StringUtils.isEmpty(roomTopic)) {
            edtTitleTopic.setText(roomTopic);
        }

        if (!StringUtils.isEmpty(topicCount)) {
            edtContentTopic.setText(topicCount);
        }

    }

    private void getCall() {
        roomTopic = edtTitleTopic.getText().toString();
        topicCount = edtContentTopic.getText().toString();
        if (StringUtils.isEmpty(roomTopic)) {
            showToast(getString(R.string.hint_topic_topic));
            return;
        }

        if (StringUtils.isEmpty(topicCount)) {
            showToast(getString(R.string.hint_content_topic));
            return;
        }
        showDialog();
        HashMap<String, Object> map = HttpManager.getInstance().getMap();
        map.put("rid", roomId);
        map.put("uid", userToken);
        map.put("roomTopic", roomTopic);
        map.put("roomCount", topicCount);
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
