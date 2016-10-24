package com.gft.backend.services;

import com.gft.backend.configs.SpringWebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
public class AuthenticationServiceTest {
    @Autowired
    WebApplicationContext wac;

    @Test
    public void initAuth() throws Exception  {
        AuthenticationService authenticationService = wac.getBean(AuthenticationService.class);
        UserDetails userDetails = authenticationService.loadUserByUsername("alex");
        assertEquals("alex", userDetails.getUsername());
        assertEquals("ROLE_USER",userDetails.getAuthorities().toArray()[0].toString());
    }
}
