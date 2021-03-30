package com.wmy.cosmetic.service;

import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.entity.Role;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface EmployeeService {
     Employee login(String username);
     PageInfo<Employee> findEmployeeList(Integer id, String name,String position, int pageNum, int pageSize);
     void addEmployee(Employee employee);
     List<Perm> findPermsByEmployeeName(String username);
     Employee findEmployeeById(Integer id);
     int updateEmploy(Employee employee);
     void updateEmploy1(Employee employee);
     int deleteEmployee(Integer id);
     int updatePassword(String uuid,String password);
     void deleteEmpById(List<String> uuids);
     List<Role> findAllRole();
     void updateAvatarImgPath(String uuid,String imgUrl);
     PageInfo<Role> roleList(Integer id,Integer permid,Integer pageNumber,Integer limit);
     String importExcel(MultipartFile file) throws IOException, ParseException;
     void exportExcel(HttpServletResponse response) throws IOException;
}
