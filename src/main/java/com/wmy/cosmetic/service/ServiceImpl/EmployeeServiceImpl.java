package com.wmy.cosmetic.service.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wmy.cosmetic.entity.Employee;
import com.wmy.cosmetic.entity.Role;
import com.wmy.cosmetic.mapper.EmployeeMapper;
import com.wmy.cosmetic.service.EmployeeService;
import com.wmy.cosmetic.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
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
    public PageInfo<Employee> findEmployeeList(Integer uuid, String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Employee> employees= employeeMapper.findEmployeeList(uuid, name);
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
        Md5Hash md5Hash =new Md5Hash(employee.getPassword(),salt,1024);
        employee.setPassword(md5Hash.toHex());
        return employeeMapper.addEmployee(employee);
    }

    @Override
    public Employee findRolesByEmployeeName(String username) {
        if (ObjectUtils.isEmpty(username)){
            return null;
        }
        return employeeMapper.findRolesByEmployeeName(username);
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
            String salt = SaltUtils.getSalt(8);
            Md5Hash md5Hash = new Md5Hash(employee.getPassword(),salt,1024);
            employee.setSalt(salt);
            employee.setPassword(md5Hash.toHex());
            return employeeMapper.updateEmploy(employee);
        }
    }

    @Override
    public int deleteEmployee(Integer id) {
        if (ObjectUtils.isEmpty(id)){
            return 0;
        }
        return employeeMapper.deleteEmployee(id);
    }

    @Override
    public int updatePassword(Integer id, String password) {
        if (id!=null&&password!=null){
           return  employeeMapper.updatePassword(id, password);
        }
        return 0;
    }

}
