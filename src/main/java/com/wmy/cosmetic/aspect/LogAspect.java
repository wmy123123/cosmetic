package com.wmy.cosmetic.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(* com.wmy.cosmetic.web.*.*(..))")
    public void log() {}
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String now= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args, now);
        logger.info(String.format( "\n用户[%s]在[%s]访问了[%s]方法路径为[%s]请求参数[%s]", requestLog.getIp(),requestLog.getNow(),requestLog.getClassMethod(),requestLog.getUrl(), Arrays.toString(requestLog.getArgs())));
    }

    @After("log()")
    public void doAfter() {

    }

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }



    /**
     * 封装一个日志对象
     */
    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;
        private String now;

        public RequestLog(String url, String ip, String classMethod, Object[] args, String now) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
            this.now = now;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getClassMethod() {
            return classMethod;
        }

        public void setClassMethod(String classMethod) {
            this.classMethod = classMethod;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

        public String getNow() {
            return now;
        }

        public void setNow(String now) {
            this.now = now;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    ", now='" + now + '\'' +
                    '}';
        }
    }
}
