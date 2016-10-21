package com.gft.backend.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by miav on 2016-10-21.
 */
public class FacebookContextTest {
    @Test
    public void initContext() throws Exception  {
        FacebookContext context = new FacebookContext();
        context.setAppId("AppId");
        context.setSecretId("Secret");
        assertEquals("AppId", context.getAppId());
        assertEquals("Secret", context.getSecretId());
    }
}
