package com.gft.backend.utils;

import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import static org.junit.Assert.assertEquals;

/**
 * Created by miav on 2016-10-21.
 */
public class CustomTokenEnhancerTest {

    @Test
    public void initEnhancer() throws Exception  {
        CustomTokenEnhancer enhancer = new CustomTokenEnhancer();
        Authentication userAuthentication = DummyAuthenticationBuilder.getOAuth("alex");
        OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken("123");
        OAuth2AccessToken enhanced = enhancer.enhance(oAuth2AccessToken, (OAuth2Authentication) userAuthentication);
        assertEquals("ok", enhanced.getAdditionalInformation().get("status"));
    }
}
