package com.lsxy.area.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/9.
 */
@Component
public class StasticsCounter {

    private static final Logger logger = LoggerFactory.getLogger(StasticsCounter.class);
    
    //收到API网关  请求消息次数
    private AtomicInteger receivedGWRequestCount = new AtomicInteger(0);
    //发送给API网关消息次数
    private AtomicInteger sendGWResponseCount = new AtomicInteger(0);

    //发送给区域请求次数
    private AtomicInteger sendAreaNodeRequestCount = new AtomicInteger(0);

    //收到区域的请求次数
    private AtomicInteger receivedAreaNodeRequestCount = new AtomicInteger(0);

    //发送SYS.CALL指令给区域的次数
    private AtomicInteger sendAreaNodeSysCallCount = new AtomicInteger(0);

    //收到CTI事件次数
    private AtomicInteger receivedAreaNodeCTIEventCount = new AtomicInteger(0);

    //收到CTI incoming事件次数
    private AtomicInteger receivedAreaNodeInComingEventCount = new AtomicInteger(0);


    public AtomicInteger getSendAreaNodeSysCallCount() {
        return sendAreaNodeSysCallCount;
    }

    public AtomicInteger getReceivedAreaNodeCTIEventCount() {
        return receivedAreaNodeCTIEventCount;
    }

    public AtomicInteger getReceivedAreaNodeInComingEventCount() {
        return receivedAreaNodeInComingEventCount;
    }

    public AtomicInteger getReceivedGWRequestCount() {
        return receivedGWRequestCount;
    }

    public AtomicInteger getSendGWResponseCount() {
        return sendGWResponseCount;
    }

    public AtomicInteger getSendAreaNodeRequestCount() {
        return sendAreaNodeRequestCount;
    }

    public AtomicInteger getReceivedAreaNodeRequestCount() {
        return receivedAreaNodeRequestCount;
    }

    @Scheduled(fixedDelay=5000)
    public void doOutStatistic(){
        if(logger.isDebugEnabled()){
            logger.debug("==============统计指标=========");
            logger.debug("收到API网关请求消息次数:{}",receivedGWRequestCount.get());
            logger.debug("发送给API网关消息次数:{}",sendGWResponseCount.get());
            logger.debug("发送给区域请求次数:{}",sendAreaNodeRequestCount.get());
            logger.debug("收到区域的请求次数:{}",receivedAreaNodeRequestCount.get());
            logger.debug("发送SYS.CALL指令给区域的次数:{}",sendAreaNodeSysCallCount.get());
            logger.debug("收到CTI事件次数:{}",receivedAreaNodeCTIEventCount.get());
            logger.debug("收到CTI incoming事件次数:{}",receivedAreaNodeInComingEventCount.get());
            logger.debug("=============================\r\n\r\n");
        }
    }
    
}
