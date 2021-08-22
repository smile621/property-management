package com.smile.eam.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class whoamiReturnDto {
    private int id;                 //用户ID
    private List<RoleIdNameDto> roleIdNameDtoList;    //角色信息
    private Integer departmentId;     //部门ID
    private String department;      //部门
    private List<Integer> permission;   //权限ID
    private String username;        //用户名
    private String nickname;        //昵称
    private String avatar;          //头像
    private String phone;           //手机
    private String email;           //邮箱
    private String job;             //职位
    private String sex;             //性别
    private int status;             //状态
    private Date createdAt;         //创建时间
    private Date updatedAt;         //更新时间
}
