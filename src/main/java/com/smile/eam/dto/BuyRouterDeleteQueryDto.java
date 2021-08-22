package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyRouterDeleteQueryDto {

    @NotNull(message = "要删除id列表不能为空")
    private List<Integer> ids;
}
