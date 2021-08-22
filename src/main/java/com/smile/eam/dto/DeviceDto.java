package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {
    private int id;
    private String name;
    private String avatar;
    private int assetCategoryId;    //资产分类id
    private int userId;
    private int deviceId;
    private int vendorId;
    private BigDecimal price;
    private int buyRouterId;
    private String description;
    private String ip;
    private String mac;
    private String specification;
    private String version;
    private int issueId;
    private int warrantyNumber;
    private String startAt;
    private String endAt;
    private String buyAt;
    private String expiredAt;
    private String createdAt;
    private String updatedAt;
    private int status;
}
