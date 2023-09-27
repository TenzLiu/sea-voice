package cn.sinata.xldutils.net.utils;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import cn.sinata.xldutils.activitys.BaseActivity;
import cn.sinata.xldutils.bean.ResultData;
import cn.sinata.xldutils.fragment.BaseFragment;
import cn.sinata.xldutils.utils.Toast;
import cn.sinata.xldutils.utils.Utils;
import retrofit2.HttpException;

/**
 *
 */
public abstract class ResultDataSubscriber<T> extends ResultSubscriber<ResultData<T>> {


    public ResultDataSubscriber(BaseActivity activity) {
        super(activity);

    }

    public ResultDataSubscriber(BaseFragment fragment) {
        super(fragment);

    }

    @Override
    public void onNext(ResultData<T> tResultData) {
        super.onNext(tResultData);
        Utils.systemErr(tResultData);
        if (tResultData.getResult_code() == 1) {
            onSuccess(tResultData.getMessage(), tResultData.getData());
        } else {
            onError(new ResultException(tResultData.getResult_code(), tResultData.getMessage()));
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        int code = -1;
        String msg = Error.REQUEST_ERROR;
        if (e instanceof JsonSyntaxException ||
                e instanceof NumberFormatException ||
                e instanceof IllegalStateException) {
            msg = Error.PARSER_ERROR;
        } else if (e instanceof HttpException) {
            msg = Error.SERVER_ERROR;
        } else if (e instanceof ConnectException) {
            msg = Error.NET_ERROR;
        } else if (e instanceof SocketTimeoutException) {
            msg = Error.NET_TIMEOUT;
        } else if (e instanceof ResultException) {
            code = ((ResultException) e).getCode();
            msg = e.getMessage();
        }
        onError(code, msg);

    }

    protected void onError(int code, String msg) {
        if (shouldShowErrorToast()) {
            showToast(msg);
        }
    }

    public abstract void onSuccess(String msg, T t);

    private void showToast(String msg) {
        if (activities != null) {
            BaseActivity activity = activities.get();
            if (activity != null) {
                Toast.create(activity).show(msg);
            }
        }
        if (fragments != null) {
            BaseFragment fragment = fragments.get();
            if (fragment != null) {
                Toast.create(fragment).show(msg);
            }
        }

    }

    protected boolean shouldShowErrorToast() {
        return true;
    }
}
