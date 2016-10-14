package com.gft.backend.controllers;

import com.gft.backend.entities.EBayCategory;
import com.gft.backend.services.EBayCategoryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miav on 2016-10-14.
 */
@RestController
@RequestMapping("/category")
public class EBayCategoryController {

    private static final Logger logger = Logger.getLogger(EBayCategoryController.class);

    @Autowired
    private EBayCategoryService categoryService;

    @PreAuthorize("#oauth2.clientHasRole('ROLE_USER')")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EBayCategory>> getCategories(@RequestParam(value = "id", required = false) String id){
        List<EBayCategory> result = new ArrayList<>();
        if(id == null){
            result = categoryService.getAllCategory();
        }
        else {
            result.add(categoryService.getCategoryById(id));
        }
        return new ResponseEntity<List<EBayCategory>>(result, HttpStatus.OK);
    }
}
