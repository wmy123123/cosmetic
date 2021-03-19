package com.wmy.cosmetic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
    private String product_id;
    private String orderid;
    private int num;//商品数量
    private double product_price;//单价
    private Product product;
}
