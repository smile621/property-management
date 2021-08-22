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
public class CheckTrackReturnDto {

    private int id;
    private int checkId;
    private String assetName;
    private String status;
    private String dutyId;
    private Date createdAt;
    private Date updatedAt;

}
