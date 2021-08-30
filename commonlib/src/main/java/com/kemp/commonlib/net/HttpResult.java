package com.kemp.commonlib.net;

public class HttpResult<T> {
    public int Status; // 状态（1请求失败；-1验证失败，2请求成功）
    public String Message; // 请求附加信息
    public String Method;
    public T Data;

    public final static int SUCCESS = 2;

    public boolean isSuccess() {
        return Status == SUCCESS;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "Status=" + Status +
                ", Message='" + Message + '\'' +
                ", Method='" + Method + '\'' +
                ", Data=" + Data +
                '}';
    }
}
