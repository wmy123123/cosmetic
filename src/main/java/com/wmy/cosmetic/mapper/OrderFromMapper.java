package com.wmy.cosmetic.mapper;

import com.wmy.cosmetic.entity.OrderForm;
import com.wmy.cosmetic.entity.OrderItem;
import com.wmy.cosmetic.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface OrderFromMapper {
    void addOrderForm(OrderForm orderForm);
    void addOrderItem(List<OrderItem> orderItems);
    List<OrderForm> findOrderForm(Map<String,Object> param);
    void deleteOrderForm(String orderid);
    void deleteOrderItem(String orderid);
    List<OrderForm> findOrderItem(String orderid);
    List<Product> saleMessage(Map<String,Object> param);
    List<Product> prodSellStatistics(Map<String,Object> param);
}
