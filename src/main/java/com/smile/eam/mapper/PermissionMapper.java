package com.smile.eam.mapper;

import com.smile.eam.entity.AdminPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.smile.eam.entity.AdminPermission.STATUS_DELETE;


@Repository
public interface PermissionMapper {

    /**
     * 通过roleId和permissionId查找是否有相应权限
     */
    @Select("SELECT permission_id FROM admin_role_permission WHERE role_id = #{roleId} and permission_id = #{permissionId} LIMIT 1")
    String  findPermissionIdByRoleId(@Param("roleId")int roleId,@Param("permissionId")int permissionId);

    /**
     * 查找全部的权限列表
     */
    @Select("SELECT * FROM admin_permissions WHERE status != "+STATUS_DELETE+"")
    List<AdminPermission> findPermissionAll();

}
