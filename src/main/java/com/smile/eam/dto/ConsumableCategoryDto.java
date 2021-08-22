package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableCategoryDto {
    private int id;
    private String name;
    private String description;
    private String categoryName;//父级分类名称
    private Date createdAt;
    private Date updatedAt;
}
