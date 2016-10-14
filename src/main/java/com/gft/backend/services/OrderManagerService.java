package com.gft.backend.services;

import com.gft.backend.dao.OrderResultDAO;
import com.gft.backend.entities.CustomerOrder;
import com.gft.backend.entities.EBayItem;
import com.gft.backend.entities.OrderResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miav on 2016-10-14.
 */
@Service
public class OrderManagerService {

    private static final Logger logger = Logger.getLogger(OrderManagerService.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderQueueService queueService;

    @Autowired
    private EBayService eBayService;

    private OrderResultService orderResultService;

    public CustomerOrder registerNewOrder(CustomerOrder order){
        order.setStatus("New");
        queueService.registerNewOrder(order);
        orderService.registerNewOrder(order);
        return order;
    }

    @Scheduled(fixedRate = 20000)
    public void processOrder(){
        CustomerOrder nextOrder = queueService.getNextOrder();
        List<String> keys = new ArrayList<>();
        keys.add(nextOrder.getKeySearchString());
        List<EBayItem> eBayItems = eBayService.searchItems(nextOrder.getCategoryId(), keys, null);
        List<OrderResult> results = new ArrayList<>(eBayItems.size());
        for(EBayItem item : eBayItems){
            OrderResult orderResult = new OrderResult();
            orderResult.setStatus("New");
            orderResult.setItemLink(item.getItemURL());
            orderResult.setOrder(nextOrder);
            results.add(orderResult);
        }
        nextOrder.setStatus("done");
        orderService.updateOrder(nextOrder);
        orderResultService.saveResults(results);
    }
}
