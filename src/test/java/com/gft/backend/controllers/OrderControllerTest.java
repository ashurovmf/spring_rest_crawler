package com.gft.backend.controllers;

import com.gft.backend.configs.SpringWebConfig;
import com.gft.backend.utils.DummyAuthenticationBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by miav on 2016-09-23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = {SpringWebConfig.class})
@WebAppConfiguration
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class OrderControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @WithMockUser(username = "alex", roles = {"ROLE_USER"})
    public void getOrder() throws Exception {
        try {
            login();
            this.mockMvc.perform(get("/order/me").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                    .andExpect(jsonPath("$", hasSize(0)));
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
    }

    private void login(){
        SecurityContextHolder.getContext().setAuthentication(DummyAuthenticationBuilder.getOAuth("alex"));
    }
}
