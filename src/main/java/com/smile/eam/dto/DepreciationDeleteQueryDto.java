package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationDeleteQueryDto {

    private List<Integer> ids;//要删除的id
}
