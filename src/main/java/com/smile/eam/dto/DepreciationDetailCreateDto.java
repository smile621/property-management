package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationDetailCreateDto {

    private int depreciationId;
    private String name;
    private int period;
    private int measure;
    private String rate;

}
