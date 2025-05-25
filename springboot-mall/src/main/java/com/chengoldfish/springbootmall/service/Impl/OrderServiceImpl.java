package com.chengoldfish.springbootmall.service.Impl;

import com.chengoldfish.springbootmall.dao.OrderDao;
import com.chengoldfish.springbootmall.dao.ProductDao;
import com.chengoldfish.springbootmall.dao.UserDao;
import com.chengoldfish.springbootmall.dto.CreateOrderRequest;
import com.chengoldfish.springbootmall.dto.OrderQueryParams;
import com.chengoldfish.springbootmall.model.*;
import com.chengoldfish.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        //針對每一個order 都針對一個oderItems，將其放到orderList
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        //order擴充List
        //因為一張訂單可能包含了許多order Item，把orderItemList加到Order裡
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        //oreder現在 包含 訂單總資訊 + 這筆訂單購買了哪些商品
        return order;

    }

    @Transactional //修改多張表格_一定要加 : 確保兩張表格同時新增 或 同時失敗
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //檢查user是否存在
        User user = userDao.getUserById(userId);

        if(user == null) {
            logger.warn("該 userId {} 不存在"+userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }


        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            //根據前端傳來的productId 查詢出的數據 放進 product
            Product product = productDao.getProductById(buyItem.getProductId());

            //檢查product是否存在，stock是否足夠
            if (product == null) {
                logger.warn("商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < buyItem.getQuantity()){
                logger.warn("商品 {} 庫存不足，無法購買，剩餘庫存 {} ，欲購買數量 {}",
                        buyItem.getProductId(),product.getStock(),buyItem.getQuantity());

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            }
            //扣除庫存
            productDao.updateStock(product.getProductId(),product.getStock() - buyItem.getQuantity());

            //計算總價
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            //轉換BuyItem to OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);


        }


        //新建訂單
        Integer orderId = orderDao.createOrder(userId,totalAmount);

        orderDao.createOrderItems(orderId,orderItemList);

        return orderId;
    }
}
