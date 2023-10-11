package com.hanifshop.productservice.product_service.repository;

import com.hanifshop.productservice.product_service.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author Hanif al kamal 10/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public interface ProductCategoryDao extends JpaRepository<ProductCategory, Integer>{

    ProductCategory findByCategoryId(Long categoryId);

}
