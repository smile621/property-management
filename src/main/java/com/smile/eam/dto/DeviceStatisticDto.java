package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatisticDto {
    private BigDecimal totalPrice;//总价
    private String month;//当前月份
    private int depreciateId;//折旧ID
}
