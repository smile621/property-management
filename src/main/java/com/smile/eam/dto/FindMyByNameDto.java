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
public class FindMyByNameDto {
    private List<ToDoResponse> countByName;
    private Pagination pagination;
}
