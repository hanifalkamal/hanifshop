package com.hanifshop.productservice.product_service.service.impl;

import com.hanifshop.productservice.product_service.model.ProductCategory;
import com.hanifshop.productservice.product_service.repository.ProductCategoryDao;
import com.hanifshop.productservice.product_service.service.ProductService;
import com.hanifshop.productservice.product_service.util.Constant;
import com.hanifshop.productservice.product_service.util.EngineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Hanif al kamal 10/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Override
    public Map<String, Object> AddProductCategory(String categoryName) {
        try {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setCategoryName(categoryName);
            productCategoryDao.save(productCategory);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.addCategory);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.addCategory);
        }
    }

    @Override
    public Map<String, Object> DeleteProductCategory(Long categoryId) {

        try {
            productCategoryDao.deleteByCategoryId(categoryId);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.deleteCategory);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.deleteCategory);
        }
    }

    @Override
    public Map<String, Object> UpdateProductCategory(Long categoryId, String categoryName) {
        try {
            ProductCategory productCategory = productCategoryDao.findByCategoryId(categoryId);
            productCategory.setCategoryName(categoryName);
            productCategoryDao.save(productCategory);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.updateCategory);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.updateCategory);
        }
    }

    @Override
    public Map<String, Object> ListProductCategory() {
        try {
            List<ProductCategory> listCategory = productCategoryDao.findAll();

            if (listCategory.isEmpty()){
                return EngineUtils.createSuccessReponse(200, "Product Category is empty", Constant.ControllerRoute.allCategory);
            }

            List<Map<String, Object>> categoryList = listCategory.stream()
                    .map(category -> {
                        Map<String, Object> categoryMap = new HashMap<>();
                        categoryMap.put("categoryId", category.getCategoryId());
                        categoryMap.put("categoryName", category.getCategoryName());
                        return categoryMap;
                    })
                    .collect(Collectors.toList());

            return EngineUtils.createSuccessReponse(200, categoryList, Constant.ControllerRoute.allCategory);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.allCategory);
        }
    }
}
