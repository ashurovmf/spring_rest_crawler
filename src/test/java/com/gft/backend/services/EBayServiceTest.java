package com.gft.backend.services;

import com.gft.backend.configs.SpringWebConfig;
import com.gft.backend.entities.EBayCategory;
import com.gft.backend.entities.EBayItem;
import com.gft.backend.services.EBayService;
import com.gft.backend.utils.EBayResultWrapper;
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

import static org.junit.Assert.*;

/**
 * Created by miav on 2016-09-27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {SpringWebConfig.class})
@WebAppConfiguration
public class EBayServiceTest {

    @Autowired
    WebApplicationContext wac;

    @Test
    public void searchItemsTest() throws Exception {
        EBayService service = (EBayService) wac.getBean(EBayService.class);
        List<String> keys = new ArrayList<>(3);
        keys.add("IPhone");
        EBayResultWrapper eBayResultWrapper = service.searchItems("9355", keys, null);
        List<EBayItem> items = (List<EBayItem>) eBayResultWrapper.getResult();
        assertNotNull("List of ebay items is not null", items);
        assertTrue(items.size() > 0);
        assertEquals("EUR", items.get(0).getCurrency());
        assertEquals("1.0", items.get(0).getCurrentPrice());
    }

    @Test
    public void getCategoryTest() throws Exception {
        EBayService service = (EBayService) wac.getBean(EBayService.class);
        EBayResultWrapper eBayResultWrapper = service.getCategories();
        List<EBayCategory> categories = (List<EBayCategory>) eBayResultWrapper.getResult();
        assertNotNull("List of ebay items is not null", categories);
        assertTrue(categories.size() > 0);
    }
}
