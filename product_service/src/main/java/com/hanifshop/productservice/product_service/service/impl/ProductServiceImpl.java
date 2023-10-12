package com.hanifshop.productservice.product_service.service.impl;

import com.hanifshop.productservice.product_service.dto.ProductDto;
import com.hanifshop.productservice.product_service.stream.KafkaProducer;
import com.hanifshop.productservice.product_service.model.Product;
import com.hanifshop.productservice.product_service.model.ProductCategory;
import com.hanifshop.productservice.product_service.repository.ProductCategoryDao;
import com.hanifshop.productservice.product_service.repository.ProductDao;
import com.hanifshop.productservice.product_service.service.ProductService;
import com.hanifshop.productservice.product_service.util.Constant;
import com.hanifshop.productservice.product_service.util.EngineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    ProductDao productDao;

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
            ProductCategory productCategory = productCategoryDao.findByCategoryId(categoryId);
            checkCategoryAvailability(categoryId);

            productCategoryDao.delete(productCategory);

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

            checkCategoryAvailability(categoryId);

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

    @Override
    public Map<String, Object> AddProduct(ProductDto dto) {
        try {

            ProductCategory productCategory = checkCategoryAvailability(dto.getCategoryId());

            Product product = new Product();
            product.setProductName(dto.getProductName());
            product.setCategory(productCategory);
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());
            product.setStockQuantity(dto.getStockQuantity());
            product.setCreatedAt(new Date());
            product.setUpdatedAt(new Date());
            productDao.save(product);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.addProduct);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.addProduct);
        }
    }

    @Override
    public Map<String, Object> DeleteProduct(ProductDto dto) {
        try {

            Product product = checkProductAvailability(dto.getProductId());
            productDao.delete(product);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.deleteProduct);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.deleteProduct);
        }
    }

    @Override
    public Map<String, Object> UpdateProduct(ProductDto dto) {
        try {


            Product product = checkProductAvailability(dto.getProductId());
            ProductCategory productCategory =
                    dto.getCategoryId() != 0L ? checkCategoryAvailability(dto.getCategoryId()) :
                            product.getCategory();

            product.setProductName(dto.getProductName().isEmpty() ? product.getProductName() : dto.getProductName());
            product.setCategory(productCategory);
            product.setPrice(dto.getPrice() != 0L ? dto.getPrice() : product.getPrice());
            product.setDescription(dto.getDescription().isEmpty() ? product.getDescription() : dto.getDescription());
            product.setStockQuantity(dto.getStockQuantity() != 0 ? dto.getStockQuantity() : product.getStockQuantity());
            product.setUpdatedAt(new Date());
            productDao.save(product);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.updateProduct);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.updateProduct);
        }
    }

    @Override
    public Map<String, Object> ListProduct(ProductDto dto) {
        try {
            List<Product> listProduct = dto.getCategoryId() != 0L ?
                    productDao.findByCategory(checkCategoryAvailability(dto.getCategoryId())) :
                    productDao.findAll();

            if (listProduct.isEmpty()){
                return EngineUtils.createSuccessReponse(200, "Product is empty", Constant.ControllerRoute.allCategory);
            }

            List<Map<String, Object>> productList = listProduct.stream()
                    .map(product -> {
                        Map<String, Object> categoryMap = new HashMap<>();
                        categoryMap.put("productId", product.getProductId());
                        categoryMap.put("productName", product.getProductName());
                        categoryMap.put("description", product.getDescription());
                        categoryMap.put("price", product.getPrice());
                        categoryMap.put("stockQuantity", product.getStockQuantity());
                        categoryMap.put("category", product.getCategory());
                        categoryMap.put("lastUpdate", product.getUpdatedAt());
                        return categoryMap;
                    })
                    .collect(Collectors.toList());

            return EngineUtils.createSuccessReponse(200, productList, Constant.ControllerRoute.allProduct);

        }catch (Exception e){
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.allProduct);
        }
    }

    @Override
    public Boolean isQtyProductValid(Long productId, int requestedQty) {
        try {
            Product product = productDao.findByProductId(productId);
            return product.getStockQuantity() >= requestedQty;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    ProductCategory checkCategoryAvailability(Long categoryId) throws Exception {
        ProductCategory productCategory = productCategoryDao.findByCategoryId(categoryId);

        if (productCategory == null)
            throw new Exception("Category not found");

        return productCategory;
    }

    Product checkProductAvailability(Long productId) throws Exception {
        Product product = productDao.findByProductId(productId);

        if (product == null)
            throw new Exception("Product not found");

        return product;
    }
}
