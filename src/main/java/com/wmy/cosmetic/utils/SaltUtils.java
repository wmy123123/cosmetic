package com.wmy.cosmetic.utils;

import java.util.Random;

public class SaltUtils {
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789!@#$%^&*()".toCharArray();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }
    public static String getEmployeeNumber(){
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = "1234567890".toCharArray();
        for (int i = 0; i < 4; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            stringBuilder.append(aChar);
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
//        String salt=getSalt(8);
//        System.out.println(salt);
        System.out.println(getEmployeeNumber());
    }
}
