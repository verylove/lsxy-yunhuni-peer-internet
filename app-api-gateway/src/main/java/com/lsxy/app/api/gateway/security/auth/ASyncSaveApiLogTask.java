package com.lsxy.app.api.gateway.security.auth;

import com.lsxy.yunhuni.api.gateway.model.ApiInvokeLog;
import com.lsxy.yunhuni.api.gateway.service.ApiInvokeLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/7.
 */
@Component
public class ASyncSaveApiLogTask {
    private static final Logger logger = LoggerFactory.getLogger(ASyncSaveApiLogTask.class);

    @Autowired
    private ApiInvokeLogService apiInvokeLogService;

    /**
     * 调用日志异步入库
     * @param appid
     * @param payload
     * @param contentType
     * @param method
     * @param signature
     * @param uri
     */
    @Async
    public void invokeApiSaveDB(String appid, String payload, String contentType, String method, String signature, String uri,String tenantId,String certId){
        ApiInvokeLog log = new ApiInvokeLog();
        log.setAppid(appid);
        log.setBody(payload);
        log.setContentType(contentType);
        log.setMethod(method);
        log.setSignature(signature);
        log.setUri(uri);
        log.setCertid(certId);
        log.setTenantId(tenantId);
        //TODO 设置api调用类型
        String[] split = uri.split("/");
        String type;
        if(split.length > 5){
            type = "/" + split[4] + "/" +split[5];
        }else{
            type = uri;
        }
        log.setType(type);
        apiInvokeLogService.save(log);

        if(logger.isDebugEnabled()) {
            logger.debug("调用日志异步入库中完成:{}",log);
        }
    }

    public static void main(String[] args) {
        String uri = "/v1/account/da7ac16cba78993fd942d3ef2beb8549/call/verify_call";
        String[] split = uri.split("/");
        String type = "/" + split[4] + "/" +split[5];
        System.out.println(split.length);
        System.out.println(type);
    }

}