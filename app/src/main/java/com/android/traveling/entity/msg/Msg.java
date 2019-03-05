package com.android.traveling.entity.msg;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by HY
 * 2019/1/2 22:08
 *
 * 信息实体类
 *
 */
@SuppressWarnings("unused")
public class Msg {

    public final static int CORRECT_STATUS = 0;
    public final static int ERROR_STATUS = 1;
    public final static int NO_DATA = 2;

    //自定义注解
    @IntDef({ERROR_STATUS, NO_DATA})  //注解仅存在于源码中，在class字节码文件中不包含
    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {}

    private int status;

    private String info;

    public Msg() {
    }

    public Msg(int status, String info) {
        this.status = status;
        this.info = info;
    }

    public static Msg correctMsg(String info) {
        return new Msg(CORRECT_STATUS, info);
    }

    public static Msg errorMsg(String info) {
        return new Msg(ERROR_STATUS, info);
    }

    public int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "status=" + status +
                ", info='" + info + '\'' +
                '}';
    }
}
