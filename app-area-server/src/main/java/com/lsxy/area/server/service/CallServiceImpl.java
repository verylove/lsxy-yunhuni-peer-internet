package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.*;
import com.lsxy.area.api.exceptions.*;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.NotifyCall;
import com.lsxy.yunhuni.api.session.model.VoiceCallback;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.NotifyCallService;
import com.lsxy.yunhuni.api.session.service.VoiceCallbackService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class CallServiceImpl implements CallService {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);

    @Autowired(required = false)
    private StasticsCounter cs;

    @Autowired(required = false)
    private TestIncomingZB tzb;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private CalCostService calCostService;

    @Autowired
    BusinessStateService businessStateService;

    @Autowired
    LineGatewayService lineGatewayService;

    @Autowired
    VoiceCallbackService voiceCallbackService;

    @Autowired
    NotifyCallService notifyCallService;

    @Autowired
    CallSessionService callSessionService;

    @Override
    public String call(String from, String to, int maxAnswerSec, int maxRingSec) throws YunhuniApiException {

        String callid = UUIDGenerator.uuid();
        String params = "to=%s&from=%s&maxAnswerSec=%d&maxRingSec=%d&callid=%s";
        params = String.format(params, to, from, maxAnswerSec, maxRingSec, callid);

        try {
            //找到合适的区域代理

                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("发送SYS_CALL指令到区域:{}", rpcrequest);
                    }

                    /*发送给区域的请求次数计数*/
                    if (cs != null) cs.getSendAreaNodeRequestCount().incrementAndGet();

                    tzb.doCallZB(to,rpcrequest);

                    rpcCaller.invoke(sessionContext, rpcrequest);

                    /*呼叫API调用次数计数*/
                    if(cs!=null)cs.getSendAreaNodeSysCallCount().incrementAndGet();
                } catch (Exception e) {
                    throw new InvokeCallException(e);
                }
            return callid;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String duoCallback(String ip,String appId, DuoCallbackDTO dto) throws YunhuniApiException {
        String apiCmd = "duo_call";
        String duocCallId = UUIDGenerator.uuid();
        String to1 = dto.getTo1();
        String to2 = dto.getTo2();
        if(apiGwRedBlankNumService.isRedNum(to1) || apiGwRedBlankNumService.isRedNum(to2)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);
        if(app.getStatus() != app.STATUS_ONLINE){
            throw new AppOffLineException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(app.getIsVoiceCallback() != 1){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(apiCmd, app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        //TODO 获取号码
        String oneTelnumber = appService.findOneAvailableTelnumber(app);
        dto.setFrom1(oneTelnumber);
        dto.setFrom2(oneTelnumber);
        //TODO 获取线路IP和端口
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);
        String to1_uri = dto.getTo1()+"@"+lineGateway.getIp()+":"+lineGateway.getPort();
        String to2_uri = dto.getTo2()+"@"+lineGateway.getIp()+":"+lineGateway.getPort();
        Map<String, Object> params = new HashMap<>();
        params.put("from1_uri", dto.getFrom1());
        params.put("to1_uri",to1_uri);
        params.put("from2_uri", dto.getFrom2());
        params.put("to2_uri",to2_uri);
        params.put("max_connect_seconds",dto.getMax_call_duration());
        params.put("max_ring_seconds",dto.getMax_dial_duration());
        params.put("ring_play_file",dto.getRing_tone());
        params.put("ring_play_mode",dto.getRing_tone_mode());
        params.put("user_data1",duocCallId);
        params.put("user_data2",duocCallId);
        //录音
        if(dto.getRecording()){
            //TODO 录音文件名称
            params.put("record_file ",duocCallId);
            params.put("record_mode",dto.getRecord_mode());
            params.put("record_format ",1);
        }

        //增加区域参数 选择合适的会话(传入appid即可)
        params.put("appid ",app.getId());
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_DUO_CALLBACK, params);
        try {
            Map<String,Object> data = new MapBuilder<String,Object>()
                    .put(to1_uri,UUIDGenerator.uuid())
                    .put(to2_uri,UUIDGenerator.uuid())
                    .build();
            //将数据存到redis
            BusinessState cache = new BusinessState.Builder()
                                    .setTenantId(app.getTenant().getId())
                                    .setAppId(appId)
                                    .setId(duocCallId)
                                    .setType(apiCmd)
                                    .setUserdata(dto.getUser_data())
                                    .setCallBackUrl(app.getUrl())
                                    .setAreaId(app.getArea().getId())
                                    .setLineGatewayId(lineGateway.getId())
                                    .setBusinessData(data)
                                    .build();

            businessStateService.save(cache);
            //保存双向回拔表
            VoiceCallback voiceCallback = new VoiceCallback(duocCallId,dto.getFrom1(),dto.getFrom2(),to1_uri,to2_uri);
            voiceCallbackService.save(voiceCallback);
            CallSession callSession = new CallSession((String) data.get(to1_uri),CallSession.STATUS_CALLING,app,app.getTenant(),duocCallId, ProductCode.changeApiCmdToProductCode(apiCmd).name(),oneTelnumber,to1_uri);
            CallSession callSession2 = new CallSession((String) data.get(to2_uri),CallSession.STATUS_CALLING,app,app.getTenant(),duocCallId, ProductCode.changeApiCmdToProductCode(apiCmd).name(),oneTelnumber,to2_uri);
            callSessionService.save(callSession);
            callSessionService.save(callSession2);
            rpcCaller.invoke(sessionContext, rpcrequest);

            return duocCallId;
        }catch(Exception e){
            logger.error("消息发送到区域失败:{}", rpcrequest);
            e.printStackTrace();
            throw new InvokeCallException(e);
        }
    }

    @Override
    public void duoCallbackCancel(String ip, String appId, String callId) throws YunhuniApiException{
        App app = appService.findById(appId);
        if(app.getStatus() != app.STATUS_ONLINE){
            throw new AppOffLineException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        BusinessState businessState = businessStateService.get(callId);
        Map<String, Object> params = new MapBuilder<String, Object>()
                .put("res_id",businessState.getResId())
                .put("user_data ",businessState.getId())
                .put("appid ",app.getId())
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_DUO_CALLBACK_CANCEL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        }catch(Exception e){
            logger.error("消息发送到区域失败:{}", rpcrequest);
            e.printStackTrace();
            throw new InvokeCallException(e);
        }

    }

    @Override
    public String notifyCall(String ip, String appId, NotifyCallDTO dto) throws YunhuniApiException{
        String apiCmd = "notify_call";
        String callId = UUIDGenerator.uuid();
        String to = dto.getTo();
        if(apiGwRedBlankNumService.isRedNum(to)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);
        if(app.getStatus() != app.STATUS_ONLINE){
            throw new AppOffLineException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(app.getIsVoiceCallback() != 1){
            throw new AppServiceInvalidException();
        }
        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(apiCmd, app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        //TODO 获取号码
        String oneTelnumber = appService.findOneAvailableTelnumber(app);
        dto.setFrom(oneTelnumber);
        //TODO 获取线路IP和端口
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);
        String to_uri = dto.getTo()+"@"+lineGateway.getIp()+":"+lineGateway.getPort();
        //TODO 获取线路IP和端口
        Map<String, Object> params = new HashMap<>();
        params.put("from_uri", dto.getFrom());
        params.put("to_uri", to_uri);
//        params.put("play_content",JSONUtil.objectToJson(dto.getFiles()));
        params.put("play_repeat",dto.getRepeat());
        params.put("max_ring_seconds",dto.getMax_dial_duration());
        params.put("user_data",callId);
        if(dto.getFiles() != null && dto.getFiles().size()>0){
            Object[][] plays = new Object[][]{new Object[]{StringUtils.join(dto.getFiles(),"|"),7,""}};
            params.put("play_content", JSONUtil2.objectToJson(plays));
        }
        try {
            //增加区域参数 选择合适的会话(传入appid即可)
            params.put("appid ",app.getId());
            Area area = app.getArea();
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_NOTIFY_CALL, params);
            Map<String,Object> data = new MapBuilder<String,Object>()
                    .put(to_uri,UUIDGenerator.uuid())
                    .build();
            //将数据存到redis
            BusinessState cache = new BusinessState.Builder()
                                    .setTenantId(app.getTenant().getId())
                                    .setAppId(app.getId())
                                    .setId(callId)
                                    .setType(apiCmd)
                                    .setUserdata(dto.getUser_data())
                                    .setCallBackUrl(app.getUrl())
                                    .setAreaId(area.getId())
                                    .setLineGatewayId(lineGateway.getId())
                                    .setBusinessData(data)
                                    .build();
            businessStateService.save(cache);
            //保存语音通知
            NotifyCall notifyCall = new NotifyCall(dto.getFrom(),to_uri);
            notifyCallService.save(notifyCall);
            CallSession callSession = new CallSession((String) data.get(to_uri),CallSession.STATUS_CALLING,app,app.getTenant(),callId, ProductCode.changeApiCmdToProductCode(apiCmd).name(),oneTelnumber,to_uri);
            callSessionService.save(callSession);

            rpcCaller.invoke(sessionContext, rpcrequest);
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String captchaCall(String ip, String appId, CaptchaCallDTO dto) throws YunhuniApiException{
        String to = dto.getTo();
        if(apiGwRedBlankNumService.isRedNum(to)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);

        if(app.getStatus() != app.STATUS_ONLINE){
            throw new AppOffLineException();
        }

        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(app.getIsVoiceValidate() != 1){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.captcha_call.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        //TODO 获取线路IP和端口
        //TODO 待定
        String callId = UUIDGenerator.uuid();
        //TODO
        String oneTelnumber = appService.findOneAvailableTelnumber(app);
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        Map<String, Object> params = new MapBuilder<String, Object>()
                .putIfNotEmpty("to_uri",to+"@"+lineGateway.getIp()+":"+lineGateway.getPort())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("max_ring_seconds",dto.getMax_dial_duration())
                .putIfNotEmpty("valid_keys",dto.getVerify_code())
                .putIfNotEmpty("max_keys",dto.getMax_keys())
                .putIfNotEmpty("user_data",callId)
                .put("appid ",app.getId())
                .build();
        if(dto.getFiles() != null && dto.getFiles().size()>0){
            Object[][] plays = new Object[][]{new Object[]{StringUtils.join(dto.getFiles(),"|"),7,""}};
            params.put("play_content", JSONUtil2.objectToJson(plays));
        }
        try {
            //找到合适的区域代理
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_CAPTCHA_CALL, params);
            rpcCaller.invoke(sessionContext, rpcrequest);
            //将数据存到redis
            BusinessState cache = new BusinessState.Builder()
                    .setTenantId(app.getTenant().getId())
                    .setAppId(app.getId())
                    .setId(callId)
                    .setType("captcha_call")
                    .setCallBackUrl(app.getUrl())
                    .setUserdata(dto.getUser_data())
                    .setAreaId(app.getArea().getId())
                    .setLineGatewayId(lineGateway.getId())
                    .setBusinessData(new MapBuilder<String,Object>()
                            .put("from",oneTelnumber)
                            .put("to",to)
                            .build())
                    .build();
            businessStateService.save(cache);
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }
}
