package com.gft.backend.services;

import com.gft.backend.configs.SpringWebConfig;
import com.gft.backend.entities.EBayCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by miav on 2016-10-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {SpringWebConfig.class})
@WebAppConfiguration
public class EBayCategoryServiceTest {
    @Autowired
    WebApplicationContext wac;

    @Test
    public void initService() throws Exception  {
        EBayCategoryService eBayCategoryService = wac.getBean(EBayCategoryService.class);
        List<EBayCategory> allCategory = eBayCategoryService.getAllCategory();
        assertEquals(503,allCategory.size());
    }

    @Test
    public void getCategoryBuIdTest() throws Exception  {
        EBayCategoryService eBayCategoryService = wac.getBean(EBayCategoryService.class);
        EBayCategory category = eBayCategoryService.getCategoryById("9355");
        assertEquals("Cell Phones & Smartphones",category.getName());
        assertEquals("2",category.getLevel());
    }
}
