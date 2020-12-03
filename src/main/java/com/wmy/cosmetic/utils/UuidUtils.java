package com.wmy.cosmetic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UuidUtils {
    /**
     * 获取一个UUID值
     * @return UUID值[String]
     */
    public static  String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static void main(String[] args) {
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
}