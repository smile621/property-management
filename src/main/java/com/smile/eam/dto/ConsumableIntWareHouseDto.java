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
public class ConsumableIntWareHouseDto {
    private int consumableId;//耗材id
    private int inWareHouseUserId;//入库人id
    private String intDescription;//入库描述
    private int inNumber;//入库数
    private String buyAt;//购入时间
    private String inWareHouseAt;//入库时间
}
