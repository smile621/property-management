package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConsumableDto {
    private int id;                 //id
    private String description;     //耗材描述
    private String name;            //耗材名称
    private String unitPrice;       //单价
    private int categoryId;         //分类Id
    private String categoryName;    //分类名称
    private String specification;   //规格
    private int total;              //总数
    private int vendorId;           //厂商Id
    private String vendorName;      //厂商名称
    private String createdAt;         //创建时间
    private String updatedAt;         //更新时间
    private int status;             //状态
}
