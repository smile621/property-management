package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetTrackDto {
    private int id;//id
    private int fixedAssetId;//资产编号
    private String name;//设备名称
    private String userName;//归属人/借用人  放一起
    private String lendDescription;//借用描述
    private String lendEndDescription;//归还描述
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间
    private Date lendStartAt;//借用开始时间
    private Date bindingStartAt;//归属开始时间
    private Date lendPlanReturnAt;//计划归还时间
    private Date lendReturnAt;//归还时间
    private int status;//状态
}
