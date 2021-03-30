package com.wmy.cosmetic.service;

import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.entity.DailyAccount;
import com.wmy.cosmetic.entity.Product;
import com.wmy.cosmetic.entity.ProductType;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface ProductService {
    PageInfo<Product> getProductList(Integer product_id, String product_name,Integer product_type ,int page, int number);

    int addProduct(Product product);
    Product findProductById(int product_id);
    void updateProduct(Product product);
    void updateProductImg(int id,String path);
    PageInfo<Product> account(Integer page, Integer limit);
    List<DailyAccount> accountaccountWeekend() throws ParseException;
    List<Object> accountConsole();
    List<ProductType> productTypeList();
    List<ProductType> productTypeList(Integer id);
    ProductType findProductId(Integer id);
    void deleteProduct(Integer id);
    void addProductType(ProductType productType);
    void updateProductType(ProductType productType);
    void deleteProductType(Integer typeid);
}
