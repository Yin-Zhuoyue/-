package com.yzy.controller;

/**
 * 返回给前端的结果类
 */
public class JsonResult {
    private Object data;
    private Integer code;
    private String msg;

    public JsonResult() { }

    public JsonResult(Integer code, Object data, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(Integer code, Object data) {
        this.data = data;
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
