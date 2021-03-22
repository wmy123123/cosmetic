package com.wmy.cosmetic.shiro.realms;

import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.service.EmployeeService;
import com.wmy.cosmetic.utils.ApplicationContextUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ObjectUtils;

import java.util.List;

public class Realms extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取身份信息
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        //根据主身份信息获取角色和权限信息
        EmployeeService employeeService = (EmployeeService) ApplicationContextUtils.getBean("employeeService");
        List<Perm> perms = employeeService.findPermsByEmployeeName(primaryPrincipal);
        //授予角色信息
        if(!ObjectUtils.isEmpty(perms)){
            SimpleAuthorizationInfo info =new SimpleAuthorizationInfo();
            perms.forEach(perm -> {
                info.addStringPermission(perm.getPermission());
//                info.addRole(perm.getPermission());
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
