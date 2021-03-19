package com.wmy.cosmetic.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;

public class Result<T>  {

    private int code;
    private String msg;
    private long count;
    private List<T> data;
    private T datas;



    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }


    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static String success(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",ResultCode.SUCCESS.code());
        jsonObject.put("msg",ResultCode.SUCCESS.message());
        return jsonObject.toString();
    }
    public static String success(ResultCode resultCode,Object data){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",resultCode.code());
        jsonObject.put("msg",resultCode.message());
        jsonObject.put("data",data);
        return jsonObject.toString();
    }
    public static String success(ResultCode resultCode,long count,Object data){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",resultCode.code());
        jsonObject.put("msg",resultCode.message());
        jsonObject.put("count",count);
        jsonObject.put("data",data);
        return jsonObject.toString();
    }
    public static String failure(ResultCode resultCode){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",resultCode.code());
        jsonObject.put("msg",resultCode.message());
        return jsonObject.toString();
    }

    public static String failure(ResultCode resultCode,Object data){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",resultCode.code());
        jsonObject.put("message",resultCode.message());
        jsonObject.put("msg",data);
        return jsonObject.toString();
    }
}
