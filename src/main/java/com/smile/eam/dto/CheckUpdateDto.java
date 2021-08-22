package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckUpdateDto {

    private int assetId;
    private int checkId;
    private String description;
    private int status;//以盘点，丢失

}
