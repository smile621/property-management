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
public class DeviceSearch {
    private int id;//资产编号
    private List<Integer> deviceIds;//设备id
    private List<Integer> vendorIds;//厂商id
    private String categoryName;//分类名称
    private String userName;//用户名
    private String description;//描述
    private BigDecimal price;//价格
    private String deviceListName;//设备名称
}
