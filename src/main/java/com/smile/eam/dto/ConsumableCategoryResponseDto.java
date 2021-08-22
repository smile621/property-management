package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Category;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableCategoryResponseDto {

    Pagination pagination;
    List<Category> categoryList;
}
