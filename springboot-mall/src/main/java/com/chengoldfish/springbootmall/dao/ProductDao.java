package com.chengoldfish.springbootmall.dao;

import com.chengoldfish.springbootmall.constant.ProductCategory;
import com.chengoldfish.springbootmall.dto.ProductQueryParams;
import com.chengoldfish.springbootmall.dto.ProductRequest;
import com.chengoldfish.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    Integer countProduct(ProductQueryParams params);

    List<Product> getProducts(ProductQueryParams params);

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void updateStock(Integer productId, Integer stock);

    void deletProductById(Integer productId);
}
