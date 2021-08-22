package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private int id;                         //用户ID

    private String username;                //用户名
    private String nickname;                //昵称
    private String sex;                     //性别
    private String job;                     //职位
    private String phone;                   //手机
    private String email;                   //邮箱

    private Set<Integer> roleList;          //角色Id列表
    private List<String> roleNameList;      //角色名字列表
    private int departmentId;               //部门ID
    private String department;              //部门名字
    private Set<Integer> permissionList;    //权限ID列表
}
