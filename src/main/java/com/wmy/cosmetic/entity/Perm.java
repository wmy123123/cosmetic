package com.wmy.cosmetic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Perm {
    private Integer id;
    private Integer parentid;
    private String name;
    private String url;
    private Integer type;
    private String permission;
    private Integer sort;
}
