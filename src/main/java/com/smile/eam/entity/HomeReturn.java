package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeReturn {

    private BigDecimal myAsset;
    private int myTodo;
    private int AssetMalfunction;
    private int serviceError;
    private BigDecimal totalValue;
    private BigDecimal deviceValue;
    private BigDecimal partValue;
    private BigDecimal softwareValue;
    private BigDecimal serviceValue;
    private List<PriceDate> totalValueList;
    private List<PriceDate> partValueList;
    private List<PriceDate> softwareValueList;
    private List<PriceDate> deviceValueList;
    private List<PriceDate> serviceValueList;
    private List<MalfunctionStatistics> malfunctionStatisticsList;
    private List<ServiceHomeReturn> serviceList;

}
