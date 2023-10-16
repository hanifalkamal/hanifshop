package com.hanifshop.productservice.product_service.controller.impl;

import com.hanifshop.productservice.product_service.controller.ProductController;
import com.hanifshop.productservice.product_service.dto.ProductCategoryDto;
import com.hanifshop.productservice.product_service.dto.ProductDto;
import com.hanifshop.productservice.product_service.interceptor.Token;
import com.hanifshop.productservice.product_service.interceptor.ValidateSession;
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

    @Override
    @ValidateSession
    public ResponseEntity<?> updateProductCategory(ProductCategoryDto dto, String token) {
        Map<String, Object> mapping = productService.UpdateProductCategory(dto.getCategoryId(), dto.getCategoryName());
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    @ValidateSession
    public ResponseEntity<?> addProductCategory(ProductCategoryDto dto, String token) {
        Map<String, Object> mapping = productService.AddProductCategory(dto.getCategoryName());
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    @ValidateSession
    public ResponseEntity<?> removeProductCategory(ProductCategoryDto dto, String token) {
        Map<String, Object> mapping = productService.DeleteProductCategory(dto.getCategoryId());
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> productList(ProductDto productDto) {
        Map<String, Object> mapping = productService.ListProduct(productDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    @ValidateSession
    public ResponseEntity<?> addProduct(ProductDto productDto, String token) {
        Map<String, Object> mapping = productService.AddProduct(productDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    @ValidateSession
    public ResponseEntity<?> updateProduct(ProductDto productDto, String token) {
        Map<String, Object> mapping = productService.UpdateProduct(productDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeProduct(ProductDto productDto, String token) {
        Map<String, Object> mapping = productService.DeleteProduct(productDto);
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getCache() {
        Map<String, Object> mapping = productService.showStockCache();
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }
}
