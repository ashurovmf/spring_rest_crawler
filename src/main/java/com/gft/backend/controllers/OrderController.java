package com.gft.backend.controllers;

import com.gft.backend.entities.CustomerOrder;
import com.gft.backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miav on 2016-09-23.
 */
@RestController
@EnableWebMvc
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

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
    @RequestMapping(value = "/change", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerOrder> createOrder(@RequestBody CustomerOrder order) {
        CustomerOrder result = orderService.registerNewOrder(order);
        return new ResponseEntity<CustomerOrder>(result, HttpStatus.OK);
    }
}
