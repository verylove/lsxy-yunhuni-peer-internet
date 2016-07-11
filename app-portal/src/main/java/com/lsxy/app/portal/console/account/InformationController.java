package com.lsxy.app.portal.console.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/6/24.
 * 基本资料处理类
 */
@Controller
@RequestMapping("/console/account/information")
public class InformationController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");





    /**
     * 基本资料首页入口
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        RestResponse<Account> restResponse = findByUsername(request);
        Account account = restResponse.getData();
       InformationVo informationVo = new InformationVo(account);

        mav.addObject("informationVo",informationVo);
        mav.setViewName("/console/account/information/index");
        return mav;
    }

    /**
     * 查询用户信息的rest请求
     * @return
     */
    private RestResponse findByUsername(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/account/find_by_username";
        Map map = new HashMap();
        return  RestRequest.buildSecurityRequest(token).post(uri,map, Account.class);
    }
    /**
     * 基本资料修改入口
     * @param request
     * @param informationVo 基本资料VO对象
     * @return
     */
    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request, InformationVo informationVo ){
        ModelAndView mav = new ModelAndView();
        //修改数据库数据

        RestResponse<Account> restResponse = saveInformation(request,informationVo);
        Account account = restResponse.getData();

        if(account!=null) {
            informationVo = new InformationVo(account);
            request.getSession().setAttribute("InformationVo", informationVo);
            mav.addObject("informationVo", informationVo);
            mav.addObject("msg", "修改成功！");
        }else{
            mav.addObject("informationVo", informationVo);
            mav.addObject("msg", "修改失败！");
        }
        mav.setViewName("/console/account/information/index");
        return mav;
    }

    /**
     * 保存基本资料的rest请求
     * @param informationVo 基本资料vo对象
     * @return
     */
    private RestResponse saveInformation( HttpServletRequest request,InformationVo informationVo){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/account/information/save_information";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(informationVo, Map.class);

        return  RestRequest.buildSecurityRequest(token).post(uri,map, Account.class);
    }


}
