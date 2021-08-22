package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetService {

    private Integer id;
    private String expiredAt;
    private String name;
    private String description;
    private String price;
    private String buyAt;
    private Integer vendorId;
    private Integer buyRouterId;
    private Integer status;
}
