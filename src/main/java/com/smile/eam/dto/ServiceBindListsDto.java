package com.smile.eam.dto;

import com.smile.eam.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBindListsDto {

    List<ServiceBindListDto> serviceBindListDto;
    Pagination pagination;
}