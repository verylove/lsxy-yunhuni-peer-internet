package com.lsxy.app.portal.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Tandy on 2016/6/7.
 */
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(SpringSecurityConfig.class);

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("初始化Spring Security安全框架");
        }
        String loginPage = "/login";
        String logoutPage = "/logout";
        String loginFailurePage = "/login?er=true";
        String loginSuccessPage = "/console/index";
        http.authorizeRequests()
                .antMatchers("/console/**").access("hasRole('ROLE_TENANT_USER')")
                .and()
                    //增加自定义的登录校验过滤器
                    .addFilterBefore(new CheckCodeAuthenticationFilter(loginPage,loginFailurePage),UsernamePasswordAuthenticationFilter.class)
                    .formLogin().loginPage(loginPage)
                    .failureUrl(loginFailurePage)
                    .defaultSuccessUrl(loginSuccessPage)
//                .usernameParameter("username").passwordParameter("password")
                .and()
                    .logout().logoutSuccessUrl(loginPage)
                .and()
                    .csrf()
                    //登录登出不用csrf
                    .ignoringAntMatchers(loginPage,logoutPage);
//                .and()
//                    .exceptionHandling().accessDeniedPage("/exception/403");
    }

    /**
     * 配置加密机制,以及加密盐值
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        ReflectionSaltSource rss = new ReflectionSaltSource();
        rss.setUserPropertyToUse("username");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setSaltSource(rss);

        provider.setUserDetailsService(userDetailsService);

        provider.setPasswordEncoder(getPasswordEncode());

        provider.setHideUserNotFoundExceptions(false);

        auth.authenticationProvider(provider);

    }


    /**
     * MD5加密器
     * @return
     */
    private org.springframework.security.authentication.encoding.PasswordEncoder getPasswordEncode() {
        ShaPasswordEncoder encode =  new org.springframework.security.authentication.encoding.ShaPasswordEncoder(256);
        encode.setEncodeHashAsBase64(true);
        return encode;
    }



}


