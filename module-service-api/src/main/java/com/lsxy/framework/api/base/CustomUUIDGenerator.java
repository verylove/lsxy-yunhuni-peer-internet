package com.lsxy.framework.api.base;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.UUIDHexGenerator;

import java.io.Serializable;

/**
 * Created by liups on 2016/9/23.
 */
public class CustomUUIDGenerator extends UUIDHexGenerator {
    public Serializable generate(SessionImplementor session, Object obj) {
        if (obj instanceof IdEntity
                && (((IdEntity) obj).getId() != null
                && ((IdEntity) obj).getId().trim().length() > 0)) {
            return ((IdEntity) obj).getId();
        } else {
            return super.generate(session, obj);
        }
    }
}
