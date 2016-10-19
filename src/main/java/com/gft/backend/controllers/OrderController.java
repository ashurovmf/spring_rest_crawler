package com.gft.backend.controllers;

import com.gft.backend.entities.CustomerOrder;
import com.gft.backend.entities.OrderResult;
import com.gft.backend.services.OrderManagerService;
import com.gft.backend.services.OrderResultService;
import com.gft.backend.services.OrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by miav on 2016-09-23.
 */
@RestController
@EnableWebMvc
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderResultService resultService;

    @Autowired
    private OrderManagerService managerService;

    @PreAuthorize("#oauth2.clientHasRole('ROLE_USER')")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerOrder>> getOrder(@RequestParam(value = "id", required = false) Integer id) {
        List<CustomerOrder> result = new ArrayList<>();
        if(id == null){
            result = orderService.getAllOrders();
        }
        else {
            CustomerOrder order = orderService.findOrderById(id);
            result.add(order);
        }
        return new ResponseEntity<List<CustomerOrder>>(result, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.clientHasRole('ROLE_USER')")
    @RequestMapping(value = "/me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerOrder>> getMyOrder(@RequestParam(value = "id", required = false) Integer id,
                                                          Principal principal) {
        List<CustomerOrder> result = new ArrayList<>();
        if(id == null){
            logger.debug("Me request with user:"+principal.getName());
            result = orderService.getAllByUserName(principal.getName());
        }
        else {
            CustomerOrder order = orderService.findOrderById(id);
            result.add(order);
        }
        return new ResponseEntity<List<CustomerOrder>>(result, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.clientHasRole('ROLE_USER')")
    @RequestMapping(value = "/change", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerOrder> createOrder(@RequestParam(value = "name", required = true)
                                                                 String name,
                                                     @RequestParam(value = "location", required = false)
                                                                 String location,
                                                     @RequestParam(value = "keys", required = true)
                                                                 String keys,
                                                     @RequestParam(value = "categoryId", required = true)
                                                                 String categoryId,
                                                     Principal principal) {
        CustomerOrder order = createCustomerOrder(name, location, keys, categoryId, principal.getName());
        CustomerOrder result = managerService.registerNewOrder(order);
        return new ResponseEntity<CustomerOrder>(result, HttpStatus.OK);
    }


    @PreAuthorize("#oauth2.clientHasRole('ROLE_USER')")
    @RequestMapping(value = "/change", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerOrder> updateOrder(@RequestBody CustomerOrder order) {
        CustomerOrder result = orderService.updateOrder(order);
        return new ResponseEntity<CustomerOrder>(result, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.clientHasRole('ROLE_USER')")
    @RequestMapping(value = "/results", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<OrderResult>> orderResults(@RequestParam(value = "id", required = false) Integer orderId) {
        CustomerOrder order = orderService.findOrderById(orderId);
        Set<OrderResult> result = order.getResults();
        return new ResponseEntity<Set<OrderResult>>(result, HttpStatus.OK);
    }

    @PreAuthorize("#oauth2.clientHasRole('ROLE_USER')")
    @RequestMapping(value = "/close", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerOrder> closeOrder(@RequestParam(value = "id", required = false) Integer orderId) {
        CustomerOrder result = orderService.findOrderById(orderId);
        result.setStatus("closed");
        orderService.updateOrder(result);
        return new ResponseEntity<CustomerOrder>(result, HttpStatus.OK);
    }

    private CustomerOrder createCustomerOrder(String name,String location, String keys,
                                              String categoryId, String userName) {
        CustomerOrder order = new CustomerOrder();
        order.setName(name);
        if(location != null){
            order.setLocation(location);
        }
        else {
            order.setLocation("Poland");
        }
        order.setKeySearchString(keys);
        order.setCategoryId(categoryId);
        order.setStatus("new");
        order.setUserName(userName);
        return order;
    }
}
