package cn.sinata.xldutils.bean;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import cn.sinata.util.DES;
import cn.sinata.xldutils.utils.Utils;

/**
 *
 * Created by liaoxiang on 16/6/20.
 */
public class ResultData<T> {

    private T data;
    @SerializedName("msg")
    private String message;
    private int code;

    public T getData() {
        return data;
    }

    public void setResult_data(T result_data) {
        this.data = result_data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResult_code() {
        return code;
    }

    public void setResult_code(int result_code) {
        this.code = result_code;
    }

    public String getDecodeDesString(){
        if (data instanceof String){
            String  s = (String) data;
            String desData = null;
            try {
                desData = TextUtils.isEmpty(s)?"":DES.decryptDES(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String d = "{\"message\":\""+message+"\",\"result_code\":"+data+",\"result_data\":"+desData+"}";
            Utils.systemErr(d);
            return d;
        }
        return "";
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "result_data=" + data +
                ", message='" + message + '\'' +
                ", result_code=" + code +
                '}';
    }
}