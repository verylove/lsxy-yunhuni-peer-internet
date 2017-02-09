package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class AgentNotConversationMemberException extends YunhuniApiException {
    public AgentNotConversationMemberException(Throwable t) {
        super(t);
    }

    public AgentNotConversationMemberException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AgentNotConversationMember;
    }
}