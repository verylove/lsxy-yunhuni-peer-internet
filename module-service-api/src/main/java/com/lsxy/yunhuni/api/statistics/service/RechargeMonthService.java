package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.statistics.model.RechargeMonth;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

/**
 * 订单记录月统计service
 * Created by zhangxb on 2016/8/1.
 */
public interface RechargeMonthService extends BaseService<RechargeMonth> {
    /**
     * 根据当前时间，进行统计
     * @param date1 时间yyyy-MM-dd
     * @param day1 第几天 1-31
     * @param date2 前一天的时间 yyyy-MM-dd
     * @param day2 前一天是第几天 1-31
     * @param select 组合groupby条件
     */
    public void monthStatistics(Date date1, int day1, Date date2, int day2, String[] select,String[] all) throws SQLException;
    /**
     * 获取某月的某个租户的充值额
     * @param d
     * @return
     */
    public BigDecimal getAmongAmountByDateAndTenant(Date d, String tenant);

}
