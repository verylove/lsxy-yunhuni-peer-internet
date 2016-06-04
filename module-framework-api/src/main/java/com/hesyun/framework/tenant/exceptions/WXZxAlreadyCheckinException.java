package com.hesyun.framework.tenant.exceptions;

import com.hesyun.framework.tenant.model.Account;

public class WXZxAlreadyCheckinException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public WXZxAlreadyCheckinException(Account person){
        super("商户"+person.getTenant().getTenantName()+"用户："+person.getUserName()+"已在服务号签到。");
    }
}
