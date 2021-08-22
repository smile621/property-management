package com.smile.eam.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DeviceAffiliate {
    private int id;
    private int assetId;//软件/配件id
    private int assetAffiliate;//归属的设备id
    private String createdAt;
    private String updatedAt;
    private int status;//状态
}
