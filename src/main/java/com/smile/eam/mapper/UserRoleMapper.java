package com.smile.eam.mapper;

import com.smile.eam.entity.AdminUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserRoleMapper {

    //通过用户id查询角色
    @Select(" SELECT * FROM `admin_user_role` WHERE user_id = #{userId}")
    AdminUserRole findRoleByUserId(@Param("userId") int userId);
}
