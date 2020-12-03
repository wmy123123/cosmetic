package com.wmy.cosmetic.mapper;

import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.entity.Role;

import java.util.List;

public interface EmployeeMapper {
    Employee findByUsernameAndPassword(String username);
    List<Employee> findEmployeeList(Integer uuid,String name);
    int addEmployee(Employee employee);
    Employee findRolesByEmployeeName(String username);
    Employee findEmployeeById(Integer id);
    int updateEmploy(Employee employee);
    int deleteEmployee(Integer id);
    int updatePassword(Integer id,String password);
    //根据角色id查询权限集合
    List<Perm> findPermsByRoleId(int id);
}
