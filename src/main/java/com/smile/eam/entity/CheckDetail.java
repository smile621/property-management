package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckDetail {

    private int id;
    private int checkId;
    private int assetId;
    private int assetCategoryId;
    private Date createdAt;
    private Date updatedAt;
    private int status;
    private String assetName;
    private String description;
    private int checkUserId;
    private BigDecimal price;

}
