package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.common.Pagination;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableResponseDto {

    Pagination pagination;                      //页码列表
    List<ConsumableDto> consumableDtoList;      //耗材列表

}
