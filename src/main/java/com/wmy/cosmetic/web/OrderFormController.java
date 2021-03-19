package com.wmy.cosmetic.web;

import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.entity.OrderForm;
import com.wmy.cosmetic.entity.OrderItem;
import com.wmy.cosmetic.entity.Result;
import com.wmy.cosmetic.service.ServiceImpl.OrderFromService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("order")
public class OrderFormController {
    private final Logger logger=LoggerFactory.getLogger(OrderFormController.class);
    @Autowired
    OrderFromService orderFromService;
    @RequestMapping("addOrder")
    @ResponseBody
    public Result<String> addOrder(@RequestBody List<OrderItem> orderItems){
        Result<String> result=new Result<>();
        result.setCode(0);//默认交易成功
        try {
            orderFromService.addOrder(orderItems);
            result.setMsg("下单成功");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("下单失败");
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("findOrderForm")
    public Result<OrderForm> findOrderForm(@RequestParam(required = false) String startdt , @RequestParam(required = false) String enddt,@RequestParam(required = false,value = "page")Integer pageNumber,
                                           @RequestParam(required = false,value = "limit")Integer limit){
        Result<OrderForm> result=new Result<>();
        result.setCode(0);//默认交易成功
        try {
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
            PageInfo<OrderForm> orderForm = orderFromService.findOrderForm(date1,date2,pageNumber,limit);
            result.setMsg("查询成功");
            result.setCount(orderForm.getTotal());
            result.setData(orderForm.getList());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result.setCode(1);//默认交易成功
            result.setMsg("查询失败");
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("deleteOrderForm")
    public Result<OrderForm> deleteOrderForm(@RequestParam(required = false) String orderid){
        Result<OrderForm> result=new Result<>();
        result.setCode(0);//默认交易成功
        try {
            orderFromService.deleteOrderForm(orderid);
            result.setMsg("删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result.setCode(1);//默认交易成功
            result.setMsg("删除失败");
        }
        return result;
    }
    @RequestMapping("findOrderItem")
    public String findOrderItem(@RequestParam(required = false) String orderid, Model model){
        Result<OrderForm> result=new Result<>();
        result.setCode(0);//默认交易成功
        try {
            List<OrderForm> orderForm = orderFromService.findOrderForm(orderid);
            result.setMsg("查询成功");
            result.setData(orderForm);
            model.addAttribute("orderForm",orderForm);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result.setCode(1);//默认交易成功
            result.setMsg("查询失败");
        }
        return "member/orderItem";
    }
    @ResponseBody
    @RequestMapping("importExcel")
    public Result<OrderForm> importExcel(@RequestParam("file") MultipartFile myFile){
        Result<OrderForm> result=new Result<>();
        result.setCode(0);//默认交易成功
        try {
            orderFromService.importExcel(myFile);
            result.setMsg("导入成功");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result.setCode(1);//默认交易成功
            result.setMsg("导入失败");
        }
        return result;
    }
    @ResponseBody
    @RequestMapping("ExportExcel")
    public Result<OrderForm> ExportExcel(@RequestParam("file") MultipartFile myFile){
        Result<OrderForm> result=new Result<>();
        result.setCode(0);//默认交易成功
        try {
            orderFromService.importExcel(myFile);
            result.setMsg("导入成功");
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result.setCode(1);//默认交易成功
            result.setMsg("导入失败");
        }
        return result;
    }
}
