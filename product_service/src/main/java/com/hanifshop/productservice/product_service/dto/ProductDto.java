package com.hanifshop.productservice.product_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Hanif al kamal 11/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Getter
@Setter
public class ProductDto {

    private Long productId = 0L;
    private String productName = "";
    private String description = "";
    private Double price = 0.0;
    private Integer stockQuantity = 0;
//    private ProductCategory category = new ProductCategory();
    private Long categoryId = 0L;

}
