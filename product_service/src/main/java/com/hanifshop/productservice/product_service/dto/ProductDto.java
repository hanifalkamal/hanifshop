package com.hanifshop.productservice.product_service.dto;

import com.hanifshop.productservice.product_service.model.Product;
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

    public static ProductDto fromProduct(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategoryId(product.getCategory().getCategoryId());
        return dto;
    }

    public Product toProduct() {
        Product product = new Product();

        product.setProductId(this.getProductId());
        product.setProductName(this.getProductName());
        product.setDescription(this.getDescription());
        product.setPrice(this.getPrice());
        product.setStockQuantity(this.getStockQuantity());
        product.setCategory(product.getCategory());
        return product;
    }

}
