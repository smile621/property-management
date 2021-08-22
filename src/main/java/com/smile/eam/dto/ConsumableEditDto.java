package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConsumableEditDto {

    private int id;                     //id
    private String description;         //耗材描述
    private String name;                //耗材名称

    private BigDecimal unitPrice;              //单价
    private int categoryId;             //分类id
    private String specification;       //规格
    private int vendorId;               //厂商id

}
