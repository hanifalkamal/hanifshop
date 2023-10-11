package com.hanifshop.productservice.product_service.controller;

import com.hanifshop.productservice.product_service.dto.ProductCategoryDto;
import com.hanifshop.productservice.product_service.dto.ProductDto;
import com.hanifshop.productservice.product_service.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Hanif al kamal 11/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Controller
public interface ProductController {

    @GetMapping(Constant.ControllerRoute.allCategory)
    ResponseEntity<?> productCategory();

    @PostMapping(Constant.ControllerRoute.updateCategory)
    ResponseEntity<?> updateProductCategory(
            ProductCategoryDto productCategoryDto
    );

    @PostMapping(Constant.ControllerRoute.addCategory)
    ResponseEntity<?> addProductCategory(
            ProductCategoryDto productCategoryDto
    );

    @PostMapping(Constant.ControllerRoute.deleteCategory)
    ResponseEntity<?> removeProductCategory(
            ProductCategoryDto productCategoryDto
    );

    @GetMapping(Constant.ControllerRoute.allProduct)
    ResponseEntity<?> productList(
            ProductDto productDto
    );

    @PostMapping(Constant.ControllerRoute.addProduct)
    ResponseEntity<?> addProduct(
            ProductDto productDto
    );

    @PostMapping(Constant.ControllerRoute.updateProduct)
    ResponseEntity<?> updateProduct(
            ProductDto productDto
    );

    @PostMapping(Constant.ControllerRoute.deleteProduct)
    ResponseEntity<?> removeProduct(
            ProductDto productDto
    );




}
