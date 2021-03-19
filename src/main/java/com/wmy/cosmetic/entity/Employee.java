package com.wmy.cosmetic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    private int id;
    private String uuid;//职员编号
    private String username;//账号
    private String password;//密码
    private String name;//姓名
    private String sex;//性别
    private String  position;//职位
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date entrytime;//入职时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resignationtime;//离职时间
    private double salary;//薪水
    private String avatar;//相片地址
    private String salt;//随机盐
    private String phone;//电话
    private String status;//任职状态

    //定义角色集合
    private List<Role> roles;
    //定义权限集合
}
