package com.smile.eam.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AssetAffiliateResponse {
    private Integer  assetId;//软件资产id
    private String assetAffiliate;//归属设备id
    private int deviceId;//设备id
}
