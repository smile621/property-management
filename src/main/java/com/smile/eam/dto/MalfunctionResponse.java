package com.smile.eam.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MalfunctionResponse {
    private int id;
    private int fixedAssetId;
    private String fixedAsset;
    private String solveId;
    private String malfunctionDescription;
    private String repairDescription;
    private Date createdAt;
    private Date updatedAt;
    private String status;
}
