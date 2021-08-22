package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDetailDto {

    private Integer id;
    private String name;
    private Integer parentId;
    private String parentName;
    private Integer principalPersonId;
    private String principalPerson;

}
