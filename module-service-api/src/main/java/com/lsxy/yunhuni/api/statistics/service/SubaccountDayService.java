package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;

import java.util.Date;

/**
 * Created by liups on 2017/2/21.
 */
public interface SubaccountDayService extends BaseService<SubaccountDay> {
    void dayStatistics(Date date);
}
