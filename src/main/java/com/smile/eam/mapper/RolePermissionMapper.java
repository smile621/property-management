package com.smile.eam.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RolePermissionMapper {

    @Select(" SELECT permission_id FROM `admin_role_permission` WHERE role_id = #{id}")
    int findPermissionIdByRoleId(@Param("id") int roleId);
}
