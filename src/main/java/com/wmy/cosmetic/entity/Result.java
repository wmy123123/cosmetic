package com.wmy.cosmetic.entity;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;

public class Result implements Serializable {

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
