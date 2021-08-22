package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.RoleCreateDto;
import com.smile.eam.dto.RoleSearchDto;
import com.smile.eam.dto.RoleUpdateDto;
import com.smile.eam.entity.AdminUserToken;
import com.smile.eam.entity.AdminRole;
import com.smile.eam.entity.AdminUserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleMapper {

    /**
     * 角色模糊搜索列表
     */
    @Select("select * from admin_role where concat (id,name) like '%' #{role.name} '%' " +
            " and status!=" + AdminUserToken.STATUS_DELETE + " order by id desc limit #{pagination.offset}, #{pagination.limit}")
    List<AdminRole> searchRoles(@Param("role") RoleSearchDto role, @Param("pagination") Pagination pagination);

    /**
     * 角色模糊搜索数
     */
    @Select("select count(*) from admin_role where concat (id,name) like '%' #{role.name} '%' " +
            " and status!= " + AdminUserToken.STATUS_DELETE)
    long roleSearchCount(@Param("role") RoleSearchDto role);

    /**
     * 新建角色
     */
    @Insert("insert into admin_role (name) values (#{roleName})")
    int createRole(RoleCreateDto roleCreateDto);

    /**
     * 编辑角色名
     */
    @Update("update admin_role set name=#{roleName} where id=#{id}")
    void updateRoleName(RoleUpdateDto roleUpdateDto);

    /**
     * 角色删除
     */
    @Update("update admin_role set status=" + AdminUserToken.STATUS_DELETE + " where id=#{roleId}")
    void deleteRole(int roleId);

    /**
     * 删除角色用户绑定
     */
    @Update("update admin_user_role set status=" + AdminUserToken.STATUS_DELETE + " where role_id=#{roleId}")
    void deleteUserRole(int roleId);

    /**
     * 通过角色名找id(验证角色名是否已存在)
     */
    @Select("select id from admin_role where name=#{name} and status!=" + AdminUserToken.STATUS_DELETE + " limit 1")
    String findOneRoleIdByRoleName(String roleName);

    /**
     * 通过userId获取roleIds(多个)
     */
    @Select("<script>" +
            " select * from admin_user_role " +
            " where user_id IN" +
            " <foreach collection='userIds' item='id' open='('  separator=','  close=')'  >" +
            " #{id} " +
            " </foreach>" +
            " and status != " + AdminUserToken.STATUS_DELETE +
            " </script> ")
    List<AdminUserRole> findUserRolesByUserIds(@Param("userIds") List<Integer> userIds);

    /**
     * 根据角色id获取角色信息(多个)
     */
    @Select("<script>" +
            " select * from admin_role " +
            " where id in" +
            " <foreach  collection='roleIdsSet' item='id' open='(' separator=',' close=')' > " +
            " #{id} " +
            " </foreach>" +
            " and status!=" + AdminUserToken.STATUS_DELETE +
            " </script>")
    List<AdminRole> findRoleByRoleIds(@Param("roleIdsSet") Set<Integer> roleIdsSet);

    /**
     * 批量删除
     */
    @Update("<script>" +
            " update admin_role set status=100 " +
            " where id in " +
            " <foreach collection='ids' item='id' open='(' separator=',' close=')' > " +
            " #{id} " +
            " </foreach>" +
            " </script>")
    int deleteQuery(@Param("ids") List<Integer> ids);
}
