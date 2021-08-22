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
public class DepartmentCreateDto {

    @NotNull(message = "部门名不能为空")
    private String name;

    @NotNull(message = "父级部门ID不能为空")
    private Integer parentId;           //父级部门ID

    @NotNull(message = "负责人ID不能为空")
    private Integer principalPersonId;  //负责人ID（没值则传0）
}
