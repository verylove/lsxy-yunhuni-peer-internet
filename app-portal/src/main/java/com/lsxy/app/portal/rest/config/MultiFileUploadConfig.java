package com.lsxy.app.portal.rest.config;

import com.lsxy.app.portal.rest.console.test.upload.UploadCommonsMultipartResolver;
import com.lsxy.app.portal.rest.security.SpringSecurityConfig;
import com.lsxy.framework.config.SystemConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by zhangxb on 2016/6/28.
 * 多文件上传配置类
 */
@Configuration
public class MultiFileUploadConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new UploadCommonsMultipartResolver();
        int size = Integer.valueOf( SystemConfig.getProperty("portal.realauth.resource.upload.maxsize"));
        if(size>0) {
            resolver.setMaxUploadSize(size*1024*1024);//此处单位是b,1024*1024=1M
        }
        return resolver;
    }
}
