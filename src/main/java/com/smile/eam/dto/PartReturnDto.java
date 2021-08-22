package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartReturnDto {

    private int assetId;
    private int assetCategoryId;
    private String assetCategory;
    private int buyRouterId;
    private String buyRouter;
    private int vendorId;
    private String vendor;
    private String name;
    private String description;
    private String avatar;
    private String price;
    private String outPrice;
    private String status;
    private String malfunctionStatus;
    private String affiliationDevice;
    private String depreciation;
    private String buyAt;
    private String expiredAt;
    private String startAt;
    private String endAt;
    private String createAt;
    private String updateAt;
    private List cateList;

}
