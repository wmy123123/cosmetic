package com.wmy.cosmetic.mapper;

import com.wmy.cosmetic.entity.Product;
import com.wmy.cosmetic.entity.ProductType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
@Mapper
public interface ProductMapper {
    List<Product> findProductList(Integer product_id, String product_name,Integer product_type,int page, int number);
    int insertProduct(Product product);
    Product findProductById(int product_id);
    void updateProduct(Product product);
    void delete(@Param("name") String table,@Param("time") Date entrytime);
    void updateProImg(@Param("product_id") int id,@Param("product_img") String path);
    List<Product> account();
    List<Double> accountaccountWeekend(@Param("startdt")Date startdt,@Param("entdt") Date entdt);
    Integer ordernum();
    Integer productnum();
    Integer employeenum();
    Double  incomeWithYear(@Param("startdt")Date startdt,@Param("enddt") Date enddt);
    List<ProductType> productTypeList();
    void deleteProduct(Integer product_id);
}
