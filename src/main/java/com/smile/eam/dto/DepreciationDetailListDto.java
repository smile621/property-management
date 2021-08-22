package com.smile.eam.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationDetailListDto {
    private int id;
    private String name;
    private int period;
    private String measure;
    private String rate;
}
