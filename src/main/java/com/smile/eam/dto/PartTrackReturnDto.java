package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartTrackReturnDto {
    private int assetId;
    private String name;
    private int trackDeviceId;
    private String trackDeviceName;
    private Date bindingStartAt;
    private Date bindingEndAt;
    private String lendDescription;
    private Date lendStartAt;
    private Date lendPlanReturnAt;
    private Date lendReturnAt;
    private Date lendEndDescription;
    private Date createAt;
    private Date updateAt;
    private String status;
}
