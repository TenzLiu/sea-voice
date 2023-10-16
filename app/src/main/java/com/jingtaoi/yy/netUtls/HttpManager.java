package com.jingtaoi.yy.netUtls;


import android.util.Log;

import com.jingtaoi.yy.base.MyApplication;
import com.jingtaoi.yy.utils.AtsUtils;
import com.jingtaoi.yy.utils.Const;
import com.jingtaoi.yy.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingtaoi.yy.bean.BaseBean;
import com.jingtaoi.yy.utils.LogUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.sinata.util.DES;
import cn.sinata.xldutils.utils.StringUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 网络请求管理类
 * Created by Administrator on 2018/1/25.
 */

public class HttpManager {
    private static HttpManager httpManager;
    private Gson gson;
    private Map<String, String> headerParams;
    private HashMap<String, Object> map;

    public static synchronized HttpManager getInstance() {
        if (httpManager == null) synchronized (HttpManager.class) {
            if (httpManager == null) {
                httpManager = new HttpManager();
            }
        }
        return httpManager;
    }


    /**
     * 初始化请求头，具体情况根据需求设置
     */
    public void initHeader() {
        headerParams = new HashMap<>();
        map = new HashMap<>();
        // 传参方式为json
        headerParams.put("Content-Type", "application/json");
    }

    public HashMap<String, Object> getMap() {
        if (map == null) {
            map = new HashMap<>();
        }
        map.clear();
        return map;
    }

    /**
     * 初始化数据
     *
     * @param action 当前请求的尾址
     */
    private Retrofit initBaseData(final String action) {
        initHeader();
        // 监听请求条件
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                LogUtils.i("zzz", "request====1" + action);
                LogUtils.i("zzz", "request====2" + request.headers().toString());
                LogUtils.i("zzz接口信息", "request====3" + request.toString());
                okhttp3.Response proceed = chain.proceed(request);
//                LogUtils.i("zzz", "proceed====4" + proceed.headers().toString());
                return proceed;
            }
        });

        Retrofit.Builder builder1 = new Retrofit.Builder()
                .client(builder.build())                                    // 配置监听请求
                .addConverterFactory(GsonConverterFactory.create())         // 请求结果转换（当前为GSON）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()); // 请求接受工具（当前为RxJava2）
//        builder1.baseUrl(BASE_URL + action.substring(0, action.lastIndexOf("/") + 1));
        builder1.baseUrl(Const.BASE_URL);

        return builder1.build();
    }

    /**
     * Get请求
     *
     * @param action   请求接口的尾址
     * @param params   请求参数
     * @param observer observer
     */
    public <T extends BaseBean> void get(final String action,
                                         Map<String, Object> params, Observer<ResponseBody> observer) {
        if (params == null) {
            params = new HashMap<>();
        }
        String apptokenid = (String) SharedPreferenceUtils.get(MyApplication.getInstance(), Const.User.APP_TOKEN, "");
        if (!StringUtils.isEmpty(apptokenid)) {
            params.put("apptokenid", apptokenid);//用户token
        }
        if (gson == null) {
            gson = new Gson();
        }

        ApiServics getService = initBaseData(action).create(ApiServics.class);
        Log.i("zzz", "request====" + new JSONObject(params));

//        getService.getResult(action.substring(action.lastIndexOf("/") + 1), headerParams, params)
        getService.getResult(action, headerParams, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


//    /**
//     * Post请求 未加密
//     *
//     * @param action 请求接口的尾址
//     * @param params 请求参数
//     */
//    public <T extends BaseBean> void post(final String action, Map<String, Object> params,
//                                          Observer<ResponseBody> observer) {
//        if (params == null) {
//            params = new HashMap<>();
//        }
//        LogUtils.e("msgMap参数信息", params.toString());
//        String apptokenid = (String) SharedPreferenceUtils.get(MyApplication.getInstance(), Const.User.APP_TOKEN, "");
//        if (!StringUtils.isEmpty(apptokenid)) {
//            params.put("apptokenid", apptokenid);//用户token
//        }
//        ApiServics jsonService = initBaseData(action).create(ApiServics.class);
////        LogUtils.d("post", params.toString());
//        jsonService.postResult(action, params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }

    /**
     * Post请求  加密成get请求
     *
     * @param action 请求接口的尾址
     * @param params 请求参数
     */
    public <T extends BaseBean> void post(final String action, Map<String, Object> params,
                                          Observer<ResponseBody> observer) {
        if (params == null) {
            params = new HashMap<>();
        }
        String apptokenid = (String) SharedPreferenceUtils.get(MyApplication.getInstance(), Const.User.APP_TOKEN, "");
        if (!StringUtils.isEmpty(apptokenid)) {
            params.put("apptokenid", apptokenid);//用户token
        }
        try {
            params.put("codon", DES.encryptDES("10086").replace("+", URLEncoder.encode("+","UTF-8")));//app访问请求
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LogUtils.i("msgMap参数信息", params.toString());

        StringBuilder paramsStr = new StringBuilder();
        paramsStr.append("server=apis/").append(action);

        for (Map.Entry<String, Object> paramsNow : params.entrySet()) {
            paramsStr.append("&");
            paramsStr.append(paramsNow.getKey());
            paramsStr.append("=");
            paramsStr.append(paramsNow.getValue());
        }

        HashMap<String, Object> keyParams = getMap();
        LogUtils.i("key", paramsStr.toString());
        try {
            String replace = DES.encryptDES(paramsStr.toString());
            keyParams.put("key", AtsUtils.jiami(replace));
            LogUtils.i("key1", replace);
//            LogUtils.e("key2解密" + DES.decryptDES(replace));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("机密解密出错" + e.getMessage());
        }
        ApiServics getService = initBaseData(action).create(ApiServics.class);
        Log.i("zzz", "request====" + new JSONObject(params));

//        getService.getResult("shenhai.server", headerParams, keyParams)
        getService.getResult("three.server", headerParams, keyParams)//debug
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


//    /**
//     * Post请求
//     *
//     * @param action 请求接口的尾址
//     * @param params 请求参数
//     */
//    public <T extends BaseBean> void post(final String action, Map<String, Object> params,
//                                         Observer<ResponseBody> observer) {
//      if (params == null) {
//           params = new HashMap<>();
//       }
//      LogUtils.e("msgMap参数信息", params.toString());
//
//        ApiServics jsonService = initBaseData(action).create(ApiServics.class);
//        LogUtils.d("post", params.toString());
//
//       jsonService.postResult(action, params)
//                .subscribeOn(Schedulers.io())
//               .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//   }


    /**
     * Post请求
     *
     * @param action   请求接口的尾址
     * @param fileName 文件名
     */
    public <T extends BaseBean> void post(final String action, File fileName,
                                          Observer<ResponseBody> observer) {


        ApiServics service = initBaseData(action).create(ApiServics.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = fileName;

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        service.upload(action, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    /**
     * 将json数据转换为map集合
     *
     * @param gsonStr
     * @return
     */
    public Map<String, Object> gsonToMap(String gsonStr) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = gson.fromJson(gsonStr, type);
        return map;
    }


    /**
     * 将map集合转为json
     *
     * @param map
     * @return
     */
    public String mapToJson(Map<String, Object> map) {
        String tab_str = "";
        String json = tab_str + "{";
        int i = 0;
        for (String key : map.keySet()) {
            if (i >= map.size())
                break;
            String content = "";
            try {
                List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(key);
                content += "[";
                int j = 0;
                for (Map<String, Object> map2 : list) {
                    if (j == list.size())
                        break;
                    content += mapToJson(map2) + (j++ == list.size() - 1 ? "" : ",");
                }
                content += tab_str + "]"/* + (i == (map.size() - 1) ? "" : ",")*/;
            } catch (Exception e) {
                content = "\"" + map.get(key).toString() + "\"";
            }
            json += tab_str + "\"" + key + "\":" + content + (i++ == map.size() - 1 ? "" : ",");
        }
        json += tab_str + "}";
        return json;
    }

}
