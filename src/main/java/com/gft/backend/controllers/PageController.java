package com.gft.backend.controllers;

import com.gft.backend.annotations.LogMethodTime;
import com.gft.backend.services.EBayService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by miav on 2016-10-12.
 */
@Controller
public class PageController {
    private static final Logger logger = Logger.getLogger(PageController.class);


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView toShowMainPage() {
        logger.debug("Main page is creating");
        ModelAndView model = new ModelAndView();
        model.addObject("message", "This is main page for all users");
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = "/login")
    public ModelAndView loginwith() {
        ModelAndView model = new ModelAndView();
        model.setViewName("loginwith");
        return model;
    }

    @RequestMapping(value = "/me")
    public ModelAndView orders() {
        ModelAndView model = new ModelAndView();
        model.setViewName("order");
        return model;
    }

    @RequestMapping(value = "/details")
    public ModelAndView orderresult(@RequestParam(value = "order", required = true) Integer orderId) {
        ModelAndView model = new ModelAndView();
        model.addObject("order_id", orderId);
        model.setViewName("orderdetails");
        return model;
    }

}
