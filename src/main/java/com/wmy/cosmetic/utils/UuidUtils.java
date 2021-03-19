package com.wmy.cosmetic.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UuidUtils {
    //订单编号前缀
    public static final String PREFIX = "COSM";
    //订单编号后缀（核心部分）
    private static long code;
    /**
     * 获取一个UUID值
     * @return UUID值[String]
     */
    public static  String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static void main1(String[] args) {
        System.out.println(getUUID());
    }

    /**
     * 获取多个UUID值
     * @param number 所需个数
     * @return UUID集合
     */
    public List<String> getUUID(Integer number){
        List<String> list = new ArrayList<>();
        while (0 <= (number--)){
            list.add(getUUID());
        }
        return list;
    }

    // 生成订单编号
    public static synchronized String getOrderCode() {
        int cod=0;
        code++;
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        long m = Long.parseLong((str)) * 10000;
        m += code;
        return PREFIX + m;
    }
}