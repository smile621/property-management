package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateToDoRequest {

    @NotNull(message = "请传入处理人id[solveId]")
    @ApiModelProperty("处理人")
    private Integer solveId;//解决人

    @ApiModelProperty("标题")
    @NotNull(message = "标题不为空[title]")
    private String title;//标题

    @ApiModelProperty("内容描述")
    @NotNull(message = "描述不为空[description]")
    private String description;//描述

    @ApiModelProperty("优先级，0：无 1：低 2：中 3：高")
    @NotNull(message = "请选择优先级[priority]")
    private Integer priority;//优先级
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAt;//开始时间
    private String tag;//标签
    private static Integer status=5;//默认状态待处理


}
