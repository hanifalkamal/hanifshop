package com.hanifshop.productservice.product_service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hanif al kamal 11/10/2023
 * @contact hanif.alkamal@gmail.com
 */

@Setter
@Getter
public class ProductCategoryDto extends BaseDto{

    private Long categoryId = 0L;
    private String categoryName = "";

}
