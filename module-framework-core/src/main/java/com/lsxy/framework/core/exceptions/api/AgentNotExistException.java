package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class AgentNotExistException extends YunhuniApiException {
    public AgentNotExistException(Throwable t) {
        super(t);
    }

    public AgentNotExistException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AgentNotExist;
    }
}
