package com.hanifshop.productservice.product_service.controller.impl;

import com.hanifshop.productservice.product_service.controller.ProductController;
import com.hanifshop.productservice.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * @author Hanif al kamal 11/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Controller
public class ProductControllerImpl implements ProductController {

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<?> productCategory() {
        Map<String, Object> mapping = productService.ListProductCategory();
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }
}
