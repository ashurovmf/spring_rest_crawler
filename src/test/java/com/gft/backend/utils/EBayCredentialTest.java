package com.gft.backend.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by miav on 2016-09-27.
 */
public class EBayCredentialTest {
    @Test
    public void initEbayCredentialTest() throws Exception {
        EBayCredential credential = new EBayCredential();
        credential.setAppId("AppId1");
        credential.setCertId("Cert-Id-test-1");
        credential.setDevId("DevIdSmp");
        credential.seteBayToken("tokenName");

        assertEquals("AppId1", credential.getAppId());
        assertEquals("Cert-Id-test-1", credential.getCertId());
        assertEquals("DevIdSmp", credential.getDevId());
        assertEquals("tokenName", credential.geteBayToken());
    }
}
