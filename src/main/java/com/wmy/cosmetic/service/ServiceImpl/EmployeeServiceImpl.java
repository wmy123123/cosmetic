package com.wmy.cosmetic.service.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.Exception.ServiceException;
import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.OrderForm;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.entity.Role;
import com.wmy.cosmetic.mapper.EmployeeMapper;
import com.wmy.cosmetic.service.EmployeeService;
import com.wmy.cosmetic.utils.SaltUtils;
import com.wmy.cosmetic.utils.ZipUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
    //定义两种格式文件的后缀
    private final static String XLS = "xls";
    private final static String XLSX = "xlsx";

    @Autowired(required = false)
    private EmployeeMapper employeeMapper;
    @Override
    public Employee login(String username) {
        if (username==null){
            return null;
        }
        return employeeMapper.findByUsernameAndPassword(username);
    }

    @Override
    public PageInfo<Employee> findEmployeeList(Integer uuid, String name, String position, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Map<String, Object> param = new HashMap<>();
        param.put("uuid",uuid);
        param.put("name",name);
        param.put("position",position);
        List<Employee> employees= employeeMapper.findEmployeeList(param);
        PageInfo<Employee> pageHelper=new PageInfo<>(employees);
        return pageHelper;
    }
    public int addEmployee(Employee employee) {
        if (ObjectUtils.isEmpty(employee)){
            return 0;
        }
        Employee em = employeeMapper.findByUsernameAndPassword(employee.getUsername());
        if (!ObjectUtils.isEmpty(em)){
            return 0;
        }
        employee.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        employee.setEntrytime(new Date());
        employee.setUuid(SaltUtils.getEmployeeNumber());
        String salt = SaltUtils.getSalt(8);
        employee.setSalt(salt);
        employee.setStatus("在职");
        Md5Hash md5Hash =new Md5Hash(employee.getPassword(),salt,1024);
        employee.setPassword(md5Hash.toHex());
        return employeeMapper.addEmployee(employee);
    }

    @Override
    public List<Perm> findPermsByEmployeeName(String username) {
        if (ObjectUtils.isEmpty(username)){
            throw new ServiceException("参数为空");
        }
        return employeeMapper.findPermsByEmployeeName(username);
    }

    @Override
    public Employee findEmployeeById(Integer id) {
        if (!ObjectUtils.isEmpty(id)){
            return employeeMapper.findEmployeeById(id);
        }
        return null;
    }

    @Override
    public int updateEmploy(Employee employee) {
        if (ObjectUtils.isEmpty(employee)){
            return 0;
        }else{
//            String salt = SaltUtils.getSalt(8);
//            Md5Hash md5Hash = new Md5Hash(employee.getPassword(),salt,1024);
//            employee.setSalt(salt);
//            employee.setPassword(md5Hash.toHex());
            return employeeMapper.updateEmploy(employee);
        }
    }

    @Override
    public void updateEmploy1(Employee employee) {
        employeeMapper.updateEmploy1(employee);
    }

    @Override
    public int deleteEmployee(Integer id) {
        if (ObjectUtils.isEmpty(id)){
            return 0;
        }
        return employeeMapper.deleteEmployee(id);
    }

    @Override
    public int updatePassword(String uuid, String password) {
        if (uuid!=null&&password!=null){
           return  employeeMapper.updatePassword(uuid, password);
        }
        return 0;
    }

    @Override
    public void deleteEmpById(List<String> uuids) {
       employeeMapper.deleteEmpById(uuids);
    }

    @Override
    public List<Role> findAllRole() {
        return employeeMapper.findAllRole();
    }

    @Override
    public void updateAvatarImgPath(String uuid, String imgUrl) {
        employeeMapper.updateAvatarImgPath(uuid,imgUrl);
    }

    @Override
    public PageInfo<Role> roleList(Integer id,Integer pageNumber,Integer limit) {
        PageHelper.startPage(pageNumber,limit);
        List<Role> roles = employeeMapper.roleList(id);
        PageInfo<Role> roleList =new PageInfo<>(roles);
        return roleList;
    }

    @Override
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
        for (int i = 4; i <= rows ; i++) {
            Row row=sheet.getRow(i);
            //行不为空
            if(row!=null){
                Employee employee=new Employee();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String uuid= getCellValue(row.getCell(0));
                employee.setUuid(uuid);
                String username= getCellValue(row.getCell(1));
                employee.setUsername(username);
                String password= getCellValue(row.getCell(2));
                employee.setPassword(password);
                String name= getCellValue(row.getCell(3));
                employee.setName(name);
                String position= getCellValue(row.getCell(4));
                employee.setPosition(position);
                String entrytime = getCellValue(row.getCell(5));
                Date date1=null;
                Date date2=null;
                if (!StringUtils.isEmpty(entrytime)){
                    date1=sdf.parse(entrytime);
                }
                employee.setEntrytime(date1);
                String resignationtime=getCellValue(row.getCell(6));
                if (!StringUtils.isEmpty(resignationtime)){
                    date2=sdf.parse(resignationtime);
                }
                employee.setResignationtime(date2);
                Double salary= Double.valueOf(getCellValue(row.getCell(7)));
                employee.setSalary(salary);
                String avatar= getCellValue(row.getCell(8));
                employee.setAvatar(avatar);
                String salt= getCellValue(row.getCell(9));
                employee.setSalt(salt);
                String phone= getCellValue(row.getCell(10));
                employee.setPhone(phone);
                String sex= getCellValue(row.getCell(11));
                employee.setSex(sex);
                String status= getCellValue(row.getCell(12));
                employee.setStatus(status);
                employeeMapper.addEmployee(employee);
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
                    value="未知类型";
                    break;
            }
        }
        return value;
    }
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
        // TODO Auto-generated method stub
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        //设置行高
        return style;
    }
    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {
        //1.创建工作簿
        HSSFWorkbook wb=new HSSFWorkbook();
        //1.1创建合并单元格对象
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,1,0,12);//起始行,结束行,起始列,结束列
        //1.2头标题样式
        HSSFCellStyle headStyle = createCellStyle(wb,(short)16);
        //1.3列标题样式
        HSSFCellStyle style = createCellStyle(wb,(short)12);
        //2.创建工作表
        HSSFSheet sheet=wb.createSheet("Sheet1");
        //2.1加载合并单元格对象
        sheet.addMergedRegion(callRangeAddress);
        //设置默认列宽
        sheet.setDefaultColumnWidth(10);
        //3.创建行
        //3.1创建头标题行;并且设置头标题
        HSSFRow head=sheet.createRow(0);
        HSSFRow head2=sheet.createRow(1);
        head.setHeight((short) (20*30));
        head2.setHeight((short) (20*30));
        HSSFCell headCell= head.createCell(0);
        //加载单元格样式并赋值
        headCell.setCellStyle(headStyle);
        headCell.setCellValue("用户列表");
        //3.2创建列标题;并且设置列标题
        HSSFRow row=sheet.createRow(2);
        row.setHeight((short) (20*25));
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("员工编号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("账号");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("密码");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("职位");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("入职时间");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("离职时间");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("工资");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("证件照路径");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("密码盐");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("电话");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("性别");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("状态");
        cell.setCellStyle(style);
        Map<String,Object> map=new HashMap<>();
        map.put("uuid",null);
        map.put("name",null);
        map.put("position",null);
        List<Employee> list= employeeMapper.findEmployeeList(map);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i <list.size() ; i++) {
            row=sheet.createRow(i+3);
            Employee employee=list.get(i);
            row.createCell(0).setCellValue(employee.getUuid());
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getPassword());
            row.createCell(3).setCellValue(employee.getName());
            row.createCell(4).setCellValue(employee.getPosition());
            Date entrytime= employee.getEntrytime();
            String date= "";
            if (!StringUtils.isEmpty(entrytime)){
                date=sdf.format(entrytime);
            }
            row.createCell(5).setCellValue(date);
            Date resignationtime= employee.getResignationtime();
            String date2="";
            if (!StringUtils.isEmpty(resignationtime)){
                date2=sdf.format(resignationtime);
            }
            row.createCell(6).setCellValue(date2);
            row.createCell(7).setCellValue(employee.getSalary());
            row.createCell(8).setCellValue(employee.getAvatar());
            row.createCell(9).setCellValue(employee.getSalt());
            row.createCell(10).setCellValue(employee.getPhone());
            row.createCell(11).setCellValue(employee.getSex());
            row.createCell(12).setCellValue(employee.getStatus());
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String fileName =df.format(new Date());// new Date()为获取当前系统时间
        OutputStream os= response.getOutputStream();
        String path="D:\\idea_workplace\\cosmetic\\excelPath\\"+fileName+".xls";
        OutputStream outputStream=new FileOutputStream(path);
        response.reset();
//        response.setHeader("Content-disposition", "attachment; filename="+new String(fileName.getBytes("gbk"), "iso8859-1")+".xls");
//        response.setContentType("application/vnd.ms-excel");
        wb.write(outputStream);
        ZipUtils.toZip(path, response.getOutputStream(), true);
        wb.close();
        os.close();
    }
}
