package com.wmy.cosmetic.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderForm {
    private String orderid;//订单号
    private double payment;//支付金额
    private int status;//交易状态
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdt;//开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enddt;//结束时间
    private List<OrderItem> orderItem;
}
