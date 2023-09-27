package cn.sinata.xldutils;

import android.app.Application;

/**
 *
 *
 */
public abstract class BaseApplication extends Application{

    protected abstract String setSharedPreferencesName();

    @Override
    public void onCreate() {
        super.onCreate();
        xldUtils.init(getApplicationContext(), setSharedPreferencesName());
    }

}
