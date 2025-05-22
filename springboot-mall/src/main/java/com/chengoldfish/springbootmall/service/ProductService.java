package com.chengoldfish.springbootmall.service;

import com.chengoldfish.springbootmall.dto.ProductRequest;
import com.chengoldfish.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
}
