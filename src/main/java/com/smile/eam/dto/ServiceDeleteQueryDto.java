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
public class ServiceDeleteQueryDto {

    @NotNull(message = "删除ID不能为空")
    private List<Integer> ids;
}
