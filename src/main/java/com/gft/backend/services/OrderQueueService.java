package com.gft.backend.services;

import com.gft.backend.entities.CustomerOrder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by miav on 2016-10-14.
 */
@Service
public class OrderQueueService {

    private static final Logger logger = Logger.getLogger(OrderQueueService.class);

    private BlockingQueue<CustomerOrder> mainQueue = new ArrayBlockingQueue<CustomerOrder>(128);

    private  BlockingQueue<CustomerOrder> nextStepQueue = new ArrayBlockingQueue<CustomerOrder>(128);

    public boolean registerNewOrder(CustomerOrder order){
        try {
            mainQueue.put(order);
        } catch (InterruptedException e) {
            logger.error("Cannot insert new order in queue",e);
            return false;
        }
        return true;
    }

    public boolean retryOrder(CustomerOrder order){
        try {
            nextStepQueue.put(order);
        } catch (InterruptedException e) {
            logger.error("Cannot insert the order in next queue",e);
            return false;
        }
        return true;
    }

    public CustomerOrder getNextOrder(){
        CustomerOrder peek = mainQueue.peek();
        if(peek == null){
            return nextStepQueue.poll();
        }
        else{
            return mainQueue.poll();
        }
    }

}
