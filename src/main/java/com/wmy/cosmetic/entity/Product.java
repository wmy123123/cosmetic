package com.wmy.cosmetic.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private int product_id;
    private String product_name;
    private String product_img;
    private String product_account;
    private double product_price;
    private int  product_type;
    private String  product_message;
    private Date saledt;
    private long saleNum;
}
