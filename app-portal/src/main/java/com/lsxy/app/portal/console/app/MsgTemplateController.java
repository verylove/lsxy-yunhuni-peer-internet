package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static com.lsxy.framework.web.rest.RestRequest.buildSecurityRequest;

/**
 * Created by zhangxb on 2017/3/7.
 */
@Controller
@RequestMapping("/console/msg_template")
public class MsgTemplateController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(MsgTemplateController.class);
    @RequestMapping("/plist")
    public RestResponse getPage(HttpServletRequest request,@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "20") int pageSize, @RequestParam String appId, String name, String subId ){
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/msg_template/plist";
        RestResponse response =  buildSecurityRequest(token).getPage(url,MsgTemplate.class,appId);
        return response;
    }
//    @RequestMapping(value = "/new")
//    public RestResponse create(HttpServletRequest request, String appId, String type, String name, String content, String remark){
//        String token = getSecurityToken(request);
//        String url = PortalConstants.REST_PREFIX_URL + "/rest/msg_template/new";
//        RestResponse response =  buildSecurityRequest(token).post(url,String.class,appId);
//        return RestResponse.success("成功");
//    }
//    @RequestMapping(value = "/eidt/{id}")
//    public RestResponse edit(HttpServletRequest request, @PathVariable String id, String name, String content, String remark){
//        Account account = getCurrentAccount();
//        MsgTemplate msgTemplate = msgTemplateService.findById(id);
//        if(msgTemplate!=null){
//            if(msgTemplate.getStatus() == -1){
//                msgTemplate.setName( name );
//                msgTemplate.setContent( content );
//                msgTemplate.setContent( remark );
//                msgTemplateService.save(msgTemplate);
//                return RestResponse.success("成功");
//            }else{
//                return RestResponse.failed("","不可修改");
//            }
//        }else{
//            return RestResponse.failed("","记录不存在");
//        }
//    }
//    @RequestMapping(value = "/delete/{id}")
//    public RestResponse delete(HttpServletRequest request, @PathVariable String id){
//        MsgTemplate msgTemplate = msgTemplateService.findById(id);
//        if(msgTemplate!=null){
//            try {
//                msgTemplateService.delete(msgTemplate);
//                return RestResponse.success("成功");
//            } catch (Exception e) {
//                logger.error("删除记录失败",e);
//                return RestResponse.failed("",e.getMessage());
//            }
//        }else{
//            return RestResponse.failed("","记录不存在");
//        }
//    }
//    @RequestMapping(value = "/find/{id}")
//    public RestResponse findById(HttpServletRequest request, @PathVariable String id){
//        MsgTemplate msgTemplate = msgTemplateService.findById(id);
//        if(msgTemplate!=null){
//            return RestResponse.success(msgTemplate);
//        }else{
//            return RestResponse.failed("","记录不存在");
//        }
//    }
}
