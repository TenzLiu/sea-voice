package cn.sinata.xldutils.activitys;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.Locale;

import cn.sinata.xldutils.R;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.utils.Utils;
import cn.sinata.xldutils.xldUtils;
import io.reactivex.functions.Consumer;

/**
 * 选择拍照或相册等dialog
 * @author sinata
 *
 */
public class SelectPhotoDialog extends DialogActivity implements OnClickListener{

	private final int CODE_PERMISSION = 10;
	public static final String DATA = "path";
	private File tempFile;//临时文件
//	private String fileName;//图片文件名称

	@Override
	protected int exitAnim() {
		return 0;
	}

	@Override
	protected int setContentLayout() {
		return R.layout.take_photo_popupwindow;
	}
	//初始化视图
	@Override
	protected void initView() {
		getWindow().setGravity(Gravity.BOTTOM);
		RxPermissions permissions = new RxPermissions(this);
		permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
						if (!aBoolean) {
							showToast("请授予访问本地存储的权限！");
							finish();
						}
					}
				});
		Button btnTakePhoto = bind(android.R.id.button1);
		Button btnSelectPhoto = bind(android.R.id.button2);
		Button btnCancel = bind(android.R.id.button3);

		btnTakePhoto.setOnClickListener(this);
		btnSelectPhoto.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK) {
			switch (requestCode) {
				case 0:
					if (tempFile!=null && tempFile.exists()) {
						Intent intent=new Intent();
						intent.putExtra(DATA, tempFile.getAbsolutePath());
						setResult(RESULT_OK,intent);
						finish();
						overridePendingTransition(0, 0);
					}
					
					break;

				case 1:
					if (data!=null) {
						Uri uri = data.getData();
						if (uri != null) {
							String path = Utils.getUrlPath(this, uri);
							if (path != null) {
								int typeIndex = path.lastIndexOf(".");
								if (typeIndex != -1) {
			                        String fileType = path.substring(typeIndex + 1).toLowerCase(Locale.CHINA);
			                      //某些设备选择图片是可以选择一些非图片的文件。然后发送出去或出错。这里简单的通过匹配后缀名来判断是否是图片文件
			                      //如果是图片文件则发送。反之给出提示
			                        if (fileType.equals("jpg") || fileType.equals("gif")
			                                || fileType.equals("png") || fileType.equals("jpeg")
			                                || fileType.equals("bmp") || fileType.equals("wbmp")
			                                || fileType.equals("ico") || fileType.equals("jpe")) {
			                        	Intent intent=new Intent();
			    						intent.putExtra(DATA, path);
			    						setResult(RESULT_OK,intent);
			    						finish();
			    						overridePendingTransition(0, 0);
									}else {
										Toast.create(this).show("无法识别的图片类型！");
									}
								}else {
									Toast.create(this).show("无法识别的图片类型！");
								}
							}else {
								Toast.create(this).show("无法识别的图片类型或路径！");
							}
						}else {
							Toast.create(this).show("无法识别的图片类型！");
						}
					}
					break;
			}
		}
		
	}

//	@Override
//	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//		SPUtils.save(permissions[0],false);
//		if (requestCode == CODE_PERMISSION){
//			if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//					&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
//				//用户同意使用write
//
//			}else{
//				//用户不同意，向用户展示该权限作用
//				if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//					DialogUtils.createNoticeDialog(this, "请注意", "本应用需要使用访问本地存储权限，否则无法正常使用！",false, "确定", "取消", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							finish();
//						}
//					}, new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							finish();
//						}
//					});
//
//					return;
//				}
//				finish();
//			}
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//拍照
		case android.R.id.button1: {
			//检测路径是否存在，不存在就创建
			xldUtils.initFilePath();
			Intent intent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			String fileName = System.currentTimeMillis() + ".jpg";
			tempFile = new File(xldUtils.PICDIR, fileName);
			Uri u = Uri.fromFile(tempFile);
			//7.0崩溃问题
			if (Build.VERSION.SDK_INT<24){
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
			}else {
				ContentValues contentValues = new ContentValues(1);
				contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
				Uri uri =this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			startActivityForResult(intent, 0);
		}
			break;
		// 相册选取
		case android.R.id.button2: {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
			intent.setType("image/*");
			startActivityForResult(intent, 1);
		}
			break;
		//按钮以外区域
//		case R.id.view_outside:
		// 取消
		case android.R.id.button3:
			try {
				//直接调用返回键事件
				onBackPressed();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
}
