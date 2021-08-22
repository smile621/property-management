package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumableInOutDto {
    private int id;
    private String inWareHouseUserName;//入库人
    private String recipientName;//领用人
    private String outWareHouseUserName;//出库人
    private String consumableName;//耗材名称
    private String intDescription;//入库描述
    private String outDescription;//出库描述
    private int outNumber;//出库数
    private int inNumber;//入库数
    private String buyAt;//购入时间
    private String outWareHouseAt;//出库时间
    private String  updatedAt;//更新时间
    private String createdAt;//创建时间
    private String inWareHouseAt;//入库时间
    private int status;
}
