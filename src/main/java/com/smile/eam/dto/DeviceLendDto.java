package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceLendDto {
    private int id;//固定资产id
    private int lendUserId;//借用人id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String lendPlanReturnAt;//计划归还时间
    private String lendDescription;//借用描述

}
