package com.wmy.cosmetic.service.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.glassfish.external.statistics.Statistic;
import com.wmy.cosmetic.entity.DailyAccount;
import com.wmy.cosmetic.entity.Product;
import com.wmy.cosmetic.entity.ProductType;
import com.wmy.cosmetic.entity.Statistics;
import com.wmy.cosmetic.mapper.ProductMapper;
import com.wmy.cosmetic.service.ProductService;
import com.wmy.cosmetic.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        List<Statistics> re = productMapper.accountaccountWeekend(startdt, entdt);
        List<DailyAccount> list=new ArrayList<>();
        for (int i = 0; i < pastDateTime.size(); i++) {
            String dt=pastDateTime.get(i);
            String month=dt.substring(5,7);
            String day=dt.substring(8,10);
            dailyAccount=new DailyAccount();
            dailyAccount.setDate(month+"."+day);
            for (int j = 0; j <re.size() ; j++) {
                 if (day.equals(re.get(j).getDate())){
                     dailyAccount.setMoney(re.get(j).getMoney());
                 }
            }
            if (StringUtils.isEmpty(dailyAccount.getMoney())){
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
    public List<ProductType> productTypeList(Integer id) {
        return productMapper.productTypeList1(id);
    }

    @Override
    public ProductType findProductId(Integer id) {
        return productMapper.findProductType(id);
    }

    @Override
    public void deleteProduct(Integer id) {
        productMapper.deleteProduct(id);
    }

    @Override
    public void addProductType(ProductType productType) {
        productMapper.addProductType(productType);
    }

    @Override
    public void updateProductType(ProductType productType) {
       productMapper.updateProductType(productType);
    }

    @Override
    public void deleteProductType(Integer typeid) {
       productMapper.deleteProductType(typeid);
    }

}
