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
public class DeviceAttributeSoftWareDto {
    private int id;
    private String assetCategoryName;//分类名称
    private String name;
    private String version;
    private String issueName;
    private Date endAt;//结束使用时间
}
