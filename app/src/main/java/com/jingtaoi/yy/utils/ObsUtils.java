package com.jingtaoi.yy.utils;

import android.annotation.SuppressLint;

import com.obs.services.ObsClient;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 华为云obs，对象存储服务
 */
public class ObsUtils {

    ObsClient obsClient;
    String ak = Const.ak;
    String sk = Const.sk;
    String endPoint = Const.endPoint;
    String bucketname = Const.bucket;

    public ObsUtils() {
        // 创建ObsClient实例
        obsClient = new ObsClient(ak, sk, endPoint);
    }

    @SuppressLint("CheckResult")
    public void uploadFile(String filepath, ObsCallback obsCallback) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                File file = new File(filepath);
                if (!file.exists()) {
                    file = new File(filepath);
                }
                String objectname = UUID.randomUUID().toString();
                PutObjectResult putObjectResult = obsClient.putObject(bucketname, objectname, file);
                emitter.onNext(putObjectResult.getObjectUrl());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.e("obs finish" + s);
                        obsCallback.onUploadFileSuccess(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("obs" + e.toString());
                        obsCallback.onFail(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void downloadFile(String url,String filepath, ObsCallback obsCallback) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                ObsObject obsObject = obsClient.getObject(bucketname, url);

                FileOutputStream fos = null;
                InputStream input = null;
                byte[] buf = new byte[1024];
                int len = 0;
                try {
                    input = obsObject.getObjectContent();
                    File file = new File(filepath);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    while ((len = input.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    emitter.onNext(filepath);
                } catch (IOException e) {
                    emitter.onError(e);
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
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
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.e("obs finish" + s);
                        obsCallback.onUploadFileSuccess(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("obs" + e.toString());
                        obsCallback.onFail(e.toString());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void closeObs() {
        try {
            if (obsClient != null) {
                obsClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public interface ObsCallback {
        void onUploadFileSuccess(String url);

        void onUploadMoreFielSuccess();

        void onFail(String message);

    }


}
