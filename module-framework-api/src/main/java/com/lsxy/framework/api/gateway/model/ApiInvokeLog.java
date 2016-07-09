package com.lsxy.framework.api.gateway.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.tenant.model.Account;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/4.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_api_log")
public class ApiInvokeLog extends IdEntity{
    private String uri;
    private String appid;
    private String certid;

    private String signature;
    private String method;
    private String body;
    private String contentType;

    @Column(name="api_uri")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Column(name="app_id")
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @Column(name="cert_id")
    public String getCertid() {
        return certid;
    }

    public void setCertid(String certid) {
        this.certid = certid;
    }

    @Column(name="signature")
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Column(name="method")
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Column(name="body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Column(name="content_type")
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
