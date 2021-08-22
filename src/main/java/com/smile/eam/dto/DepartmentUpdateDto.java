package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentUpdateDto {

    @NotNull(message = "部门ID不能为空")
    private Integer departmentId;           //部门ID

    @NotNull(message = "部门名不能为空")
    private String departmentName;      //部门名字

    @NotNull(message = "父级部门ID不能为空")
    private int parentId;               //父级部门ID

    @NotNull(message = "负责人ID不能为空")
    private int principalPersonId;      //负责人ID
}
