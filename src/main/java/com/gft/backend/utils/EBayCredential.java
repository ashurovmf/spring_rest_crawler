package com.gft.backend.utils;

/**
 * Created by miav on 2016-09-27.
 */
public class EBayCredential {
    private String appId = "";
    private String devId = "";
    private String certId = "";
    private String eBayToken = "";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String geteBayToken() {
        return eBayToken;
    }

    public void seteBayToken(String eBayToken) {
        this.eBayToken = eBayToken;
    }
}
