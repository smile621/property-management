package com.smile.eam.dto;

import com.smile.eam.common.Pagination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.entity.AdminRole;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoleDto {

    List<AdminRole> adminRoles;
    Pagination pagination;
}
