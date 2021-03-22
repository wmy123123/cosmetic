package com.wmy.cosmetic.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.service.ServiceImpl.PermissionService;
import com.wmy.cosmetic.shiro.realms.Realms;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class shiroConfig {
    @Autowired(required = false)
    PermissionService permissionService;

    //创建shiroFilter过滤器
    // 下面两个方法对 注解权限起作用有很大的关系，请把这两个方法，放在配置的最上面
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap1 = new LinkedHashMap<>();
        filterMap1.put("perms", new MyFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterMap1);
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /**
         * 添加shiro的内置过滤器
         *           anon:无需认证就可以访问
         *           authc:必须认证才可以访问
         *           user:必须拥有记住我才可以访问
         *           perm:拥有对某个资源的权限才能访问
         *           role:拥有某个角色权限才能使用
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/page/login", "anon");
        filterMap.put("/static/**", "anon");
        filterMap.put("/templates/**", "anon");
        //        filterMap.put("/**", "authc");
        List<Perm> perms = permissionService.permsList();
        perms.forEach(perm -> {
            filterMap.put(perm.getUrl(), "perms[" + perm.getPermission() + "]");
        });
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
//        shiroFilterFactoryBean.setLoginUrl("/user/logout");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/page/unAuthorize");
        return shiroFilterFactoryBean;
    }

    //创建安全管理器
    @Bean
    public SecurityManager securityManager(Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(realm);
        return securityManager;
    }

    //创建自定义realm
    @Bean
    public Realm getRealm() {
        Realms realms = new Realms();
        //修改凭证校验匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置加缪算法为MD5
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //设置散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        realms.setCredentialsMatcher(hashedCredentialsMatcher);
        return realms;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置session过期时间一个月
        sessionManager.setGlobalSessionTimeout(30 * 24 * 3600);
        return sessionManager;
    }

    @Bean
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("webcookie");
        /**
         * HttpOnly标志的引入是为了防止设置了该标志的cookie被JavaScript读取，
         * 但事实证明设置了这种cookie在某些浏览器中却能被JavaScript覆盖，
         * 可被攻击者利用来发动session fixation攻击
         */
        simpleCookie.setHttpOnly(true);
        /**
         * 设置浏览器cookie过期时间，如果不设置默认为-1，表示关闭浏览器即过期
         * cookie的单位为秒 比如60*60为1小时
         */
        simpleCookie.setMaxAge(30 * 24 * 3600);
        return simpleCookie;
    }

    //整合ShiroDialect:用来整合shiro thymleaf
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

    /**
     * 开启aop注解支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
