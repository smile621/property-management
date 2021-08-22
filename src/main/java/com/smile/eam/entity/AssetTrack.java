package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetTrack {
    private int id;//ID
    private int fixedAssetId;//资产编号
    private int userId;//归属人
    private int lendUserId;//借用人
    private String lendDescription;//借用描述
    private String lendEndDescription;//归还描述
    private Date createdAt;//创建时间
    private Date updatedAt;//更新时间
    private Date lendPlanReturnAt;//计划归还时间
    private Date lendReturnAt;//实际归还时间
    private Date lendStartAt;//借用开始时间
    private Date lendEndAt;//借用结束时间
    private Date bindingStartAt;//归属开始时间
    private Date bindingEndAt;//归属结束时间
    private int status;//状态
}
