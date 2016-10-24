package com.gft.backend.services;

import com.gft.backend.configs.SpringWebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;

/**
 * Created by miav on 2016-10-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {SpringWebConfig.class})
@WebAppConfiguration
public class MailServiceTest {
    @Autowired
    WebApplicationContext wac;

    @Test(expected = MailException.class)
    public void initService() throws Exception  {
        MailService mailService = wac.getBean(MailService.class);
        mailService.sendEmail("test@gft.com","clnnode@gft.com","Sub","Body");
        assertTrue(mailService != null);
    }
}
