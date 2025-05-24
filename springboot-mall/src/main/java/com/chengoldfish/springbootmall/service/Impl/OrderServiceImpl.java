package com.chengoldfish.springbootmall.service.Impl;

import com.chengoldfish.springbootmall.dao.OrderDao;
import com.chengoldfish.springbootmall.dao.ProductDao;
import com.chengoldfish.springbootmall.dto.CreateOrderRequest;
import com.chengoldfish.springbootmall.model.BuyItem;
import com.chengoldfish.springbootmall.model.Order;
import com.chengoldfish.springbootmall.model.OrderItem;
import com.chengoldfish.springbootmall.model.Product;
import com.chengoldfish.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

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
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            //根據前端傳來的productId 查詢出的數據 放進 product
            Product product = productDao.getProductById(buyItem.getProductId());

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
