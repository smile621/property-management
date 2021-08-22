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
public class SoftwareListDeleteRequest {
    @NotNull(message = "请传入批量删除列表[list]")
    private List<Integer> list;//批量删除列表
}
