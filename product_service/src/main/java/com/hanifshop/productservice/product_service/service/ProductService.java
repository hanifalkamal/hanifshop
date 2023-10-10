package com.hanifshop.productservice.product_service.service;

import java.util.Map;

/**
 * @author Hanif al kamal 10/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public interface ProductService {

    Map<String, Object> AddProductCategory(String categoryName);
    Map<String, Object> DeleteProductCategory(Long categoryId);
    Map<String, Object> UpdateProductCategory(Long categoryId, String categoryName);
    Map<String, Object> ListProductCategory();

}
