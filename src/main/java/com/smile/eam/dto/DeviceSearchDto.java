package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceSearchDto {
    private int id;//资产编号
    private String name;//设备名
    private String vendorName;//厂商名称
    private String categoryName;//分类名称
    private String userName;//用户名
    private String description;//描述
}
