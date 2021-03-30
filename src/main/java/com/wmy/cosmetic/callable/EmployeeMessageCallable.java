package com.wmy.cosmetic.callable;

import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Callable;

@Component
@Scope("prototype")
public class EmployeeMessageCallable implements Callable<String> {
    @Autowired
    private EmployeeService employeeService;
    private MultipartFile file;
    @Override
    public String call() throws Exception {
       String filename= employeeService.importExcel(file);
        return filename;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
