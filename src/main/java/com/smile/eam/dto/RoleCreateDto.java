package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDto {

    @NotNull(message = "角色名不能为空")
    private String roleName;                //角色名

    @NotNull(message = "权限列表不能为空")
    private List<Integer> permissionList;   //权限列表
}
