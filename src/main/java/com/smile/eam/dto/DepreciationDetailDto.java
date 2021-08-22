package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationDetailDto {

    private int id;
    private String name;
    private String description;
    private String category;
    private String CreatedAt;
    private String UpdatedAt;
    private List<DepreciationDetailListDto> depreciationDetailLists;
}
