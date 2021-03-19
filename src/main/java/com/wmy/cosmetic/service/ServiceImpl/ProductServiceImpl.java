package com.wmy.cosmetic.service.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.entity.DailyAccount;
import com.wmy.cosmetic.entity.Product;
import com.wmy.cosmetic.entity.ProductType;
import com.wmy.cosmetic.mapper.ProductMapper;
import com.wmy.cosmetic.service.ProductService;
import com.wmy.cosmetic.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired(required = false)
    ProductMapper productMapper;
    @Override
    public PageInfo<Product> getProductList(Integer product_id, String product_name,Integer product_type ,int page, int number) {
        PageHelper.startPage(page,number);
        List<Product> productList = productMapper.findProductList(product_id, product_name,product_type,page, number);
        PageInfo<Product> pageHelper=new PageInfo<>(productList);
        return pageHelper;
    }

    @Override
    public int addProduct(Product product) {
       return   productMapper.insertProduct(product);
    }

    @Override
    public Product findProductById(int product_id) {
      return   productMapper.findProductById(product_id);
    }

    @Override
    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    @Override
    public void updateProductImg(int id, String path) {
        productMapper.updateProImg(id,path);
    }

    @Override
    public PageInfo<Product> account(Integer page, Integer limit) {
        PageHelper.startPage(page,limit);
        List<Product> account = productMapper.account();
        PageInfo<Product> pageInfo=new PageInfo<>(account);
        return pageInfo;
    }

    @Override
    public List<DailyAccount> accountaccountWeekend() throws ParseException {
        DailyAccount dailyAccount=null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> pastDateTime=DateUtils.getPastDateTime(6);
        Date startdt = DateUtils.getStartByDate(simpleDateFormat.parse(pastDateTime.get(0)));
        Date entdt = DateUtils.getEndByDate(simpleDateFormat.parse(pastDateTime.get(pastDateTime.size() - 1)));
        List<Double> doubles = productMapper.accountaccountWeekend(startdt, entdt);
        List<DailyAccount> list=new ArrayList<>();
        for (int i = 0; i < pastDateTime.size(); i++) {
            dailyAccount=new DailyAccount();
            String dt = pastDateTime.get(i);
            dailyAccount.setDate(dt.substring(5,7)+"."+ dt.substring(8,10));
            if(i<=doubles.size()-1){
                dailyAccount.setMoney(doubles.get(i));
            }else {
                dailyAccount.setMoney(0);
            }
            list.add(dailyAccount);
        }
        return list;
    }

    @Override
    public List<Object> accountConsole() {
        List<Object> list = new ArrayList<>();
        list.add(productMapper.ordernum());
        list.add(productMapper.productnum());
        list.add(productMapper.employeenum());
        Double money = productMapper.incomeWithYear(DateUtils.getCurrentYearStartTime(), DateUtils.getCurrentYearEndTime());
        list.add(money);
        return list;
    }

    @Override
    public List<ProductType> productTypeList() {
        return productMapper.productTypeList();
    }

    @Override
    public void deleteProduct(Integer id) {
        productMapper.deleteProduct(id);
    }

}
