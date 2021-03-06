package com.lsxy.app.api.gateway.security;

import com.lsxy.app.api.gateway.security.auth.RestAuthenticationProvider;
import com.lsxy.app.api.gateway.security.auth.SignatureAuthFilter;
import com.lsxy.app.api.gateway.security.ip.BlackIpListFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by Tandy on 2016/6/7.
 */
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(SpringSecurityConfig.class);


    @Autowired
    private RestAuthenticationProvider restAuthenticationProvider;

    public static final String PRIMARY_ACCOUNT = "PRIMARY_ACCOUNT";
//

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("初始化Spring Security安全框架");
        }
        http.csrf().disable();



//        http.requestMatchers().antMatchers("/test/**").


        RequestMatcher apiRM = new AntPathRequestMatcher("/v*/**");

        http
            //管理类API只能主账号调用
            .addFilterBefore(new ManagementFilter(new AntPathRequestMatcher("/v*/account/*/management/**")),UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new SignatureAuthFilter(authenticationManager(),apiRM), ManagementFilter.class)
            .addFilterBefore(new BlackIpListFilter(),SignatureAuthFilter.class)
            .authorizeRequests().requestMatchers(apiRM).authenticated()
            .and().httpBasic()
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    /**
     * 配置校验器
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user001").password("123").roles("TENANT_USER");
        auth.authenticationProvider(restAuthenticationProvider);
    }



}


