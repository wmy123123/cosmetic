package com.wmy.cosmetic.service.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.Exception.ServiceException;
import com.wmy.cosmetic.entity.OrderForm;
import com.wmy.cosmetic.entity.OrderItem;
import com.wmy.cosmetic.entity.Product;
import com.wmy.cosmetic.mapper.OrderFromMapper;
import com.wmy.cosmetic.utils.UuidUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderFromService {
    //定义两种格式文件的后缀
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    @Autowired(required = false)
    OrderFromMapper orderFromMapper;

    @Transactional
    public Double addOrder(List<OrderItem> orderItems){
        String orderId = UuidUtils.getOrderCode();//订单id
        double totalpric = 0;//订单总钱数
        for (OrderItem od: orderItems) {
            totalpric+=od.getNum()*od.getProduct_price();
            od.setOrderid(orderId);
        }
        int status =1;
        Date createdt=new Date();//开始时间
        Date enddt=new Date();//结束时间
        OrderForm orderForm=new OrderForm();
        orderForm.setOrderid(orderId);
        orderForm.setPayment(totalpric);
        orderForm.setStatus(status);
        orderForm.setCreatedt(createdt);
        orderForm.setEnddt(enddt);
        orderFromMapper.addOrderForm(orderForm);
        orderFromMapper.addOrderItem(orderItems);
        return totalpric;
    }

    public PageInfo<OrderForm> findOrderForm(Date startdt, Date enddt, Integer page, Integer limit){
        PageHelper.startPage(page,limit);
        Map<String,Object> param=new HashMap<>();
        param.put("startdt",startdt);
        param.put("enddt",enddt);
        List<OrderForm> orderForm = orderFromMapper.findOrderForm(param);
        PageInfo<OrderForm> pageInfo=new PageInfo<>(orderForm);
        return pageInfo;
    }
    @Transactional
    public void deleteOrderForm(String orderid){
        orderFromMapper.deleteOrderItem(orderid);
        orderFromMapper.deleteOrderForm(orderid);
    }
    public List<OrderForm> findOrderForm(String id){
       return orderFromMapper.findOrderItem(id);
    }

    public PageInfo<Product> prodSellStatistics(Date startdt, Date enddt, Integer page, Integer limit){
        PageHelper.startPage(page,limit);
        Map<String,Object> param=new HashMap<>();
        param.put("startdt",startdt);
        param.put("enddt",enddt);
        List<Product> products = orderFromMapper.prodSellStatistics(param);
        PageInfo<Product> pageInfo=new PageInfo<>(products);
        return pageInfo;
    }

//    1、用HSSFWorkbook打开或者创建"Excel文件对象"
//    2、用HSSFWorkbook返回或者创建sheet对象
//    3、用Sheet对象返回行对象，用行对象得到Cell对象
//    4、对Cell对象读写
    public void importExcel(MultipartFile file) throws IOException, ParseException {
        Workbook workbook=null;
        String filename=file.getOriginalFilename();
        if (filename.endsWith(XLS)){
            workbook=new HSSFWorkbook(file.getInputStream());
        }else if (filename.endsWith(XLSX)){
            workbook=new XSSFWorkbook(file.getInputStream());
        }else{
            throw new ServiceException("文件格式不正确");
        }
        Sheet sheet=workbook.getSheet("Sheet1");
        int rows=sheet.getLastRowNum();
        if (rows==0){
            throw new ServiceException("文件数据为空");
        }
        for (int i = 1; i <=rows+1 ; i++) {
            Row row=sheet.getRow(i);
            //行不为空
            if(row!=null){
                OrderForm orderForm=new OrderForm();
                String orderid=getCellValue(row.getCell(0));
                orderForm.setOrderid(orderid);
                String payment=getCellValue(row.getCell(1));
                orderForm.setPayment(Double.parseDouble(payment));
                String status=getCellValue(row.getCell(2));
                if (!StringUtils.isEmpty(status)){
                    if (status.equals("已支付")){
                        orderForm.setStatus(1);
                    }else if (status.equals("未支付")){
                        orderForm.setStatus(0);
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createdt=getCellValue(row.getCell(3));
                if (!StringUtils.isEmpty(createdt)){
                    Date date=sdf.parse(createdt);
                    orderForm.setCreatedt(date);
                }
                String enddt=getCellValue(row.getCell(4));
                if (!StringUtils.isEmpty(enddt)){
                    Date date=sdf.parse(enddt);
                    orderForm.setEnddt(date);
                }
                orderFromMapper.addOrderForm(orderForm);
            }

        }
    }

    public String getCellValue(Cell cell) {
        String value = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC://数字
                    value = cell.getNumericCellValue() + "";
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        if (date != null) {
                            value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                        } else {
                            value = "";
                        }
                    }else{
                        value=new DecimalFormat("0.00").format(cell.getNumericCellValue());
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING://字符串
                    value=cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN://布尔类型
                    value=cell.getBooleanCellValue()+"";
                    break;
                case HSSFCell.CELL_TYPE_FORMULA://公式
                    value=cell.getCellFormula()+"";
                    break;
                case HSSFCell.CELL_TYPE_BLANK://空值
                    value="";
                    break;
                case HSSFCell.CELL_TYPE_ERROR://故障
                    value="非法字符";
                    break;
                default:
                    value="位置类型";
                    break;
            }
        }
        return value;
    }
}
