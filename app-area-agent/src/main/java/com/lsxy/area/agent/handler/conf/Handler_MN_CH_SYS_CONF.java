package com.lsxy.area.agent.handler.conf;

import com.lsxy.app.area.cti.RpcError;
import com.lsxy.app.area.cti.RpcResultListener;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.area.agent.cti.CTINode;
import com.lsxy.area.agent.utils.RefResIdUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_SYS_CONF extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CONF.class);

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_SYS_CONF;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {


        Map<String, Object> params = request.getParamMap();
        String conf_id = (String)params.get("user_data");

        try {
            CTINode cticlient = cticlientContext.getAvalibleNode(RefResIdUtil.get(params));
            cticlient.createResource( "sys.conf", params, new RpcResultListener(){
                @Override
                protected void onResult(Object o) {
                    Map<String,String> params = (Map<String,String>) o;
                    if(logger.isDebugEnabled()){
                        logger.debug("调用sys.conf成功，conf_id={},result={}",conf_id,o);
                    }

                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                            .put("method",Constants.EVENT_SYS_CONF_ON_START)
                            .put("res_id",params.get("res_id"))
                            .put("user_data",conf_id)
                            .build());
                    try {
                        rpcCaller.invoke(sessionContext,req,true);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败", Constants.EVENT_SYS_CONF_ON_START,e);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("调用sys.conf失败，conf_id={},result={}",conf_id,rpcError);
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CONF_ON_FAIL)
                                    .put("user_data",conf_id)
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req,true);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CONF_ON_FAIL,e);
                    }
                }

                @Override
                protected void onTimeout() {
                    logger.error("调用sys.conf超时，conf_id={}",conf_id);
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CONF_ON_TIMEOUT)
                                    .put("user_data",conf_id)
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req,true);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CONF_ON_TIMEOUT,e);
                    }
                }
            });
        } catch (Throwable e) {
            logger.error("调用创建会议资源失败",e);
        }
        return null;
    }
}
