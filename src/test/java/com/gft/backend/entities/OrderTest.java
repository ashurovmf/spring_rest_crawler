package com.gft.backend.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by miav on 2016-09-23.
 */
public class OrderTest {

    @Test
    public void initOrderTest() throws Exception {
        Order order = new Order();
        order.setName("AppId1");
        order.setLocation("Cert-Id-test-1");
        order.setId(38);

        assertEquals("AppId1", order.getName());
        assertEquals("Cert-Id-test-1", order.getLocation());
        assertEquals(38, order.getId());
    }
}
