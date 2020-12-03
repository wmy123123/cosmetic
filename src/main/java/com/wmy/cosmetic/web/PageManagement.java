package com.wmy.cosmetic.web;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("page")
public class PageManagement {
    @GetMapping("login")
    public String login(){
        if((SecurityUtils.getSubject().isAuthenticated())){
            return "redirect:index";
        }
        return "administrators/login";
    }
    @GetMapping("regist")
    public String regist(){
        return "administrators/reg";
    }
    @GetMapping("index")
    public String index(){
        return "index";
    }
    @GetMapping("homepage")
    public String homepage(){
        return "home/homepage";
    }
    @GetMapping("memberlist")
    public String memberlist(){
        return "member/list";
    }
    @GetMapping("administratorsList")
    public String administratorsList(){
        return "administrators/list";
    }
    @GetMapping("administratorsRole")
    public String administratorsRole(){
        return "administrators/role";
    }
    @GetMapping("administratorsInfo")
    public String administratorsInfo(){
        return "administrators/info";
    }
    @GetMapping("administratorsPassword")
    public String administratorsPassword(){
        return "administrators/password";
    }
    @GetMapping("productsList")
    public String productsList(){
        return "products/list";
    }
    @GetMapping("administratorsAddressList")
    public String administratorsAddressList(){
        return "administrators/addressList";
    }
    @GetMapping("adminform")
    public String adminform(){
        return "administrators/adminform";
    }
}
