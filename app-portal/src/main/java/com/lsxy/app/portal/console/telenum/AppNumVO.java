package com.lsxy.app.portal.console.telenum;

import java.util.Date;

/**
 * Created by liups on 2017/1/12.
 */
public class AppNumVO {
    String rentId;
    String num;
    String status;
    String isCalled;
    String isDialing;
    String areaCode;
    String expireTime;
    String certId;
    String subId;

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(String isCalled) {
        this.isCalled = isCalled;
    }

    public String getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(String isDialing) {
        this.isDialing = isDialing;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}
