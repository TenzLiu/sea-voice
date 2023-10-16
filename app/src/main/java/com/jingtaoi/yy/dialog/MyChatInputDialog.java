//package com.cityrise.uuvoice.dialog;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.cityrise.uuvoice.R;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
///**
// * 聊天弹框
// * Created by Administrator on 2018/3/9.
// */
//
//public class MyChatInputDialog extends Dialog {
//
//
//    @BindView(R.id.edt_input_mychat)
//    EditText edtInputMychat;
//    @BindView(R.id.btn_send_mychat)
//    Button btnSendMychat;
//    Context mContext;
//
//
//
//    public MyChatInputDialog(Context context) {
//        super(context, R.style.CustomStateDialogStyle);
//        this.mContext = context;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.setCancelable(false);  // 是否可以撤销
//        setContentView(R.layout.dialog_mychatinput);
//        ButterKnife.bind(this);
//        setCanceledOnTouchOutside(true);
//        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        getWindow().setGravity(Gravity.BOTTOM);
//
//        setIsOpen();
//    }
//
//    private void setIsOpen() {
//    }
//
//
//    public String getSendShow() {
//        return edtInputMychat.getText().toString();
//    }
//
//
//    public void setSend(View.OnClickListener onClickListener) {
//        btnSendMychat.setOnClickListener(onClickListener);
//    }
//}