package com.gft.backend.services;

import com.gft.backend.dao.CustomerOrderDAO;
import com.gft.backend.entities.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miav on 2016-10-13.
 */
@Service
public class OrderService {
    @Autowired
    CustomerOrderDAO orderDAO;

    public CustomerOrder registerNewOrder(CustomerOrder newOrder){
        orderDAO.create(newOrder);
        return newOrder;
    }

    public CustomerOrder updateOrder(CustomerOrder order){
        orderDAO.edit(order);
        return order;
    }

    public CustomerOrder findOrderById(Integer orderId){
        return orderDAO.find(orderId);
    }

    public List<CustomerOrder> getAllOrders(){
        return orderDAO.findAll();
    }
}
