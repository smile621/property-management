package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.common.Pagination;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableTrackResponseDto {

    Pagination pagination;
    List<ConsumableInOutDto> consumableInOutDtoList;
}
