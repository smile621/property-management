package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountDto {
    private int totalSoftware;
    private int willPastCount;
    private int past;
    private Map<String, BigDecimal> decimalMap;
}
