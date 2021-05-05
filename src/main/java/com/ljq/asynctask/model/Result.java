package com.ljq.asynctask.model;

/**
 * @author linjunqiang
 * @Date 2021/5/3
 */
public class Result {

    private Integer errno;

    private String errmsg;

    private Object data;


    public Result() {
        this.errno = 0;
        this.errmsg = "success";
        this.data = null;
    }

    public Result(Object data) {
        this.errno = 0;
        this.errmsg = "success";
        this.data = data;
    }

    public Result(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
        this.data = "";
    }

    /**
     * TODO
     */
    public Result(String errmsg) {
        this.errno = 1000;
        this.errmsg = errmsg;
        this.data = "";
    }

    public Result(int errno, String errmsg, Object data) {
        this.errno = errno;
        this.errmsg = errmsg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result(errno=" + this.getErrno() + ", errmsg=" + this.getErrmsg() + ", data=" + this.getData();
    }


    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
