package com.chengoldfish.springbootmall.service;

import com.chengoldfish.springbootmall.dto.CreateOrderRequest;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
