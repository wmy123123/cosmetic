package com.wmy.cosmetic.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtilsPro {
    /**
     * 是否是Ajax请求
     *
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType)) {
            System.out.println("----------------"+requestType);
            return true;
        } else {
            return false;
        }
    }
}
