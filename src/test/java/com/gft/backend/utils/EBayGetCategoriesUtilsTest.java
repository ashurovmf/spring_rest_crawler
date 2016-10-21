package com.gft.backend.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by miav on 2016-10-21.
 */
public class EBayGetCategoriesUtilsTest {

    @Test
    public void createRequest() throws Exception{
        DocumentBuilder documentBuilder = DocumentBuilderFactory.
                newInstance().newDocumentBuilder();
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000).build();

        HttpPost httpPost = EBayGetCategoriesUtils.getCategoriesPost(requestConfig,
                "1234567890",documentBuilder);
        assertNotNull(httpPost);
        assertEquals(4,httpPost.getAllHeaders().length);
    }
}
