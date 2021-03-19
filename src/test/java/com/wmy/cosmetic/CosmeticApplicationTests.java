package com.wmy.cosmetic;

import com.wmy.cosmetic.mapper.EmployeeMapper;
import com.wmy.cosmetic.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class CosmeticApplicationTests {
    @Autowired(required = false)
    ProductMapper productMapper;
    @Autowired(required = false)
    EmployeeMapper productMapper1;
    @Test
    void contextLoads() throws ParseException {
        String tablename="t_employee";
        String date="2021-02-09";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        productMapper.delete(tablename,sdf.parse(date));
    }
    @Test
    public void test(){
        productMapper1.insertProductType(1,"馒头");
    }
}
