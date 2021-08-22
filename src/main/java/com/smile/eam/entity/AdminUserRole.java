package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserRole {

    private int id;
    private int roleId;         //角色ID
    private int userId;         //用户ID
    private Date createdAt;     //创建时间
    private Date updatedAt;     //更新时间
    private int Status;         //状态
}
