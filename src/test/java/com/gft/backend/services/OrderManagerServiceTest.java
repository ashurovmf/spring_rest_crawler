package com.gft.backend.services;

import com.gft.backend.configs.SpringWebConfig;
import com.gft.backend.dao.CustomerOrderDAO;
import com.gft.backend.entities.CustomerOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by miav on 2016-10-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {SpringWebConfig.class})
@WebAppConfiguration
public class OrderManagerServiceTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    CustomerOrderDAO orderDAO;

    public CustomerOrder prepareOrder(){
        CustomerOrder order = new CustomerOrder();
        order.setName("TEST");
        order.setLocation("Poland");
        order.setKeySearchString("lsdrp");
        order.setCategoryId("9355");
        order.setStatus("new");
        order.setUserName("bob");
        return order;
    }

    public void clearOrder(CustomerOrder order){
        orderDAO.remove(order);
    }

    @Test
    public void initServiceMain() throws Exception {
        OrderManagerService managerService = wac.getBean(OrderManagerService.class);
        CustomerOrder order = prepareOrder();
        CustomerOrder registeredOrder = managerService.registerNewOrder(order);
        managerService.processOrder();
        order = registeredOrder;
        clearOrder(order);
        assertEquals("done",registeredOrder.getStatus());
    }

}
