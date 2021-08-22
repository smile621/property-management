package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBindListDto {

    private Integer id;
    private String name;
    private List<String> affiliates;
    private Date createdAt;
    private Date updatedAt;
}
