package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ToDoResponse {
    private Integer id;//自增id
    private String userId;//待办创建人
    private String solveId;//解决人
    private String title;//标题
    private String description;//描述
    private String priority;//优先级
    private String handleDescription;//结束描述
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startAt;//开始时间
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间
    private String tag;//标签
    private String status;//状态
}
