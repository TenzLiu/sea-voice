package cn.sinata.xldutils.net.utils;

public final class ResultException extends Exception {
    private int code;

    public ResultException(String msg) {
        super(msg);
        code = -1;//默认
    }

    public ResultException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ResultException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }


    public ResultException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}