package com.hanifshop.productservice.product_service.repository;

import com.hanifshop.productservice.product_service.model.Product;
import com.hanifshop.productservice.product_service.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Hanif al kamal 10/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public interface ProductDao extends JpaRepository<Product, Integer> {

    Product findByProductId(Long productId);

    List<Product> findByCategory(ProductCategory productCategory);

}
