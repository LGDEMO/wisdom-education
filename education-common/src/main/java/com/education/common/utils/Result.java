package com.education.common.utils;


/**
 * @descript: 返回结果类
 * @Auther: zengjintao
 * @Date: 2019/12/31 13:46
 * @Version:2.1.0
 */
public final class Result<T> {

    private T data;
    private int code;
    private static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    private static final String DEFAULT_FAIL_MESSAGE = "操作失败";
    private String message = DEFAULT_SUCCESS_MESSAGE;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.code == ResultCode.SUCCESS;
    }

    public Result() {
        this.code = ResultCode.SUCCESS;
        this.message = DEFAULT_SUCCESS_MESSAGE;
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static <T> Result success(int code, T data) {
        return new Result(code, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result success(T data) {
        return new Result(ResultCode.SUCCESS, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static Result fail(int code) {
        return new Result(code, DEFAULT_FAIL_MESSAGE);
    }

    public static Result fail() {
        return new Result(ResultCode.FAIL, DEFAULT_FAIL_MESSAGE);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
        if (code == ResultCode.FAIL) {
            this.message = "数据请求失败";
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }




}
