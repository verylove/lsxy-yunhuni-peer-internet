package com.lsxy.yunhuni.api.playvoice.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.yunhuni.api.app.model.App;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by liups on 2016/6/29.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_play_voice_file")
public class PlayVoiceFile extends IdEntity {
    private String url;     //文件URL
    private App app;        //所属APP
    private Double size;    //文件大小
    private Integer status;     //审核状态
    private String checker; //审核人
    private Date checkTime; //审核时间

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ManyToOne
    @JoinColumn(name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Column(name = "size")
    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "checker")
    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    @Column(name = "check_time")
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
}