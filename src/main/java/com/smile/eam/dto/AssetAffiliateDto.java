package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetAffiliateDto {
    private Integer id;//资产id
    private Integer keyId;
    private String assetId;//软件资产id+服务资产id
    private String assetAffiliate;//归属设备id
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    private int status;//状态
}
