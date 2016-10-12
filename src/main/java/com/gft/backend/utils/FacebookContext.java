package com.gft.backend.utils;

/**
 * Created by miav on 2016-10-11.
 */
public class FacebookContext {
    public static final String Facebook_OAUTH_URL = "https://www.facebook.com/dialog/oauth";
    public static final String Facebook_ACCESS_TOKEN_URL = "https://graph.facebook.com/oauth/access_token";
    public static final String Facebook_ME_URL = "https://graph.facebook.com/me";
    private String appId;
    private String secretId;

    public String getAppId() { return appId; }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }
}
