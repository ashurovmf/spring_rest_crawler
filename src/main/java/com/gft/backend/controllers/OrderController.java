package com.gft.backend.controllers;

import com.gft.backend.entities.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by miav on 2016-09-23.
 */
@RestController
@EnableWebMvc
@RequestMapping("/order")
public class OrderController {

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrder(@RequestParam("id") Integer id) {
        Order result = new Order();
        result.setId(id);
        result.setLocation("NY");
        result.setName("order_test");
        return new ResponseEntity<Order>(result, HttpStatus.OK);
    }
}
