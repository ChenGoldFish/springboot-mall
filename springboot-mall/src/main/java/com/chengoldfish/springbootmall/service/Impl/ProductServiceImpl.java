package com.chengoldfish.springbootmall.service.Impl;

import com.chengoldfish.springbootmall.constant.ProductCategory;
import com.chengoldfish.springbootmall.dao.ProductDao;
import com.chengoldfish.springbootmall.dto.ProductQueryParams;
import com.chengoldfish.springbootmall.dto.ProductRequest;
import com.chengoldfish.springbootmall.model.Product;
import com.chengoldfish.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Integer countProduct(ProductQueryParams params) {
        return productDao.countProduct(params);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams params) {

        return productDao.getProducts(params);
    }

    @Override
    public Product getProductById(Integer productId) {

        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId,productRequest);
    }

    @Override
    public void deletProductById(Integer productId) {

        productDao.deletProductById(productId);
    }
}
