package cn.sinata.xldutils.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import cn.sinata.xldutils.xldUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 *
 */
public class BitmapUtils {

    public static File compressImageFile(String filePath) {
        FileOutputStream fos=null;
        Bitmap tempBitmap = null;
        String path= xldUtils.PICDIR+filePath.hashCode()+".jpg";
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            float scale = 1;
            int min=Math.min(options.outWidth, options.outHeight);
            int max=Math.max(options.outWidth, options.outHeight);
            if(min>=800){
                scale = max/800f;
            }else if(max>=1200){
                scale = max/1200f;
            }
            int sampleSize=new BigDecimal(scale).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = false;
            options.inSampleSize = sampleSize;
            options.inPurgeable = true;
            options.inInputShareable = true;

            tempBitmap = BitmapFactory.decodeFile(filePath,
                    options);
            int degree=readPictureDegree(filePath);
            tempBitmap=adjustPhotoRotation(tempBitmap, degree);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            File file=new File(xldUtils.PICDIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            fos=new FileOutputStream(path);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }catch (OutOfMemoryError e) {
            java.lang.System.gc();
            e.printStackTrace();
        }finally{
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (tempBitmap!=null) {
                tempBitmap.recycle();
            }
        }
        return new File(path);
    }
    /**
     * 旋转图片
     * @param bm bitmap
     * @param orientationDegree 角度
     * @return bitmap
     */
    private static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree){

        Matrix matrix = new Matrix();
        matrix.postRotate(orientationDegree);
        return Bitmap.createBitmap(bm, 0, 0,bm.getWidth(), bm.getHeight(), matrix, true);
    }
    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path
     *            照片路径
     * @return 角度
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

    public static Bitmap decodeBitmapFromPath(Context context,String filePath){
        int[] maxSize = calculateMaxBitmapSize(context);
        int maxWidth = maxSize[0];
        int maxHeight = maxSize[1];
        return decodeBitmapFromPath(context,filePath,maxWidth,maxHeight);
    }

    @SuppressWarnings({"SuspiciousNameCombination", "deprecation"})
    private static int[] calculateMaxBitmapSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        int width, height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }
        return new int[]{width,height};
    }

    public static Bitmap decodeBitmapFromPath(Context context,String filePath,int maxWidth,int maxHeight) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            int sampleSize=calculateBitmapSampleSize(context,file,maxHeight,maxWidth);
            is = new FileInputStream(file);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = sampleSize;
            option.inPreferredConfig = Bitmap.Config.RGB_565;
            option.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(is, null, option);
            int degree = readPictureDegree(filePath);
            bitmap = adjustPhotoRotation(bitmap,degree);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
//            java.lang.System.gc();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if (is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    private static int calculateBitmapSampleSize(Context context, File file,int maxHeight,int maxWidth) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = new FileInputStream(file);
            BitmapFactory.decodeStream(is, null, options); // Just get image size
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable t) {
                    // Do nothing
                }
            }
        }
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxHeight || options.outWidth / sampleSize > maxWidth) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }
}
