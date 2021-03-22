package com.wmy.cosmetic.Exception;

import com.alibaba.fastjson.JSONArray;
import com.wmy.cosmetic.entity.Result;
import com.wmy.cosmetic.utils.WebUtilsPro;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public  class ServiceException extends RuntimeException {
//    private static final long serialVersionUID = 8428864816994640969L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * 权限异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public Object authorizationException(HttpServletRequest request, HttpServletResponse response) {
        if (WebUtilsPro.isAjaxRequest(request)) {
            System.out.println("==============");
            // 输出JSON
            Map<String, Object> resp = new HashMap<String, Object>();
            Result result = new Result();
            result.setCode(2);
            result.setMsg("您无此功能权限，请联系管理员");
            resp.put("result",result);
            writeJson(resp, response);
            return null;
        } else {
            return "redirect:/user/logout";
        }
    }

    /**
     * 输出JSON
     */
    private void writeJson(Map<String, Object> resp, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSONArray.toJSONString(resp));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
