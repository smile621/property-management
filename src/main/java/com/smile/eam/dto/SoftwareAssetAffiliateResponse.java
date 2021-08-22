package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SoftwareAssetAffiliateResponse {
    private String assetId;//软件资产名称
    private String assetAffiliate;//绑定设备id
}
