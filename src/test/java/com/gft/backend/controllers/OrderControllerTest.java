package com.gft.backend.controllers;

import com.gft.backend.configs.SpringWebConfig;
import com.gft.backend.dao.CustomerOrderDAO;
import com.gft.backend.entities.CustomerOrder;
import com.gft.backend.services.OrderService;
import com.gft.backend.utils.DummyAuthenticationBuilder;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.GreaterThan;
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

    @Autowired
    private CustomerOrderDAO orderDAO;

    private MockMvc mockMvc;

    private CustomerOrder order;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        String name = "TEST_ALEX";
        order = createOrder(name);
        orderDAO.create(order);
    }

    @After
    public void clearData(){
        orderDAO.remove(order);
    }

    @Test
    @WithMockUser
    public void getMe() throws Exception {
        login();
        this.mockMvc.perform(get("/order/me").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    public void getOrder() throws Exception {
        login();
        this.mockMvc.perform(get("/order/get").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(jsonPath("$[0].id").value(new GreaterThan(0)));
    }

    private void login(){
        SecurityContextHolder.getContext().setAuthentication(DummyAuthenticationBuilder.getOAuth("alex"));
    }

    private CustomerOrder createOrder(String name) {
        CustomerOrder order = new CustomerOrder();
        order.setName(name);
        order.setLocation("TEST");
        order.setUserName("alex");
        order.setCategoryId("9355");
        order.setKeySearchString("iphone");
        return order;
    }
}
