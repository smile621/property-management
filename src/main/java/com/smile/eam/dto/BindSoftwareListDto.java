package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.common.Pagination;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BindSoftwareListDto {
    private List<AssetAffiliateDto> SoftwareAssetAffiliateResponseList;
    private Pagination pagination;
}
