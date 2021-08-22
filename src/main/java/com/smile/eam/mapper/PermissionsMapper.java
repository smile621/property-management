package com.smile.eam.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import static com.smile.eam.dto.RolePermissionsDto.STATUS_DELETE;

public interface PermissionsMapper {

    /**
     * 通过用户ID查找所归属的角色
     */
    @Select("SELECT role_id FROM admin_user_role WHERE user_id = #{userId} AND status != "+STATUS_DELETE+"")
    List<Integer> findRoleByPermissionsId(@Param("userId")int userId);

    /**
     * 通过角色id列表查找权限ID
     */
    @Select({"<script>",
            "SELECT permission_id FROM admin_role_permission WHERE status != "+STATUS_DELETE+" AND role_id ",
            "  IN <foreach item='item' collection='list' separator=',' open='(' close=')' > #{item} </foreach>",
            "ORDER BY id ASC ",
            "</script>"})
    List<Integer> findPermissionsIdByRoleIds(@Param("list")List list);

    /**
     * 验证有无权限
     */
    @Select({"<script>",
            "SELECT COUNT(*) FROM admin_permissions WHERE status != "+STATUS_DELETE+" AND tag = #{tag} AND code = #{code} AND id ",
            "  IN <foreach item='item' collection='list' separator=',' open='(' close=')' > #{item} </foreach>",
            "</script>"})
    int findPermissions(@Param("list")List list,@Param("tag")String tag,@Param("code")String code);

}
