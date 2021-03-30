package com.wmy.cosmetic.service.ServiceImpl;


import com.alibaba.fastjson.JSONArray;
import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.entity.Perm2;
import com.wmy.cosmetic.entity.Result;
import com.wmy.cosmetic.entity.Role;
import com.wmy.cosmetic.mapper.PermissionMapper;
import com.wmy.cosmetic.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@Transactional
public class PermissionService {
    @Autowired(required = false)
    PermissionMapper permissionMapper;

    public Result<JSONArray> listAllPermission() {
        Result<JSONArray> result = new Result<>();
        List<Perm> perms = permissionMapper.listAllPermission();
        JSONArray jsonArray = new JSONArray();
        TreeUtils.setPermissionsTree(0, perms, jsonArray);
        result.setDatas(jsonArray);
        return result;
    }

    public Result<JSONArray> listAlPermission() {
        Result<JSONArray> result = new Result<>();
        List<Perm2> perms = permissionMapper.listAlPermission();
        JSONArray jsonArray = new JSONArray();
        TreeUtils.setPermissionsTree2(0, perms, jsonArray);
        result.setDatas(jsonArray);
        return result;
    }

    public List<Perm> listAllPermissionByRoleId(Integer roleId) {
        return permissionMapper.listAllPermissionByRoleId(roleId);
    }

    public Result<String> addRole(Role role) {
        Result<String> result = new Result<>();
        result.setCode(0);//默认交易成功
        Role rol = permissionMapper.findRoleByRolna(role.getRolna());
        result.setMsg("添加成功");
        if (rol != null) {
            result.setCode(1);
            result.setMsg("角色已存在");
            return result;
        }
        Date date = new Date();
        role.setCreatedt(date);
        permissionMapper.addRole(role);
        List<Long> perms = role.getPermids();
        if (!CollectionUtils.isEmpty(perms)) {
            perms.remove(0);
        }
        if (!ObjectUtils.isEmpty(perms)) {
            Map<String, Object> param = new HashMap<>();
            param.put("rolid", role.getRolid());
            param.put("permid", perms);
            permissionMapper.save(param);
        }
        return result;
    }

    @Transactional
    public void updateRole(Role role) {
        deleteRole(role.getRolid());
        addRole(role);
    }

    @Transactional
    public void deleteRole(Integer rolid) {
        permissionMapper.deleteRolePerms(rolid);
        permissionMapper.deleteRole(rolid);
    }

    public List<Perm> permsList() {
        return permissionMapper.listAlpermission();
    }

    public Perm permissionItem(Integer id) {
        return permissionMapper.permsItem(id);
    }

    public void addPerm(Perm perm) {
        permissionMapper.addPerm(perm);
    }

    public void updatePerm(Perm perm) {
        permissionMapper.updatePrerm(perm);
    }

    public void deletePerm(Integer id) {
        permissionMapper.deletePerm(id);
    }
}
