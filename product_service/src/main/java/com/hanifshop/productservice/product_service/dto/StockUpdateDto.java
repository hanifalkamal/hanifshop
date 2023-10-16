package com.hanifshop.productservice.product_service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hanif al kamal 16/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Getter
@Setter
public class StockUpdateDto {
    private Long productId = 0L;
    private Integer stockQuantity = 0;
}
