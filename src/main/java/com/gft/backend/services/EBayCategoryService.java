package com.gft.backend.services;

import com.gft.backend.dao.EBayCategoryDAO;
import com.gft.backend.entities.EBayCategory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miav on 2016-10-14.
 */
@Service
public class EBayCategoryService {

    private static final Logger logger = Logger.getLogger(EBayCategoryService.class);

    @Autowired
    private EBayCategoryDAO categoryDAO;

    @Autowired
    private EBayService eBayService;

    @Cacheable(value="categoryFindCache")
    public List<EBayCategory> getAllCategory(){
        List<EBayCategory> eBayCategories = categoryDAO.findAll();
        if(eBayCategories.size() == 0){
            List<EBayCategory> eBayServiceCategories = eBayService.getCategories();
            categoryDAO.createCollection(eBayServiceCategories);
            eBayCategories = eBayServiceCategories;
        }
        return eBayCategories;
    }

    @Cacheable(value="categoryOneFindCache", key = "#id")
    public EBayCategory getCategoryById(String id){
        return categoryDAO.find(id);
    }
}
