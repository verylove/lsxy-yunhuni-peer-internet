package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 语音呼叫
 * Created by zhangxb on 2016/7/18.
 */
@Controller
@RequestMapping("/console/statistics/specifications")
public class SpecificationsController extends AbstractPortalController {
    /**
     * 语音呼叫
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/call")
    public ModelAndView call(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_CALL,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_CALL,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/specifications/call");
        return mav;
    }
    /**
     * 语音验证码
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/code")
    public ModelAndView code(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId);
        mav.addAllObjects(map);
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_VOICECODE,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/specifications/code");
        return mav;
    }
    /**
     * 录音
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/recording")
    public ModelAndView recording(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_RECORDING,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_RECORDING,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/specifications/recording");
        return mav;
    }
    /**
     * IVR定制服务
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/ivr")
    public ModelAndView ivr(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_IVR,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_IVR,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/specifications/ivr");
        return mav;
    }
    /**
     * 会议
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/metting")
    public ModelAndView metting(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_MEETING,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_MEETING,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/specifications/metting");
        return mav;
    }
    /**
     * 语音回拨
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_CALLBACK,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_CALLBACK,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/specifications/callback");
        return mav;
    }

    /**
     * 统计
     * @param request
     * @param type 类型
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    private RestResponse sum(HttpServletRequest request,Integer type,String time,String appId){
        String token = getSecurityToken(request);
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/voice_cdr/sum?type={1}&time={2}&appId={3}";
        return RestRequest.buildSecurityRequest(token).get(uri, BigDecimal.class,type,time,appId);
    }
    /**
     * 获取页面分页数据
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param type 类型
     * @param time 时间
     * @param appId 应用
     * @return
     */
    private RestResponse getPageList(HttpServletRequest request,Integer pageNo,Integer pageSize,Integer type,String time,String appId){
        String token = getSecurityToken(request);
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/voice_cdr/plist?pageNo={1}&pageSize={2}&type={3}&time={4}&appId={5}";
        return RestRequest.buildSecurityRequest(token).getPage(uri, VoiceCdr.class,pageNo,pageSize,type,time,appId);
    }
    /**
     * 获取租户下的全部应用
     * @param request
     * @return
     */
    private RestResponse getAppList(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/app/list";
        return RestRequest.buildSecurityRequest(token).getList(uri, App.class);
    }
    /**
     * 处理初始条件
     * @param request
     * @param time
     * @param appId
     * @return
     */
    public Map init(HttpServletRequest request, String time, String appId){
        Map map = new HashMap();
        List<App> appList = (List<App>)getAppList(request).getData();
        map.put("appList",appList);
        if(StringUtils.isEmpty(appId)){
            if(appList.size()>0) {
                appId = appList.get(0).getId();
            }
        }
        map.put("appId",appId);
        if(StringUtils.isEmpty(time)){
            time = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        map.put("time",time);
        return map;
    }
}
