package com.lsxy.app.api.gateway;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.mq.FrameworkMQConfig;
import com.lsxy.framework.mq.api.MQConsumer;
import com.lsxy.framework.web.web.AbstractSpringBootStarter;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import org.hibernate.dialect.MySQL5Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;

/**
 * Created by Tandy on 2016/6/13.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class,
        FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class, FrameworkMQConfig.class})
@EnableAsync
@EnableWebMvc
public class MainClass extends AbstractSpringBootStarter {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(MainClass.class, args);
    }


    @Override
    public String systemId() {
        return "api.gateway";
    }
}
