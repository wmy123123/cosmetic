package com.wmy.cosmetic.web;


import com.github.pagehelper.PageInfo;
import com.google.code.kaptcha.Producer;
import com.wmy.cosmetic.Exception.ServiceException;
import com.wmy.cosmetic.entity.*;
import com.wmy.cosmetic.mapper.EmployeeMapper;
import com.wmy.cosmetic.service.EmployeeService;
import com.wmy.cosmetic.utils.UuidUtils;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/user")
public class EmployeeController {
    private final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    @Value("${project.path.avatarImage2}")
    private String avatarImage;
    @Value("${project.path.domain}")
    private String domain;
    @Autowired
    private EmployeeService employeeService;
    @Autowired(required = false)
    private EmployeeMapper employeeMapper;
    @Autowired
    private Producer producer;


    @ResponseBody
    @PostMapping("/addEmployee")
    public String addEmployee(Employee employee) {
        int result = employeeService.addEmployee(employee);
        if (result != 1) {
            return Result.failure(ResultCode.ERR_ADD_EMPLOYEE);
        } else {
            return Result.success(ResultCode.SUCCESS_ADD_EMPLOYEE, null);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Result<Object> login(String username, String password, String vercode, String remember, HttpSession session) {
        Result<Object> result = new Result<>();
        result.setCode(0);//默认交易成功
        Subject subject = null;
        UsernamePasswordToken token = null;
        try {
//            String kaptcha = (String) session.getAttribute("kaptcha");
//            if (kaptcha.isEmpty() || !kaptcha.equalsIgnoreCase(vercode)) {
//                result.setCode(1);
//                result.setMsg("验证码错误");
//                return result;
//            }
            //获取当前的用户
            subject = SecurityUtils.getSubject();
            token = new UsernamePasswordToken(username, password);
            if (remember != null && remember.equals("on")) {
                token.setRememberMe(true);
            }
            Employee employee = employeeService.login(username);
            if (StringUtils.isEmpty(employee)){
                result.setCode(1);
                result.setMsg("账号不存在");
                return result;
            }
            if (employee.getStatus().equals("0")) {
                subject.login(token);
                result.setCode(0);
                result.setMsg("登入成功");
            } else {
                result.setCode(1);
                result.setMsg("你的账号已被禁用，请联系管理员");
            }
        } catch (UnknownAccountException e) {
            result.setCode(1);
            result.setMsg("账号不存在");
            logger.info("账号不存在");
        } catch (IncorrectCredentialsException e) {
            result.setCode(1);
            result.setMsg("密码错误");
            logger.info("密码错误");
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("系统错误");
        }
        return result;
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/page/login";
    }

    @GetMapping("/kaptcha/{date}")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        String text = producer.createText();
        BufferedImage bufferedImage = producer.createImage(text);
        session.setAttribute("kaptcha", text);
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败" + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/findEmployeeList")
    public String findEmployeeList(@RequestParam(value = "uuid", required = false) Integer uuid,
                                   @RequestParam(value = "position", required = false) String position,
                                   @RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "page", required = false) Integer page,
                                   @RequestParam(value = "limit", required = false) Integer limit) {
        PageInfo<Employee> employees = employeeService.findEmployeeList(uuid, name, position, page, limit);
        if (employees != null) {
            return Result.success(ResultCode.SUCCESS_FIND_EMPLOYEE_lIST, employees.getTotal(), employees.getList());
        } else {
            return Result.failure(ResultCode.ERR_FIND_EMPLOYEE_LIST);
        }
    }

    @GetMapping("/getImage/{filename}")
    public void getImage(@PathVariable("filename") String filename, HttpServletResponse response) {
        filename = avatarImage + "/" + filename;
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream file = new FileInputStream(filename);
            byte[] bytes = new byte[1024];
            int b;
            while ((b = file.read(bytes)) != -1) {
                os.write(bytes, 0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/updateEmplform")
    public String updateEmplform(Integer id, Model model) {
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        Employee empl = employeeService.findEmployeeById(id);
        List<Role> allRole = employeeService.findAllRole();
        if (ObjectUtils.isEmpty(empl)) {
            return null;
        } else {
            model.addAttribute("empl", empl);
            model.addAttribute("roles", allRole);
        }
        return "administrators/updateEmplform";
    }

    @RequestMapping("/updateEmplform1")
    public String updateEmplform1(HttpServletRequest request, Model model, HttpSession session) {
//        request.getSession().invalidate();
        //       Employee employee = (Employee) request.getSession().getAttribute("user");
        Employee empl = (Employee) session.getAttribute("user");
        model.addAttribute("empl", empl);
        return "administrators/info";
    }

    @RequestMapping("/updateEmployee")
    @ResponseBody
    public String updateEmployee(Employee employee) {
        if (ObjectUtils.isEmpty(employee)) {
            return null;
        }
        int i = employeeService.updateEmploy(employee);
        if (i > 0) {
            return Result.success(ResultCode.SUCCESS_UPDATE_EMPLOYEE, null);
        } else {
            return Result.failure(ResultCode.ERR_UPDATE_EMPLOYEE, null);
        }
    }

    @RequestMapping("/updateEmployee1")
    @ResponseBody
    public Result<String> updateEmployee1(Employee employee, HttpSession session) {
        Result<String> result = new Result<>();
        result.setCode(0);//默认交易成功
        try {
            employeeService.updateEmploy1(employee);
            session.setAttribute("user", employeeService.findEmployeeById(employee.getId()));
            result.setMsg("修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteEmployee")
    public String deleteEmployee(Integer id) {
        if (ObjectUtils.isEmpty(id)) {
            return null;
        }
        int result = employeeService.deleteEmployee(id);
        if (result > 0) {
            return Result.success(ResultCode.SUCCESS_DELETE_EMPLOYEE, null);
        } else {
            return Result.failure(ResultCode.ERR_DELETE_EMPLOYEE);
        }
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(String oldPassword, String password, HttpSession session) {
        if (oldPassword != null && password != null) {
            Employee empl = (Employee) session.getAttribute("user");
            oldPassword = new Md5Hash(oldPassword, empl.getSalt(), 1024).toHex();
            Employee emplo = employeeService.findEmployeeById(empl.getId());
            if (emplo.getPassword().equals(oldPassword)) {
                password = new Md5Hash(password, empl.getSalt(), 1024).toHex();
                int result = employeeService.updatePassword(empl.getUuid(), password);
                if (result > 0) {
                    return Result.success(ResultCode.SUCCESS_UPDATE_PASSWORD, null);
                }
                return Result.failure(ResultCode.SUCCESS_UPDATE_PASSWORD);
            }
            return Result.failure(ResultCode.ERR_PASSWORD);
        }
        return null;
    }

    //批量删除员工
    @PostMapping("deleteByEmpId")
    @ResponseBody
    public Result<Employee> deleteByEmpId(@RequestParam(value = "uuids[]") String[] uuids) {
        Result<Employee> result = new Result<>();
        result.setCode(0);//默认交易成功
        try {
            List<String> uuIds = new ArrayList<>();
            for (String uuid : uuids) {
                uuIds.add(uuid);
            }
            employeeService.deleteEmpById(uuIds);
            result.setMsg("批量删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 重置密码
     *
     * @param uuid
     * @return
     */
    @GetMapping("updatePass")
    @ResponseBody
    public Result<Employee> updatePass(String uuid) {
        Result<Employee> result = new Result<>();
        result.setCode(0);
        String pass = "123456";
        try {
            Employee empl = employeeMapper.findEmployeeByUuid(uuid);
            pass = new Md5Hash(pass, empl.getSalt(), 1024).toHex();
            employeeService.updatePassword(uuid, pass);
            result.setMsg("重置密码成功");
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            result.setCount(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @GetMapping("updateStatus")
    @ResponseBody
    public Result<Employee> updateStatus(Integer id, HttpSession session) {
        Result<Employee> result = new Result<>();
        result.setCode(0);
        try {
            Date date = new Date();
            Employee empl = employeeService.findEmployeeById(id);
            if (empl.getStatus().equals("1")) {
                employeeMapper.updateStatus1(empl.getUuid(), date);
                result.setCode(2);
            } else {
                employeeMapper.updateStatus(empl.getUuid(), date);
            }
            result.setMsg("设置成功");
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            result.setCount(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @PostMapping("uploadAvatar")
    @ResponseBody
    public String uploadHeader(@RequestParam(value = "file") MultipartFile headerImage, HttpSession session) throws IOException {
        if (headerImage == null) {
            throw new ServiceException("图片为空");
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if (suffix.isEmpty()) {
            throw new ServiceException("文件格式不正确");
        }
        //生成随机文件名
        filename = UuidUtils.getUUID() + suffix;
        //确定文件存放路径
        File dest = new File(avatarImage + "/" + filename);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常" + e);
        }
        Employee employee = (Employee) session.getAttribute("user");
        String productImgUrl = domain + "/user/getImg/" + filename;
        employeeService.updateAvatarImgPath(employee.getUuid(), productImgUrl);
        return Result.success();
    }

    @GetMapping("getImg/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //服务器存放路径
        fileName = avatarImage + "/" + fileName;
        //文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream file = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = file.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取失败" + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("roleList")
    @ResponseBody
    public Result<Role> roleList(@RequestParam(required = false) Integer rolid,
                                 @RequestParam(required = false, value = "page") Integer pageNumber,
                                 @RequestParam(required = false, value = "limit") Integer limit) {
        Result<Role> result = new Result<>();
        result.setCode(0);
        try {
            Date date = new Date();
            PageInfo<Role> roles = employeeService.roleList(rolid, pageNumber, limit);
            result.setMsg("查询成功");
            result.setCount(roles.getTotal());
            result.setData(roles.getList());
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            result.setCount(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("exportExcel")
    public void ExportExcel(HttpServletResponse response) {
        try {
            employeeService.exportExcel(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }
    }

    @ResponseBody
    @RequestMapping("importExcel")
    public Result<Object> importExcel(MultipartFile file) {
        Result<Object> result = new Result<>();
        result.setCode(0);
        try {
            employeeService.importExcel(file);
            result.setMsg("导入任务创建成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}
