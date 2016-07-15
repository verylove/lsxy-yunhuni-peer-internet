package com.lsxy.yunhuni.app.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/15.
 */
public interface AppOnlineActionDao extends BaseDaoInterface<AppOnlineAction, Serializable> {
    /**
     * 根据状态获取应用的动作
     * @param app
     * @param statusAvtive
     * @return
     */
    AppOnlineAction findByAppAndStatus(App app, int statusAvtive);
}
