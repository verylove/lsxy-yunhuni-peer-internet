package com.lsxy.call.center.states.lock;

/**
 * Created by liuws on 2016/11/11.
 */
public class ExtensionLock extends RedisLock{
    @Override
    protected String setKey() {
        return null;
    }
}
