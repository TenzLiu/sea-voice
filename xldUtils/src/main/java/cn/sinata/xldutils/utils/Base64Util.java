package cn.sinata.xldutils.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * base64编码文件
 * @author Administrator
 *
 */
public class Base64Util {
	
	public static String encode(String filename) throws Exception {
		File file = new File(filename);

		InputStream in = null;
		byte[] data = null;

		try {
			in = new FileInputStream(file);
			data = new byte[in.available()];

			in.read(data);
			in.close();
		} catch (Exception e) {
			if (in != null) {
				in.close();
			}

			throw e;
		}
		return Base64.encodeToString(data, Base64.DEFAULT);
	}

	//encode图片文件
	@SuppressWarnings("deprecation")
	public static String encodeImageFile(String filename) {
		byte[] data = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filename, options);
			float scale = 1;
			int min=Math.min(options.outWidth, options.outHeight);
			int max=Math.max(options.outWidth, options.outHeight);
			if(min>=800){
				scale = max/800f;
			}else if(max>=1200){
				scale = max/1200f;
			}
			int samplesize=new BigDecimal(scale).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inJustDecodeBounds = false;
			options.inSampleSize = samplesize;
			options.inPurgeable = true;
			options.inInputShareable = true;
			Bitmap tembitmap = null;
			tembitmap = BitmapFactory.decodeFile(filename,
					options);
			int degree=readPictureDegree(filename);
			tembitmap=adjustPhotoRotation(tembitmap, degree);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			tembitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
			data = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}catch (OutOfMemoryError e) {
			System.gc();
			e.printStackTrace();
		}
		return Base64.encodeToString(data, Base64.DEFAULT);
	}
	
	/**
	 * 旋转图片
	 * @param bm
	 * @param orientationDegree 角度
	 * @return
	 */
	private static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree){

		Matrix matrix = new Matrix();;  
        matrix.postRotate(orientationDegree);  
        // 创建新的图片  
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0,  
        		bm.getWidth(), bm.getHeight(), matrix, true);  
        return resizedBitmap;
	}
	
	/** 
  * 读取照片exif信息中的旋转角度 
  * @param path 照片路径 
  * @return角度 
  */  
 private static int readPictureDegree(String path) {  
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree; 
 } 
	
	public static String encodeImageBitmap(Bitmap bmp) throws Exception {
		byte[] data = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			data = baos.toByteArray();
		} catch (Exception e) {
			throw e;
		}catch (OutOfMemoryError e) {
			System.gc();
			throw e;
		}
		return Base64.encodeToString(data, Base64.DEFAULT);
	}
	
	public static File decode(String str, String filename) throws Exception {
		File file = new File(filename);
		OutputStream out = null;
		try {
			if (!file.exists()) {
				new File(file.getParent()).mkdirs();

				file.createNewFile();
			}
			byte[] data = Base64.decode(str, Base64.DEFAULT);
			// byte[] data = decoder.decodeBuffer(str);
			for (int i = 0; i < data.length; ++i) {
				if (data[i] < 0) {
					data[i] += 256;
				}
			}

			out = new FileOutputStream(filename);

			out.write(data);
			out.flush();
			out.close();
		} catch (Exception e) {
			if (out != null) {
				out.close();
			}

			file = null;

			throw e;
		}
		return file;
	}
}
