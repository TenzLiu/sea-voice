package com.jingtaoi.yy.utils;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 阿里云oss，对象存储服务
 */
public class OSSUtil {

    private String bucketName = Const.bucketName;
    private String endpoint = Const.endpoint;
    private String keyID = Const.accessKeyId;
    private String OSS_KEY = Const.accessKeySecret;
    private Activity mActivity;
    private ArrayList<OSSAsyncTask> mTasks = new ArrayList<>();
    private OSS mOss;
    private ArrayList<String> URLs;

    //总计大小
    private Long TOTAL_SIZE = 0l;
    //已传大小
    private Long CURRENT_SIZE = 0l;
    //总计数目
    private int TOTAL_COUNT;
    //已传数目
    private int CURRENT_COUNT;

    public OSSUtil(Activity activity) {
        mActivity = activity;
        URLs = new ArrayList<>();
        initSDK(mActivity);
    }

    private void initSDK(Context context) {
        OSSPlainTextAKSKCredentialProvider provider = new OSSPlainTextAKSKCredentialProvider(keyID, OSS_KEY);
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setConnectionTimeout(30 * 1000);  // 连接超时，默认15秒
        configuration.setSocketTimeout(30 * 1000);  // socket超时，默认15秒
        configuration.setMaxConcurrentRequest(5);  // 最大并发请求数，默认5个
        configuration.setMaxErrorRetry(2);  // 失败后最大重试次数，默认2次
        mOss = new OSSClient(context, endpoint, provider);
    }

    private void upload(List<String> urls, Boolean isSingle, final OSSUploadCallBack callBack) {
        String objectKey = UUID.randomUUID().toString();
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, urls.get(CURRENT_COUNT));
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            long temp = 0;

            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                CURRENT_SIZE += currentSize - temp;
                temp = currentSize;
                callBack.onSizeProgress(CURRENT_SIZE, TOTAL_SIZE);
            }
        });

        OSSAsyncTask<PutObjectResult> task = mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                mActivity.runOnUiThread(() -> {
                    URLs.add(mOss.presignPublicObjectURL(bucketName, objectKey));
                    if (isSingle) {
                        callBack.onFinish(URLs.get(0));
                        callBack.onCountProgress(++CURRENT_COUNT, TOTAL_COUNT);
                        mTasks.clear();
                    } else {
                        ++CURRENT_COUNT;
                        callBack.onCountProgress(CURRENT_COUNT, TOTAL_COUNT);
                        if (CURRENT_COUNT == urls.size()) {
                            mTasks.clear();
                            callBack.onFinish(URLs);
                        } else
                            upload(urls, isSingle, callBack);

                    }
                });

            }


            @Override
            public void onFailure(PutObjectRequest request, ClientException
                    clientException, ServiceException serviceException) {
                // 请求异常
                if (clientException != null) {
                    clientException.printStackTrace();
                    // 本地异常如网络异常等
                    callBack.onFial("网络异常");
                }
                if (serviceException != null) {
                    serviceException.printStackTrace();
                    // 服务异常
                    callBack.onFial("服务器异常");
                }

            }
        });
        mTasks.add(task);
    }


    /**
     * 单文件上传
     *
     * @param url      the url
     * @param callBack the call back
     */
    public void uploadSingle(String url, OSSUploadCallBack callBack) {
        ArrayList<String> single = new ArrayList<>();
        single.add(url);
        for (String path : single) {
            TOTAL_SIZE += new File(url).length();
        }
        TOTAL_COUNT = single.size();
        upload(single, true, callBack);
    }

    /**
     * 多文件上传
     *
     * @param urls     the urls
     * @param callBack the call back
     */
    public void uploadMulti(List<String> urls, OSSUploadCallBack callBack) {
        for (String path : urls) {
            TOTAL_SIZE += new File(path).length();
        }
        TOTAL_COUNT = urls.size();
        upload(urls, false, callBack);
    }


    /**
     * 单文件下载
     */
    public void downLoadSingle(String url, final String path, OSSdownLoadCallback callback) {
        GetObjectRequest get = new GetObjectRequest(bucketName, url);
        //设置下载进度回调
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                OSSLog.logDebug("getobj_progress: " + currentSize + "  total_size: " + totalSize, false);
                callback.onSizeProgress(currentSize, totalSize);
            }
        });
        OSSAsyncTask task = mOss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                Log.e("zzz", "HelloMoonFragment--onSuccess--");
                // 请求成功
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = result.getObjectContent();
                    File file = new File(path);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    callback.onFinish();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    Log.e("zzz", "HelloMoonFragment--ErrorCode--clientException" + clientExcepion.getMessage());
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    callback.onFailure(clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("zzz", "HelloMoonFragment--ErrorCode--" + serviceException.getErrorCode());
                    Log.e("zzz", "HelloMoonFragment--RequestId--" + serviceException.getRequestId());
                    Log.e("zzz", "HelloMoonFragment--HostId--" + serviceException.getHostId());
                    Log.e("zzz", "HelloMoonFragment--RawMessage--" + serviceException.getRawMessage());
                    callback.onFailure(serviceException.getRawMessage());
                }

            }
        });

    }

    public abstract static class OSSUploadCallBack {
        //单张上传成功
        public void onFinish(String url) {

        }

        //批量上传成功
        public void onFinish(ArrayList<String> urls) {
        }

        //成功文件大小回调
        public void onSizeProgress(Long currentSize, Long totalSize) {
        }

        //成功文件数目回调
        public void onCountProgress(int currentCount, int totalCount) {
        }


        public void onFial(String message) {

        }

    }

    public abstract static class OSSdownLoadCallback {
        //成功
        public void onFinish() {

        }

        //失败
        public void onFailure(String message) {

        }

        //进度回调
        public void onSizeProgress(Long currentSize, Long totalSize) {
        }
    }
}



