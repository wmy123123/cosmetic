package com.wmy.cosmetic.service.ServiceImpl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wmy.cosmetic.callable.EmployeeMessageCallable;
import com.wmy.cosmetic.utils.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class EmployeeMessage {
    private final Logger logger= LoggerFactory.getLogger(EmployeeMessage.class);
    public String  importExcel(MultipartFile file) throws ExecutionException, InterruptedException {
        List<Future<String>> futures = new ArrayList<>();
        ExecutorService executorService = initTheadPool(file.getOriginalFilename());
        EmployeeMessageCallable employeeMessageCallable = (EmployeeMessageCallable) ApplicationContextUtils.getBean("employeeMessageCallable");
        employeeMessageCallable.setFile(file);
        futures.add(executorService.submit(employeeMessageCallable));
        String rs="";
        for (Future<String> future:futures) {
            rs = future.get();
            if (rs.equals(file.getOriginalFilename())){
                logger.info(rs);
            }
        }
        return rs;
    }
    /**
     * 初始化线程池
     */
    public static ExecutorService initTheadPool(String theadName) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(theadName + "-pool-%d").build();
        int nThreads = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),threadFactory,new ThreadPoolExecutor.AbortPolicy());
    }
}
