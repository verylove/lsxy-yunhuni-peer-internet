package com.lsxy.area.api.callcenter;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

/**
 * Created by Administrator on 2017/1/9.
 */
public interface ConversationOps {

    public boolean dismiss(String ip,String appId,String conversationId) throws YunhuniApiException;

    public boolean setVoiceMode(String ip,String appId,String conversationId, String agentId, Integer voiceMode) throws YunhuniApiException;
}
