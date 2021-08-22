package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMalfunctionRequest {

    private int fixedAssetId;               //设备资产ID
    private String malfunctionDescription;  //故障说明

}
