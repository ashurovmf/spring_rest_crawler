package com.gft.backend.services;

import com.gft.backend.configs.SpringWebConfig;
import com.gft.backend.dao.CustomerOrderDAO;
import com.gft.backend.dao.OrderResultDAO;
import com.gft.backend.entities.CustomerOrder;
import com.gft.backend.entities.OrderResult;
import org.aspectj.weaver.ast.Or;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by miav on 2016-10-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {SpringWebConfig.class})
@WebAppConfiguration
public class OrderResultServiceTest {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    CustomerOrderDAO orderDAO;

    @Autowired
    OrderResultDAO resultDAO;

    CustomerOrder order;

    List<OrderResult> orderResults;

    @Before
    public void prepareData(){
        order = prepareOrder();
        orderDAO.create(order);
        orderResults = prepareResults("111");
        resultDAO.createCollection(orderResults);
    }

    @After
    public void clearData(){
        clearResults(orderResults);
        orderDAO.remove(order);
    }



    @Test
    public void initServiceAll() throws Exception {
        OrderResultService resultService = wac.getBean(OrderResultService.class);
        List<OrderResult> allResults = resultService.getAllResults();
        assertTrue(allResults.size()>0);
    }

    @Test
    public void saveAll() throws Exception {
        OrderResultService resultService = wac.getBean(OrderResultService.class);
        List<OrderResult> results = prepareResults("222");
        resultService.saveResults(results);
        clearResults(results);
        assertTrue(results.get(0).getId() > 0);
    }


    private void clearResults(List<OrderResult> results) {
        for(OrderResult res : results){
            resultDAO.remove(res);
        }
    }

    private CustomerOrder prepareOrder(){
        CustomerOrder order = new CustomerOrder();
        order.setName("TEST RESULT ORDER");
        order.setStatus("0");
        order.setKeySearchString("EMPTY");
        order.setUserName("bob");
        order.setLocation("TEST LOCAL");
        order.setCategoryId("876");
        return order;
    }


    private List<OrderResult> prepareResults(String suffix){
        List<OrderResult> created = new ArrayList<>(2);
        OrderResult result = new OrderResult();
        result.setItemId("123"+suffix);
        result.setItemLink("http://link");
        result.setStatus("TEST");
        result.setOrder(order);
        created.add(result);
        result = new OrderResult();
        result.setItemId("123567"+suffix);
        result.setItemLink("http://link1");
        result.setStatus("TEST_TEST");
        result.setOrder(order);
        created.add(result);
        return created;
    }
}
