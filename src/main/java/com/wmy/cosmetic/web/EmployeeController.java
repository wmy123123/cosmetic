package com.wmy.cosmetic.web;


import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Producer;
import com.sun.org.apache.bcel.internal.generic.ATHROW;
import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Result;
import com.wmy.cosmetic.entity.ResultCode;
import com.wmy.cosmetic.service.EmployeeService;
import org.apache.ibatis.io.Resources;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;

@Controller
@RequestMapping("user")
public class EmployeeController {
    private final static Logger logger= LoggerFactory.getLogger(EmployeeController.class);
    @Value("${project.path.avatarImage}")
    private String avatarImage;
    @Autowired(required = false)
    private EmployeeService employeeService;
    @Autowired(required = false)
    private Producer producer;


    @ResponseBody
    @PostMapping("addEmployee")
    public String addEmployee(Employee employee){
        int result = employeeService.addEmployee(employee);
        if (result!=1){
            return Result.failure(ResultCode.ERR_ADD_EMPLOYEE);
        }else{
            return Result.success(ResultCode.SUCCESS_ADD_EMPLOYEE,null);
        }
    }

    @PostMapping("login")
    @ResponseBody
    public String login(String username,String password,String vercode,String remember,HttpSession session){
        String kaptcha= (String) session.getAttribute("kaptcha");
        if (kaptcha.isEmpty() || !kaptcha.equalsIgnoreCase(vercode)) {
            return Result.failure(ResultCode.USER_ERROR_VerificationCode);
        }
        //获取当前的用户
        Subject subject= SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        if (remember!=null && remember.equals("on")){
            token.setRememberMe(true);
        }
        //封装用户的登录数据
        try {
            subject.login(token);
            return Result.success();
        } catch (UnknownAccountException e) {
            logger.info("用户名错误");
            return Result.failure(ResultCode.PARAM_NOT_LOGGET_IN);
        } catch (IncorrectCredentialsException e){
            logger.info("密码错误");
            return Result.failure(ResultCode.ERR_LOGIN_PASSWORD);
        }
    }
    @GetMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/page/login";
    }
    @GetMapping("kaptcha/{date}")
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        String text= producer.createText();
        BufferedImage bufferedImage = producer.createImage(text);
        session.setAttribute("kaptcha",text);
        response.setContentType("image/png");
        try {
            OutputStream os=response.getOutputStream();
            ImageIO.write(bufferedImage,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }
    @ResponseBody
    @RequestMapping("findEmployeeList")
    public String findEmployeeList(@RequestParam(value ="uuid",required = false) Integer uuid, String name,int page,int limit){
        PageInfo<Employee> employees = employeeService.findEmployeeList(uuid,name,page,limit);
        if (employees!=null){
            return Result.success(ResultCode.SUCCESS_FIND_EMPLOYEE_lIST,employees.getTotal(),employees.getList());
        }else{
            return Result.failure(ResultCode.ERR_FIND_EMPLOYEE_LIST);
        }
    }
    @GetMapping("getImage/{filename}")
    public void getImage(@PathVariable("filename") String filename, HttpServletResponse response){
        filename= avatarImage+"/"+filename;
        String suffix=filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/"+suffix);
        try {
            OutputStream os=response.getOutputStream();
            FileInputStream file =new FileInputStream(filename);
            byte[] bytes=new byte[1024];
            int b=0;
            while ((b=file.read(bytes))!=-1){
                os.write(bytes,0,b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("updateEmplform")
    public String updateEmplform(Integer id, Model model){
        if (ObjectUtils.isEmpty(id)){
            return null;
        }
        Employee empl = employeeService.findEmployeeById(id);
        if (ObjectUtils.isEmpty(empl)){
            return null;
        }else {
           model.addAttribute("empl",empl);
        }
        return "administrators/updateEmplform";
    }
    @RequestMapping("updateEmployee")
    @ResponseBody
    public String updateEmployee(Employee employee){
        if (ObjectUtils.isEmpty(employee)){
            return null;
        }
        int i = employeeService.updateEmploy(employee);
        if (i>0){
            return Result.success(ResultCode.SUCCESS_UPDATE_EMPLOYEE,null);
        }else {
            return Result.failure(ResultCode.ERR_UPDATE_EMPLOYEE,null);
        }
    }
    @ResponseBody
    @RequestMapping("deleteEmployee")
    public String deleteEmployee(Integer id){
        if (ObjectUtils.isEmpty(id)){
            return null;
        }
        int result = employeeService.deleteEmployee(id);
        if (result>0){
            return Result.success(ResultCode.SUCCESS_DELETE_EMPLOYEE,null);
        }else{
            return  Result.failure(ResultCode.ERR_DELETE_EMPLOYEE);
        }
    }
    @RequestMapping("updatePassword")
    @ResponseBody
    public String updatePassword(String oldPassword,String password,HttpSession session){
        if (oldPassword!=null&&password!=null){
            Employee empl = (Employee) session.getAttribute("user");
            int id=empl.getId();
            password=new Md5Hash(password,empl.getSalt(),1024).toHex();
            Employee emplo = employeeService.findEmployeeById(id);
            if (emplo.getPassword().equals(password)){
                int result = employeeService.updatePassword(id, password);
                if (result>0){
                    return Result.success(ResultCode.SUCCESS_UPDATE_PASSWORD,null);
                }
                return Result.failure(ResultCode.SUCCESS_UPDATE_PASSWORD);
            }
            return Result.failure(ResultCode.ERR_PASSWORD);
        }
        return null;
    }
}
