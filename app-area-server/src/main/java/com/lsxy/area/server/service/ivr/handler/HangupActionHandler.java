package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * hungup指令处理器
 * Created by liuws on 2016/9/2.
 */
@Component
public class HangupActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getAction() {
        return "hangup";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        String res_id = state.getResId();
        if(state.getBusinessData().get(IVRActionService.IVR_ANSWER_WAITTING_FIELD) != null){
            //还在等待应答的情况下，调用拒接
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",res_id)
                    .putIfNotEmpty("user_data",callId)
                    .put("areaId",state.getAreaId())
                    .build();
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_REJECT, params);
            try {
                if(!businessStateService.closed(callId)) {
                    rpcCaller.invoke(sessionContext, rpcrequest, true);
                }
            } catch (Throwable e) {
                logger.error(String.format("调用拒绝失败,appId=%s,callid=%s",state.getAppId(),callId),e);
            }
        }else{
            //已应答调用挂断
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",res_id)
                    .putIfNotEmpty("user_data",callId)
                    .put("areaId",state.getAreaId())
                    .build();

            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
            try {
                if(!businessStateService.closed(callId)) {
                    rpcCaller.invoke(sessionContext, rpcrequest, true);
                }
            } catch (Throwable e) {
                logger.error(String.format("调用挂断失败,appId=%s,callid=%s",state.getAppId(),callId),e);
            }
        }

        businessStateService.updateInnerField(callId,IVRActionService.IVR_NEXT_FIELD,next);
        return true;
    }
}
