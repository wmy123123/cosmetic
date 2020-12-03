package com.wmy.cosmetic.service;

import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.entity.Employee;
import java.util.List;

public interface EmployeeService {
     Employee login(String username);
     PageInfo<Employee> findEmployeeList(Integer id, String name, int pageNum, int pageSize);
     int addEmployee(Employee employee);
     Employee findRolesByEmployeeName(String username);
     Employee findEmployeeById(Integer id);
     int updateEmploy(Employee employee);
     int deleteEmployee(Integer id);
     int updatePassword(Integer id,String password);
}
