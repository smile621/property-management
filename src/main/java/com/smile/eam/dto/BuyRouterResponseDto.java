package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.BuyRouter;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyRouterResponseDto {

    Pagination pagination;

    List<BuyRouter> buyRouterResponseDtoList;

}
