package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * Created by Liuws on 2016/7/14.
 * 会议测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class CallcenterTest {

    @Autowired
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private AppExtensionService appExtensionService;

    @Autowired
    private EnQueueService enQueueService;

    @Autowired
    private CallCenterService callCenterService;

    @Autowired
    private CallCenterConversationService callCenterConversationService;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Test
    public void test(){
        /*callCenterAgentService.login("","",null);*/
    }

    @Test
    public void test2(){
        /*AppExtension appExtension = new AppExtension();
        appExtension.setEnabled(1);
        appExtension.setTenantId("40288ac9575612a30157561c7ff50004");
        appExtension.setAppId("40288ac957e1812e0157e18a994e0000");
        appExtension.setName("1234561");
        appExtension.setType(AppExtension.TYPE_SIP);
        appExtension.setUser("1234561");
        appExtension.setPassword("1234561");
        appExtension.setSecret("");
        appExtension.setRegistrar("");
        appExtension.setRegisterExpires(18000);
        appExtension.setLastRegisterTime(new Date());
        appExtension.setLastRegisterStatus(200);
        appExtensionService.register(appExtension);*/
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {
        /*int size = 5000;
        ExecutorService pool= Executors.newFixedThreadPool(100);
        List<Future> results = new ArrayList<>();
        for (int i =0;i<size;i++) {
            final int j = i;
            results.add(pool.submit(new Runnable() {
                @Override
                public void run() {
                    AppExtension appExtension = new AppExtension();
                    appExtension.setEnabled(AppExtension.ENABLED);
                    appExtension.setTenantId("wegwegweg");
                    appExtension.setAppId("gwegwegwe");
                    appExtension.setName("11234561"+j);
                    appExtension.setType(AppExtension.TYPE_SIP);
                    appExtension.setUser("11234561"+j);
                    appExtension.setPassword("11234561"+j);
                    appExtension.setSecret("");
                    appExtension.setRegistrar("");
                    appExtension.setRegisterExpires(180000);
                    appExtension.setLastRegisterTime(new Date());
                    appExtension.setLastRegisterStatus(200);
                    appExtensionService.register(appExtension);
                    List<AppExtension> extensions = new ArrayList<AppExtension>();
                    extensions.add(appExtension);
                    CallCenterAgent callCenterAgent = new CallCenterAgent();
                    callCenterAgent.setAgentNo("1test1"+j);
                    callCenterAgent.setAgentNum("1test1"+j);
                    callCenterAgent.setExtentions(extensions);
                    callCenterAgent.setState(CallCenterAgent.STATE_IDLE);
                    List<AgentSkill> skills = new ArrayList<>();
                    for(int i =0;i<10;i++){
                        AgentSkill skill = new AgentSkill();
                        skill.setLevel(new Random().nextInt(100));
                        skill.setActive(1);
                        skill.setName("1haha"+i);
                        skills.add(skill);
                    }
                    callCenterAgent.setSkills(skills);
                    String agentId = callCenterAgentService.login("wegwegweg","gwegwegwe",callCenterAgent);
                }
            }));
        }
        for (Future f: results) {
            f.get();
        }
        pool.shutdown();*/
    }

    @Test
    public void test4(){
//        callCenterAgentService.logout(app.getTenant().getId(), appId, channel, callCenterAgentService.pageList(1,1).getResult().get(0).getId(), force);
    }

    @Test
    public void test5(){
        EnQueue enQueue = EnQueueDecoder.decode("<enqueue\n" +
                "            waitPlayFile=\"music.wav\"\n" +
                "            playAgentNum=\"true\"\n" +
                "            preAgentNumPlayFile=\"坐席.wav\"\n" +
                "            postAgentNumPlayFile=\"为您服务.wav\"\n" +
                "            holdPlayFile=\"wait.wav\"\n" +
                "            conversationTimeout=\"3600\"\n" +
                "            data=\"my queue data\"\n" +
                ">\n" +
                "    <filter data=\"this is filter 1\">\n" +
                "        <condition timeout=\"20\" priority=\"60\" data=\"condition 1\">\n" +
                "            <where>\n" +
                "                <![CDATA[\n" +
                "                has(\"haha0\") && get(\"haha1\") > 60.0\n" +
                "                ]]>\n" +
                "            </where>\n" +
                "            <sort>\n" +
                "                <![CDATA[\n" +
                "                has(\"haha0\") && get(\"haha1\")\n" +
                "                ]]>\n" +
                "            </sort>\n" +
                "        </condition>\n" +
                "    </filter>\n" +
                "</enqueue>");
        enQueueService.lookupAgent("40288ac9575612a30157561c7ff50004","40288ac957e1812e0157e18a994e0000",null,"","",enQueue,null,null);
    }

    @Test
    public void test6(){
        CallCenter c = new CallCenter();
        c.setCost(new BigDecimal(600));
        c.setToManualResult(""+CallCenter.TO_MANUAL_RESULT_AGENT_FAIL);
        c.setId("fwfw");//不会被修改
        callCenterService.update("40288ae258f7d97d0158f7d9adc3000a",c);
    }

    @Test
    public void test7() throws YunhuniApiException {
        System.out.println(callCenterConversationService.conversationPageList(null,null,"8a2a6a4a58b9c19d0158bd76a9310000",1,10).getTotalCount());;
    }
}
