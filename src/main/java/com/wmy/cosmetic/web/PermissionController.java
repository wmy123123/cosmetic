package com.wmy.cosmetic.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wmy.cosmetic.entity.DtreeResult;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.entity.Result;
import com.wmy.cosmetic.entity.Role;
import com.wmy.cosmetic.service.EmployeeService;
import com.wmy.cosmetic.service.ServiceImpl.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@RequestMapping("permission")
public class PermissionController {
    private final Logger logger= LoggerFactory.getLogger(PermissionController.class);
    @Autowired
    PermissionService permissionService;
    @Autowired
    EmployeeService employeeService;
    @RequestMapping("listAllPermission")
    @ResponseBody
    public Result<JSONArray> listAllPermission(){
        Result<JSONArray> result=null;
        try{
            result = permissionService.listAllPermission();
            result.setCode(0);
            result.setMsg("查询成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    @RequestMapping("addRole")
    @ResponseBody
    public Result<String> addRole(@RequestBody Role role){
        Result<String> result=null;
        try{
            result = permissionService.addRole(role);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    @RequestMapping("updateRole")
    @ResponseBody
    public Result<String> updateRole(@RequestBody Role role){
        Result<String> result=new Result<>();
        result.setCode(0);//默认交易成功
        try{
            permissionService.updateRole(role);
            result.setMsg("修改成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    @RequestMapping("deleteRole")
    @ResponseBody
    public Result<String> deleteRole(Integer rolid){
        Result<String> result=new Result<>();
        result.setCode(0);//默认交易成功
        try{
            permissionService.deleteRole(rolid);
            result.setMsg("删除成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    @RequestMapping("listAllPermissionByRoleId")
    @ResponseBody
    public Result<Perm> listAllPermissionByRoleId(Integer roleId){
        Result<Perm> result=new Result<>();
        result.setCode(0);//默认交易成功
        try{
            List<Perm> roleList = permissionService.listAllPermissionByRoleId(roleId);
            result.setData(roleList);
            result.setMsg("添加成功");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    @RequestMapping("listAlPermission")
    @ResponseBody
    public DtreeResult<JSONArray> listAlPermission(){
        DtreeResult<JSONArray> result=new DtreeResult<>();
        try{
            Result<JSONArray> result1 = permissionService.listAlPermission();
            DtreeResult.Status status=new DtreeResult.Status();
            status.setCode(200);
            status.setMessage("数据请求成功");
            result.setStatus(status);
            System.out.println(result1.getDatas());
            result.setData(result1.getDatas());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return result;
    }
    @RequestMapping("permissionItem")
    @ResponseBody
    public Result<JSONObject> permissionItem(Integer id){
        Result<JSONObject>result=new Result<>();
        result.setCode(0);
        try {
           String st= JSONObject.toJSONString(permissionService.permissionItem(id));
            JSONObject parent = (JSONObject) JSONObject.parse(st);
           result.setDatas(parent);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("查询失败");
        }
        return result;
    }
    @RequestMapping("addperm")
    @ResponseBody
    public Result<String> addperm(Perm perm){
        Result<String>result=new Result<>();
        result.setCode(0);
        try {
            permissionService.addPerm(perm);
            result.setMsg("添加成功");
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("添加失败");
        }
        return result;
    }
    @RequestMapping("updatePerm")
    @ResponseBody
    public Result<String> updatePerm(Perm p){
        Result<String>result=new Result<>();
        result.setCode(0);
        try {
            permissionService.updatePerm(p);
            result.setMsg("修改成功");
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("修改失败");
        }
        return result;
    }
    @RequestMapping("deletePerm")
    @ResponseBody
    public Result<String> deletePerm(Integer id){
        Result<String>result=new Result<>();
        result.setCode(0);
        try {
          permissionService.deletePerm(id);
          result.setMsg("删除成功");
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            result.setCode(1);
            result.setMsg("删除失败");
        }
        return result;
    }

}
