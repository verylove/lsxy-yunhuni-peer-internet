package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;

import java.util.List;

/**
 * Created by liups on 2016/9/2.
 */
public interface TelnumToLineGatewayService extends BaseService<TelnumToLineGateway> {
    /**
     * 根据号码选择区域
     * @param telnum
     * @return
     */
    String getAreaIdByTelnum(String telnum);

    /**
     * 根据号码选择线路ID
     * @param number
     * @return
     */
    List<TelnumToLineGateway> getDialingLinesByNumber(String number);

    LineGateway getCalledLineByNumber(String number);
}
