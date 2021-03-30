package com.wmy.cosmetic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private int rolid;
    private String rolna;
    private List<Long> permids;
    private String description;
    private Date createdt;
}
