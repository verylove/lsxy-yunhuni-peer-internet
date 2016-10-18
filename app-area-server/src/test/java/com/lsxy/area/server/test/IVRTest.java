package com.lsxy.area.server.test;

import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.api.IVRService;
import com.lsxy.area.api.exceptions.YunhuniApiException;
import com.lsxy.area.server.AreaServerMainClass;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by liuws on 2016/7/14.
 * IVR测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AreaServerMainClass.class)
public class IVRTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private TestNumBindService testNumBindService;

    @Autowired
    private ResourcesRentService resourcesRentService;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private LineGatewayService lineGatewayService;

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Autowired
    private IVRService ivrService;

    @Test
    public void incoming(){
        App app = appService.findById("40288aca574060400157406427f20005");
        Tenant tenant = app.getTenant();
        String res_id = UUIDGenerator.uuid();
        String from = "13692206627";
        String to = "8675522730043";
        ivrActionService.doActionIfAccept(app,tenant,res_id,from,to);

        List<VoiceIvr> lists = (List<VoiceIvr>)voiceIvrService.list("from VoiceIvr order by createTime desc");
        System.out.println(lists.get(0).getId());
        //下一步干嘛
        int step = 1;
        while(ivrActionService.doAction(lists.get(0).getId())){
            System.out.println("ivr step" + (step++));
        }
    }

    @Test
    public void ivrcall() throws YunhuniApiException {
        ivrService.ivrCall("192.168.1.1","40288aca574060400157406427f20005","8675522730043","13692206627",45,900,null);
        List<VoiceIvr> lists = (List<VoiceIvr>)voiceIvrService.list("from VoiceIvr order by createTime desc");
        System.out.println(lists.get(0).getId());
        //下一步干嘛
        int step = 1;
        while(ivrActionService.doAction(lists.get(0).getId())){
            System.out.println("ivr step" + (step++));
        }
    }
}