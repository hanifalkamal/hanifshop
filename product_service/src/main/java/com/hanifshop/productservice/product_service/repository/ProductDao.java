package com.hanifshop.productservice.product_service.repository;

import com.hanifshop.productservice.product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Hanif al kamal 10/10/2023
 * @contact hanif.alkamal@gmail.com
 */
public interface ProductDao extends JpaRepository<Product, Integer> {
}
