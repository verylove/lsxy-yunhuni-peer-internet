package com.lsxy.yunhuni.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 应用小时统计
 * Created by zhangxb on 2016/8/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_base_app_day")
public class AppDay extends IdEntity {
    private String tenantId;//所属租户
    private Date dt;//统计时间
    private Integer day;//统计日期范围1-31
    private Long sumOnLine;//总上线个数
    private Long sumLine;//总未上线个数
    private Long sum_app_num;//总应用个数
    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Column(name = "day")
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }


    @Column(name = "sum_on_line")
    public Long getSumOnLine() {
        return sumOnLine;
    }

    public void setSumOnLine(Long sumOnLine) {
        this.sumOnLine = sumOnLine;
    }
    @Column(name = "sum_line")
    public Long getSumLine() {
        return sumLine;
    }

    public void setSumLine(Long sumLine) {
        this.sumLine = sumLine;
    }
    @Column(name = "sum_app_num")
    public Long getSum_app_num() {
        return sum_app_num;
    }

    public void setSum_app_num(Long sum_app_num) {
        this.sum_app_num = sum_app_num;
    }
}
