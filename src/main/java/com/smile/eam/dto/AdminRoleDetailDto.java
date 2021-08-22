package com.smile.eam.dto;

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

public class AdminRoleDetailDto {

    AdminRole adminRole;
    List<Integer> permissionList;

}
