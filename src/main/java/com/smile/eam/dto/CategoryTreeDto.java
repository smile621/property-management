package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTreeDto {
    private int id;
    private String name;
    private int parentId;
    private List<CategoryTreeDto> children=new ArrayList<>();
    private int status;
    private int value;
    private String title;
}

