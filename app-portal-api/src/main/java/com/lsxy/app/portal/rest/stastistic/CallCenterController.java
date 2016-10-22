package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.session.model.CallCenter;
import com.lsxy.yunhuni.api.session.service.CallCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangxb on 2016/10/22.
 */
@RequestMapping("/rest/call_center")
@RestController
public class CallCenterController {
    @Autowired
    CallCenterService callCenterService;
    @RequestMapping("/plist")
    public RestResponse plist(Integer pageNo,Integer pageSize,String tenantId,String appId,String startTime,String endTime,String type,String agent){
        try{
            DateUtils.parseDate(startTime,"yyyy-MM-dd");
            DateUtils.parseDate(endTime,"yyyy-MM-dd");
            startTime += " 00:00:00";
            endTime += " 23:59:59";
        }catch (Exception e){
            return RestResponse.failed("0000","返回日期类型错误");
        }
        Page<CallCenter> page =  callCenterService.pList( pageNo, pageSize,tenantId, appId, startTime, endTime, type, agent);
        return RestResponse.success(page);
    }
}
