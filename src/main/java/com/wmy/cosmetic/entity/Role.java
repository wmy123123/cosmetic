package com.wmy.cosmetic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private int id;
    private String name;
    private List<Perm> perms;

    //定义权限集合
    public List<Perm> getPerms() {
        return perms;
    }
}
