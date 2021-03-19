package com.wmy.cosmetic.web;

import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.entity.DailyAccount;
import com.wmy.cosmetic.entity.Product;
import com.wmy.cosmetic.entity.Result;
import com.wmy.cosmetic.service.ProductService;
import com.wmy.cosmetic.service.ServiceImpl.OrderFromService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("StatisticsController")
public class StatisticsController {
    private final Logger logger=LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private OrderFromService orderFromService;
    @Autowired
    private ProductService productService;
    @RequestMapping("StatisticsNum")
    @ResponseBody
    public Result<Product> StatisticsNum(@RequestParam(required = false,value = "startdt") String startdt , @RequestParam(required = false,value = "enddt") String enddt, @RequestParam(required = false,value = "page")Integer pageNumber,
                                         @RequestParam(required = false,value = "limit")Integer limit){
        Result<Product> result=new Result<>();
        result.setCode(0);
        try{
            Date date1=null;
            Date date2=null;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!StringUtils.isEmpty(startdt)){
                startdt=startdt+" 00:00:00";
                date1= sdf.parse(startdt);
            }
            if (!StringUtils.isEmpty(enddt)){
                enddt=enddt+" 23:59:59";
                date2= sdf.parse(enddt);
            }
            PageInfo<Product> productPageInfo = orderFromService.prodSellStatistics(date1, date2, pageNumber, limit);
            result.setCount(productPageInfo.getTotal());
            result.setData(productPageInfo.getList());
            result.setMsg("查询成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("信息查询失败");
        }
        return result;
    }
    @ResponseBody
    @GetMapping("account")
    public Result<Product> account(@RequestParam(value = "page") Integer page, @RequestParam(value = "limit")Integer limit){
        Result<Product> result=new Result<>();
        result.setCode(0);//默认交易成功
        try{
            PageInfo<Product> account = productService.account(page, limit);
            result.setData(account.getList());
            result.setCount(account.getTotal());
            result.setMsg("查询成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("查询失败");
        }
        return result;
    }
    @ResponseBody
    @GetMapping("accountWeekend")
    public Result<DailyAccount> accountWeekend(){
        Result<DailyAccount> result=new Result<>();
        result.setCode(0);//默认交易成功
        try{
            List<DailyAccount> list = productService.accountaccountWeekend();
            result.setMsg("查询成功");
            result.setData(list);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("查询失败");
        }
        return result;
    }
    @ResponseBody
    @GetMapping("accountConsole")
    public Result<Object> accountConsole(){
        Result<Object> result=new Result<>();
        result.setCode(0);//默认交易成功
        try{
            List<Object> list = productService.accountConsole();
            result.setMsg("查询成功");
            result.setData(list);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("查询失败");
        }
        return result;
    }

}
