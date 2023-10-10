package com.hanifshop.productservice.product_service.controller;

import com.hanifshop.productservice.product_service.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Hanif al kamal 11/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Controller
public interface ProductController {

    @GetMapping(Constant.ControllerRoute.allCategory)
    ResponseEntity<?> productCategory();


}
