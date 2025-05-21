package com.chengoldfish.springbootmall.dao;

import com.chengoldfish.springbootmall.model.Product;

public interface ProductDao {

    Product   getProductById(Integer productId);
}
