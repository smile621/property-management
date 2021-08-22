package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.AssetCategory;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareCategoryDto {
    private Pagination pagination;
    private List<AssetCategory> assetCategoryList;
}
