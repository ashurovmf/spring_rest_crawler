package com.gft.backend.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by miav on 2016-09-27.
 */
public class EBayContextTest {
    @Test
    public void initEbayContextTest() throws Exception {
        EBayContext context = new EBayContext();
        EBayCredential credential = new EBayCredential();
        context.setApiCredential(credential);
        context.setApiServerUrl("http://svcs.sandbox.ebay.com/services/search/FindingService/v1");
        context.setSite(77);
        context.setTimeout(1000);
        context.setWsdlVersion("1.13.0");
        context.setTotalCalls(5);

        assertNotNull(context.getApiCredential());
        assertEquals("1.13.0", context.getWsdlVersion());
        assertEquals("http://svcs.sandbox.ebay.com/services/search/FindingService/v1", context.getApiServerUrl());
        assertEquals(77, context.getSite());
        assertEquals(1000, context.getTimeout());
        assertEquals(5, context.getTotalCalls());
    }
}
