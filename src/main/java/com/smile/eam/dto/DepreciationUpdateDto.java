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
public class DepreciationUpdateDto {

    @NotNull(message = "折旧项目ID不能为空")
    private Integer id;

    @NotNull(message = "折旧规则名不能为空")
    private String name;

    @NotNull(message = "描述不能为空")
    private String description;

    private List<DepreciationTemplateCreateDto> List;

}
