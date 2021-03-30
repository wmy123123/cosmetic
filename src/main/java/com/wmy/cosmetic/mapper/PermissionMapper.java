package com.wmy.cosmetic.mapper;

import com.wmy.cosmetic.entity.Perm;
import com.wmy.cosmetic.entity.Perm2;
import com.wmy.cosmetic.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface PermissionMapper {

    @Select("select * from t_perm")
    List<Perm> listAllPermission();

    List<Perm2> listAlPermission();

    List<Perm> listAlpermission();

    Perm permsItem(Integer id);

    List<Perm> listAllPermissionByRoleId(Integer roleId);

    int addRole(Role role);

    Role findRoleByRolna(String rolna);

    void save(Map<String,Object> map);

    void deleteRole(Integer rolid);
    void deleteRolePerms(Integer rolid);

    void addPerm(Perm perm);
    void updatePrerm(Perm perm);
    void deletePerm(Integer id);
}
