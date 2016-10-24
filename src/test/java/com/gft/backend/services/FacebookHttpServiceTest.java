package com.gft.backend.services;

import com.gft.backend.configs.SpringWebConfig;
import com.gft.backend.utils.FacebookContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by miav on 2016-10-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {SpringWebConfig.class})
@WebAppConfiguration
public class FacebookHttpServiceTest {
    @Autowired
    WebApplicationContext wac;

    @Test
    public void initService() throws Exception  {
        FacebookHttpService facebookHttpService = wac.getBean(FacebookHttpService.class);
        String response = facebookHttpService.sendHttpRequest("GET", FacebookContext.Facebook_ME_URL,
                new String[]{"access_token", "fields"},
                new String[]{"111", "name,email"});
        assertTrue(response.contains("error"));
    }
}
