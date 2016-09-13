package com.lsxy.app.api.gateway.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.IVRCallInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.area.api.IVRService;
import com.lsxy.area.api.exceptions.RequestIllegalArgumentException;
import com.lsxy.area.api.exceptions.YunhuniApiException;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.web.utils.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by liuws on 2016/9/1.
 */
@RestController
public class IVRCallController extends AbstractAPIController{

    private static final Logger logger = LoggerFactory.getLogger(IVRCallController.class);

    @Reference(timeout=3000)
    private IVRService ivrService;

    @RequestMapping(value = "/{accountId}/call/ivr_call",method = RequestMethod.POST)
    public ApiGatewayResponse create(HttpServletRequest request, @PathVariable String accountId,
                               @RequestHeader(value = "AppID") String appId,
                               @RequestBody IVRCallInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("IVR CALL API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        //参数校验
        checkInputLen(dto.getFrom());
        if(StringUtils.isBlank(dto.getTo())){
            throw new RequestIllegalArgumentException();
        }
        if(dto.getMaxCallDuration() == null || dto.getMaxCallDuration()<= 0 || dto.getMaxCallDuration()> MAX_DURATION_SEC){
            throw new RequestIllegalArgumentException();
        }
        if(dto.getMaxDialDuration() != null && dto.getMaxDialDuration() <= 0){
            throw new RequestIllegalArgumentException();
        }
        checkInputLen(dto.getUserData());

        String callId = ivrService.ivrCall(ip,appId,dto.getFrom(),dto.getTo(),dto.getMaxDialDuration(),dto.getMaxCallDuration(),dto.getUserData());
        Map<String,Object> result = new MapBuilder<String,Object>().put("callId",callId).build();
        return ApiGatewayResponse.success(result);
    }
}