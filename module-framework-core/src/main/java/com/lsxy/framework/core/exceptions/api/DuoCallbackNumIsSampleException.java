package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/9/8.
 */
public class DuoCallbackNumIsSampleException extends YunhuniApiException {
    public DuoCallbackNumIsSampleException(Throwable t) {
        super(t);
    }

    public DuoCallbackNumIsSampleException() {
        super();
    }

    public DuoCallbackNumIsSampleException(String context) {
        super(context);
    }

    public DuoCallbackNumIsSampleException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.DuoCallbackNumIsSample;
    }
}
