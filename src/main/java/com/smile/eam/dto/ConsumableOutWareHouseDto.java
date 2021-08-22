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
public class ConsumableOutWareHouseDto {
    private int recipientId;//领用人id
    private int consumableId;//耗材id
    private int outWareHouseUserId;//出库人id
    private String outDescription;//出库描述
    private int outNumber;//出库数
    private String outWareHouseAt;//出库时间
}
