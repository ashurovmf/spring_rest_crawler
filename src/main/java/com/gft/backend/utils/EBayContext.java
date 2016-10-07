package com.gft.backend.utils;

/**
 * Created by miav on 2016-09-27.
 */
public class EBayContext {
    private int timeout = 0;
    private int totalCalls = 0;
    private int site = 71;
    private EBayCredential apiCredential = new EBayCredential();
    private String apiServerUrl = null;
    private String wsdlVersion = "797";

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(int totalCalls) {
        this.totalCalls = totalCalls;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public EBayCredential getApiCredential() {
        return apiCredential;
    }

    public void setApiCredential(EBayCredential apiCredential) {
        this.apiCredential = apiCredential;
    }

    public String getApiServerUrl() {
        return apiServerUrl;
    }

    public void setApiServerUrl(String apiServerUrl) {
        this.apiServerUrl = apiServerUrl;
    }

    public String getWsdlVersion() {
        return wsdlVersion;
    }

    public void setWsdlVersion(String wsdlVersion) {
        this.wsdlVersion = wsdlVersion;
    }
}
