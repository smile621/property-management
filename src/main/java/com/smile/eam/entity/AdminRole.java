package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRole {

    private int id;             //角色ID
    private String name;        //角色名
    private Date createdAt;     //创建时间
    private Date updatedAt;     //更新时间
    private int status;         //状态
}
