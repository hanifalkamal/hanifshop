package com.hanifshop.productservice.product_service.service.impl;

import com.hanifshop.productservice.product_service.dto.ProductDto;
import com.hanifshop.productservice.product_service.dto.StockUpdateDto;
import com.hanifshop.productservice.product_service.stream.KafkaConsumer;
import com.hanifshop.productservice.product_service.stream.KafkaProducer;
import com.hanifshop.productservice.product_service.model.Product;
import com.hanifshop.productservice.product_service.model.ProductCategory;
import com.hanifshop.productservice.product_service.repository.ProductCategoryDao;
import com.hanifshop.productservice.product_service.repository.ProductDao;
import com.hanifshop.productservice.product_service.service.ProductService;
import com.hanifshop.productservice.product_service.util.Constant;
import com.hanifshop.productservice.product_service.util.EngineUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.lang.IgniteBiPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Hanif al kamal 10/10/2023
 * @contact hanif.alkamal@gmail.com
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    ProductDao productDao;

//    @Autowired
//    private Ignite ignite;

    @Override
    public Map<String, Object> AddProductCategory(String categoryName) {
        try {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setCategoryName(categoryName);
            productCategoryDao.save(productCategory);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.addCategory);

        } catch (Exception e) {
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

        } catch (Exception e) {
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

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.updateCategory);
        }
    }

    @Override
    public Map<String, Object> ListProductCategory() {
        try {
            List<ProductCategory> listCategory = productCategoryDao.findAll();

            if (listCategory.isEmpty()) {
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

        } catch (Exception e) {
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

            //Publish Stock
            publishStockUpdate(dto.getProductId(), dto.getStockQuantity());


            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.addProduct);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.addProduct);
        }
    }

    @Override
    public Map<String, Object> DeleteProduct(ProductDto dto) {
        try {

            Product product = checkProductAvailability(dto.getProductId());
            productDao.delete(product);

            //Publish Stock
            publishStockUpdate(product.getProductId(), 0);

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.deleteProduct);

        } catch (Exception e) {
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

            //Publish Stock
            publishStockUpdate(product.getProductId(), product.getStockQuantity());

            return EngineUtils.createSuccessReponse(200, "success", Constant.ControllerRoute.updateProduct);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.updateProduct);
        }
    }

    @Override
    public Map<String, Object> ListProduct(ProductDto dto) {
        try {
            List<Product> listProduct =
                    dto.getCategoryId() != 0L && dto.getProductId() != 0L ?
                            productDao.findByProductIdAndCategory(dto.getProductId(), checkCategoryAvailability(dto.getCategoryId())) :
                            dto.getProductId() != 0L ?
                                    productDao.findByProductId(dto.getProductId()) :
                                    dto.getCategoryId() != 0L ?
                                            productDao.findByCategory(checkCategoryAvailability(dto.getCategoryId())) :
                                            productDao.findAll();

            if (listProduct.isEmpty()) {
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

                        //Publish Stock
                        publishStockUpdate(product.getProductId(), product.getStockQuantity());

                        return categoryMap;
                    })
                    .collect(Collectors.toList());


            return EngineUtils.createSuccessReponse(200, productList, Constant.ControllerRoute.allProduct);

        } catch (Exception e) {
            e.printStackTrace();
            return EngineUtils.createFailedReponse(500, e.getMessage(), Constant.ControllerRoute.allProduct);
        }
    }

    public void publishStockUpdate(Long productId, Integer newStockValue) {
//        logger.info("Attempt to publish stock with ignite");

//        IgniteMessaging messaging = ignite.message();
//
//        StockUpdateDto stok = new StockUpdateDto();
//        stok.setProductId(productId);
//        stok.setStockQuantity(newStockValue);
//
//        messaging.send("stockUpdateTopic", stok);
    }

    @Override
    public Map<String, Object> showStockCache() {
//        IgniteMessaging messaging = ignite.message();
//        List<Map<String, Object>> listMap = new ArrayList<>();
//
//        IgniteBiPredicate<UUID, StockUpdateDto> listener = (nodeId, message) -> {
//            Long productId = message.getProductId();
//            Integer newStockValue = message.getStockQuantity();
//
//            logger.info("prodcutId = " + productId);
//            logger.info("newStockValue = " + newStockValue);
//
//            Map<String, Object> map = new HashMap<>();
//
//            map.put("prodcutId", productId);
//            map.put("stock", newStockValue);
//
//            listMap.add(map);
//            return true;
//        };
//
//        messaging.remoteListen("stockUpdateTopic", listener);

//        return EngineUtils.createSuccessReponse(200, listMap, "/Cache");
        return null;

    }

    @Override
    public Boolean isQtyProductValid(Long productId, int requestedQty) {
        try {
            Product product = productDao.findByProductId(productId).get(0);
            return product.getStockQuantity() >= requestedQty;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getProduct(Long productId) {
        try {
            return productDao.findByProductId(productId).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    ProductCategory checkCategoryAvailability(Long categoryId) throws Exception {
        ProductCategory productCategory = productCategoryDao.findByCategoryId(categoryId);

        if (productCategory == null)
            throw new Exception("Category not found");

        return productCategory;
    }

    Product checkProductAvailability(Long productId) throws Exception {
        Product product = productDao.findByProductId(productId).get(0);

        if (product == null)
            throw new Exception("Product not found");

        return product;
    }


}
