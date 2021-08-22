package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationItem {

    private int id;
    private String description;
    private String name;
    private int assetCategoryId;
    private String createdAt;
    private String updatedAt;
    private int status;

}
