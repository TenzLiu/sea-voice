package cn.sinata.util;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {

    public static final String CHAR_SET = "utf-8";

    private static byte[] iv = {15, 24, 39, 41, 55, 68, 53, 94};
    //密钥
    private static byte[] skey = "ForUApp=".getBytes();

    @SuppressWarnings("JniMissingFunction")
    public static native byte[] getKeyValue();

    @SuppressWarnings("JniMissingFunction")
    public static native byte[] getIv();

//    static {
//        System.loadLibrary("security");
////        skey = getKeyValue();
////        iv = getIv();
//    }

    @SuppressLint("TrulyRandom")
    /**
     * 加密
     * @param encryptString 待加密字�?
     * @return 加密后字符串
     */
    public static String encryptDES(String encryptString) {

        IvParameterSpec zeroIv = new IvParameterSpec(iv);

        SecretKeySpec key = new SecretKeySpec(skey, "DES");

        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

            return Base64DES.encode(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return encryptString;
    }

    /**
     * 默认密钥解密
     * @param decryptString		密文
     * @return					解密得到的原文
     * @throws Exception
     */
    public static String decryptDES(String decryptString)
            throws Exception {

        byte[] byteMi = Base64DES.decode(decryptString);

        IvParameterSpec zeroIv = new IvParameterSpec(iv);

        SecretKeySpec key = new SecretKeySpec(skey, "DES");

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);

        byte decryptedData[] = cipher.doFinal(byteMi);

        return new String(decryptedData, "UTF-8");

    }

//    /**
//     * DES解密
//     *
//     * @param decryptString 待解密字符
//     * @return 解密后字符串
//     */
//    public static String decryptDES(String decryptString) {
//        byte[] byteMi = Base64DES.decode(decryptString);
//
//        IvParameterSpec zeroIv = new IvParameterSpec(iv);
//
//        SecretKeySpec key = new SecretKeySpec(skey, "DES");
//
//        try {
//            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
//
//            byte decryptedData[] = cipher.doFinal(byteMi);
//
//            return new String(decryptedData, CHAR_SET);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        }
//
//        return decryptString;
//    }

    @SuppressLint("DefaultLocale")
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}
