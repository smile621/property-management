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
public class AdminUserDepartment {

    private int id;
    private int userId;         //用户ID
    private int departmentId;   //部门ID
    private Date createdAt;     //创建时间
    private Date updatedAt;     //更新时间
    private int status;         //状态
}
