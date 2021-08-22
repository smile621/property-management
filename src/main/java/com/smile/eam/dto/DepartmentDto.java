package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDto {

    private int id;

    @NotNull(message = "部门名不能为空")
    private String name;//部门名

    private List<Integer> idList;//子部门id列表
    private List<String> nameList;//子部门名列表
}
