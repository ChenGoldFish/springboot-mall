package com.chengoldfish.springbootmall.service;

import com.chengoldfish.springbootmall.dto.CreateOrderRequest;
import com.chengoldfish.springbootmall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);


}
