package com.wmy.cosmetic.web;

import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Role;
import com.wmy.cosmetic.service.EmployeeService;
import com.wmy.cosmetic.service.ProductService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/page")
public class PageManagement {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private ProductService productService;
    @GetMapping("/login")
    public String login(){
        if((SecurityUtils.getSubject().isAuthenticated())){
            return "redirect:index";
        }
        return "administrators/login";
    }
    @GetMapping("/regist")
    public String regist(){
        return "administrators/reg";
    }
    @GetMapping("/index")
    public String index(HttpSession session){
        Employee employee = (Employee) session.getAttribute("user");
        if (StringUtils.isEmpty(employee)){
            return "redirect:login";
        }
        return "index";
    }
    @GetMapping("/homepage")
    public String homepage(){
        return "home/console";
    }
    @GetMapping("/memberlist")
    public String memberlist(){
        return "member/list";
    }
    @GetMapping("/administratorsList")
    public String administratorsList(){
        return "administrators/list";
    }
    @GetMapping("/administratorsRole")
    public String administratorsRole(Model model){
        model.addAttribute("roles",employeeService.findAllRole());
        return "administrators/role";
    }
    @GetMapping("/administratorsInfo")
    public String administratorsInfo(){
        return "administrators/info";
    }
    @GetMapping("/administratorsPassword")
    public String administratorsPassword(){
        return "administrators/password";
    }
    @GetMapping("/productsList")
    public String productsList(Model model){
        model.addAttribute("productType", productService.productTypeList());
        return "products/list";
    }
    @GetMapping("/administratorsAddressList")
    public String administratorsAddressList(){
        return "administrators/addressList";
    }
    @GetMapping("/adminform")
    public String adminform(Model model){
        List<Role> allRole = employeeService.findAllRole();
        model.addAttribute("roles",allRole);
        return "administrators/adminform";
    }
    @GetMapping("/addProduct")
    public String addProduct(Model model){
        model.addAttribute("productType", productService.productTypeList());
        return "products/addProduct";
    }
    @GetMapping("orderForm")
    public String orderForm(){
        return "member/orderForm";
    }
    @GetMapping("statNan")
    public String statNan(){
        return "Statistics/statNan";
    }
    @GetMapping("addRole")
    public String addRole(){
        return "roles/addRole";
    }
    @GetMapping("unAuthorize")
    public String unAuthorize(){
        return "error/unAuthorize";
    }
    @GetMapping("updateRole")
    public String updateRole(Role role, Model model){
        model.addAttribute("role",role);
        return "roles/updateRole";
    }

}
