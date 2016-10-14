package com.gft.backend.services;

import com.gft.backend.dao.OrderResultDAO;
import com.gft.backend.entities.EBayCategory;
import com.gft.backend.entities.OrderResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by miav on 2016-10-14.
 */
@Service
public class OrderResultService {
    private static final Logger logger = Logger.getLogger(OrderResultService.class);

    @Autowired
    private OrderResultDAO orderResultDAO;

    @Cacheable(value="orderResultsFindCache")
    public List<OrderResult> getAllResults(){
        List<OrderResult> results = orderResultDAO.findAll();
        return results;
    }

    public void saveResults(Collection<OrderResult> resultCollection){
        orderResultDAO.createCollection(resultCollection);
    }
}
