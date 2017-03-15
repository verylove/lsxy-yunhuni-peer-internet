package com.lsxy.msg.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.msg.dao.MsgUserRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgUserRequestServiceImpl extends AbstractService<MsgUserRequest> implements MsgUserRequestService {
    @Autowired
    MsgUserRequestDao msgUserRequestDao;

    @Override
    public BaseDaoInterface<MsgUserRequest, Serializable> getDao() {
        return this.msgUserRequestDao;
    }

    @Override
    public Page<MsgUserRequest> getPageByCondition(Integer pageNo, Integer pageSize, String sendType,String appId,Date start, Date end, int isMass, String taskName, String mobile) {
        boolean flag = isMass==1?true:false;
        String hql = " from MsgUserRequest obj where obj.sendType=?1 and obj.isMass=?2 ";
        if(StringUtils.isNotEmpty(appId)){
            hql += " and obj.appId = '"+appId+"' ";
        }
        if(StringUtils.isNotEmpty(taskName)){
            hql += " and obj.taskName like '%"+taskName+"%' ";
        }
        if(StringUtils.isNotEmpty(mobile)){
            hql += " and obj.mobile like '%"+mobile+"%' ";
        }
        if(start !=null && end != null) {
            hql += "and obj.sendTime between ?3 and ?4 ";
            return pageList( hql, pageNo, pageSize,sendType,flag,start,end);
        }else{
            return pageList( hql,pageNo,pageSize,sendType,flag);
        }

    }

    @Override
    public void updateNoMassStateByMsgKey(String msgKey, int state) {
        MsgUserRequest request = msgUserRequestDao.findByMsgKey(msgKey);
        request.setState(state);
        if(state == MsgUserRequest.STATE_SUCCESS){
            request.setSuccNum(1L);
            request.setFailNum(0L);
            request.setPendingNum(0L);
        }else{
            request.setSuccNum(0L);
            request.setFailNum(1L);
            request.setPendingNum(0L);
        }
        this.save(request);
    }

    @Override
    public List<MsgUserRequest> findAwaitedRequets() {
        Date current = new Date();
        Date start = new Date(current.getTime() - 3 * 24 * 60 * 60 * 1000);
        return msgUserRequestDao.findByStateAndSendTimeBetween(MsgUserRequest.STATE_WAIT,start,current);
    }

}
