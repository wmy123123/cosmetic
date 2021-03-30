package com.wmy.cosmetic.mapper;

import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmployeeMapper {
    Employee findByUsernameAndPassword(String username);
    List<Employee> findEmployeeList(Map<String, Object> param);
    void addEmployee(Employee employee);
    List<Perm> findPermsByEmployeeName(String username);
    Employee findEmployeeById(Integer id);
    Employee findEmployeeByUuid(String uuid);
    void updateAvatarImgPath(String uuid,String imgUrl);
    int updateEmploy(Employee employee);
    void updateEmploy1(Employee employee);
    int deleteEmployee(Integer id);
    int updatePassword(String uuid,String password);
    //从职位表中查出所有职位信息
    List<Role> findRole();
    //根据角色id查询权限集合
    List<Perm> findPermsByRoleId(int id);
    //测试
    void insertProductType(int id,String name);
    void deleteEmpById(List<String> uuids);
    List<Role> findAllRole();
    //修改员工离职状态
    void updateStatus(String uuid, Date date);
    void updateStatus1(String uuid, Date date);
    List<Role> roleList(Integer id,Integer permid);
}
