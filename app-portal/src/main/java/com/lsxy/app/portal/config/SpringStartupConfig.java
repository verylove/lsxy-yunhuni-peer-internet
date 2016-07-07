package com.lsxy.app.portal.config;

import com.lsxy.app.portal.security.SpringSecurityConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Tandy on 2016/6/7.
 */
@ComponentScan("com.lsxy.app.portal")
@EnableWebMvc
@Configuration
@Import({SpringSecurityConfig.class, FrameworkCacheConfig.class})
public class SpringStartupConfig {

}
