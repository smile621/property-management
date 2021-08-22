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
public class ServiceErrorListDto {

    private Integer id;
    private Integer serviceId;
    private String name;
    private String description;
    private String startAt;
    private String endAt;
    private Integer errorStatus;
}
