package cn.sinata.xldutils.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import cn.sinata.xldutils.R;
import cn.sinata.xldutils.utils.BitmapUtils;
import cn.sinata.xldutils.utils.DensityUtil;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.utils.Utils;
import cn.sinata.xldutils.view.ClipImageBorderView;
import cn.sinata.xldutils.view.ClipZoomImageView;
import cn.sinata.xldutils.xldUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 裁剪图片页面
 */
public class CropImageActivity extends TitleActivity implements Callback{

    private ClipZoomImageView mZoomImageView;
    private String url;
    private Handler handler;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropimage_layout);
        initView();
    }

    protected void initView() {

        handler=new Handler(this);

        //比例
        float scale=getIntent().getFloatExtra("scale", 1f);
        //裁剪模式
        int mode=getIntent().getIntExtra("mode", 0);

        setTitle("裁剪图片");
        addRightButton("确定", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!Utils.ExistSDCard()) {
                    Toast.create(mContext).show("未检测到SD卡！请安装后重试！");
                    return;
                }
                if (Utils.getSDFreeSize() < 10) {
                    Toast.create(mContext).show("SD卡剩余空间太小！");
                    return;
                }
                final Bitmap bitmap = mZoomImageView.clip();
                url = xldUtils.PICDIR + System.currentTimeMillis() + ".jpg";
                showDialog("图片处理中...");
                if (bitmap != null) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            saveBitmapToSDCard(bitmap);
                            handler.sendEmptyMessage(0);
                        }

                    }.start();
                }
            }
        });

        RelativeLayout rl_content=bind(R.id.rl_content);

        mZoomImageView = new ClipZoomImageView(this);
        ClipImageBorderView mClipImageView = new ClipImageBorderView(this);
        int mHorizontalPadding ;
        if (scale<1f) {
            mHorizontalPadding= DensityUtil.dip2px(this, 5);
        }else {
            mHorizontalPadding=DensityUtil.dip2px(this, 10);
        }
        //如果mode为1,设置展示裁剪区域为圆形
        if (mode==1) {
            mClipImageView.setMode(ClipImageBorderView.Mode.Circle);
        }

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        rl_content.addView(mClipImageView, lp);
        rl_content.addView(mZoomImageView, 0, lp);

        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
        mZoomImageView.setImageScale(scale);
        mClipImageView.setImageScale(scale);
        String uri=getIntent().getStringExtra("uri");

        if (uri!=null){
            bitmap = BitmapUtils.decodeBitmapFromPath(this,uri);
            if (bitmap == null){
                Toast.create(this).show("无法打开图片，请检查是否开启读取权限！");
            }
            mZoomImageView.setImageBitmap(bitmap);
        }
    }

    private void saveBitmapToSDCard(Bitmap bmp){

        String path=xldUtils.PICDIR;
        File file = new File(path);
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        file=new File(url);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        dismissDialog();
        Intent intent=new Intent();
        intent.putExtra("path", url);
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap!=null){
            bitmap.recycle();
        }
    }
}
