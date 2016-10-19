package com.gft.backend.services;

import com.gft.backend.dao.UserDAO;
import com.gft.backend.entities.CustomerOrder;
import com.gft.backend.entities.EBayItem;
import com.gft.backend.entities.OrderResult;
import com.gft.backend.entities.UserInfo;
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

    @Autowired
    private OrderResultService orderResultService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserDAO userDAO;

    private boolean isLoaded = false;

    public CustomerOrder registerNewOrder(CustomerOrder order){
        order.setStatus("new");
        orderService.registerNewOrder(order); //save in db
        queueService.registerNewOrder(order); //put in queue
        return order;
    }

    @Scheduled(fixedRate = 20000)
    public void processOrder(){
        CustomerOrder nextOrder = queueService.getNextOrder();
        if(nextOrder != null){
            logger.debug("Try to process new order:"+nextOrder.getName());
            List<String> keys = new ArrayList<>();
            keys.add(nextOrder.getKeySearchString());
            List<EBayItem> eBayItems = eBayService.searchItems(nextOrder.getCategoryId(), keys, null);
            List<OrderResult> results = createOrderResults(nextOrder, eBayItems);
            orderResultService.saveResults(results);
            nextOrder.setStatus("done");
            orderService.updateOrder(nextOrder);
            //sendInfoAboutSerachResult(nextOrder,results);
        } else {
            if(!isLoaded){
                List<CustomerOrder> orders = orderService.getAllOrders();
                for (CustomerOrder order : orders){
                    if("new".equals(order.getStatus()))
                        queueService.retryOrder(order);
                }
            }
        }
    }

    private List<OrderResult> createOrderResults(CustomerOrder order, List<EBayItem> eBayItems) {
        List<OrderResult> results = new ArrayList<>(eBayItems.size());
        for(EBayItem item : eBayItems){
            OrderResult orderResult = new OrderResult();
            orderResult.setStatus("New");
            orderResult.setItemLink(item.getItemURL());
            orderResult.setOrder(order);
            results.add(orderResult);
        }
        return results;
    }

    private void sendInfoAboutSerachResult(CustomerOrder order, List<OrderResult> orderResults){
        UserInfo userInfo = userDAO.getUserInfo(order.getUserName());
        StringBuilder builder = new StringBuilder(1024);
        logger.debug("TRY TO SEND EMAIL TO "+userInfo.getEmail());
        builder.append("For your order we found followed offers:\n");
        for(int i=0; i < orderResults.size() && i < 3; ++i){
            builder.append("--- ");
            builder.append(orderResults.get(i).getItemLink());
            builder.append(" .\n");
        }
        builder.append("Check full results throw following:\n");
        builder.append("https://localhost:8443/restcrawler/details?order="+order.getId()+".\n");
        mailService.sendEmail(userInfo.getEmail(),"restcrawleryahoo.com","Search result",builder.toString());
    }
}
