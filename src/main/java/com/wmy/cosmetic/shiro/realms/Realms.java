package com.wmy.cosmetic.shiro.realms;

import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.service.EmployeeService;
import com.wmy.cosmetic.utils.ApplicationContextUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Realms extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取身份信息
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        //根据主身份信息获取角色和权限信息
        EmployeeService employeeService = (EmployeeService) ApplicationContextUtils.getBean("employeeService");
        Employee employee = employeeService.findRolesByEmployeeName(primaryPrincipal);
        //授予角色信息
        if(!ObjectUtils.isEmpty(employee.getRoles())){
            SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
            employee.getRoles().forEach(role -> {
                info.addRole(role.getName());
            });
            return info;
        }
        return null;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        EmployeeService employeeService = (EmployeeService) ApplicationContextUtils.getBean("employeeService");
        Employee employee = employeeService.login(username);
        if (ObjectUtils.isEmpty(employee)){
            return null;
        }
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user",employee);
        return new SimpleAuthenticationInfo(employee.getUsername(),employee.getPassword(), ByteSource.Util.bytes(employee.getSalt()),this.getName());
    }
}
