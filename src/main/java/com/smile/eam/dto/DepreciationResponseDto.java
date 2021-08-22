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
public class DepreciationResponseDto {

    private Integer id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;

}
