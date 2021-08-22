package com.smile.eam.mapper;

import com.smile.eam.entity.AdminRolePermission;
import com.smile.eam.entity.AdminUserDepartment;
import com.smile.eam.entity.AdminRole;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

public interface OrganizationMapper {

    /**
     * 插入 角色权限 表
     */
    @Insert(
            "<script>" +
                    "insert into admin_role_permission (role_id,permission_id)" +
                    "values" +
                    "<foreach collection = 'permissionList' item = 'p' index = 'index' separator=','>" +
                    "(#{roleId},#{p})" +
                    "</foreach>" +
                    "</script>"
    )
    void createRolePermission(@Param("roleId") int roleId, @Param("permissionList") List<Integer> permissionList);


    /**
     * 插入 用户——角色 表
     */

    @Insert("<script>" +
            " insert into admin_user_role (user_id,role_id) " +
            " values" +
            " <foreach collection = 'roleList' item = 'r' index = 'index' separator=','>" +
            " (#{userId},#{r})" +
            " </foreach>" +
            "</script>")
    void createUserRoles(int userId, List<Integer> roleList);


    /**
     * 插入 用户——部门 表
     */

    @Insert("insert into admin_user_department (user_id,department_id) values (#{userId},#{departmentId})")
    void createUserDepartment(@Param("userId") int userId, @Param("departmentId") int departmentId);


    /**
     * admin_user_role 删除
     */

    @Update("update admin_user_role set status=100 where user_id=#{userId} and status!= 100 ")
    int deleteUserRole(int userId);


    /**
     * 根据角色ID查找所有权限
     */
    @Select("select permission_id from admin_role_permission where role_id=#{id} and status!=100")
    List<Integer> searchPermission(int id);

    /**
     * 根据多个角色ID查找所有权限
     */
    @Select("<script>" +
            " select * from admin_role_permission " +
            " where role_id in " +
            " <foreach collection='roleIds' item='id' open='( '  separator=','   close=')'  >" +
            " #{id} " +
            " </foreach>" +
            " and status!=100 " +
            "</script>")
    List<AdminRolePermission> searchPermissions(@Param("roleIds") Set<Integer> roleIds);

    /**
     * 删除角色权限
     */
    @Update("update admin_role_permission set status=100 where role_id=#{id}")
    int deleteRolePermission(int id);


    /**
     * 通过角色ID查询角色详情  admin_role
     */
    @Select("select * from admin_role where id=#{roleId}")
    AdminRole findRoleDetailsById(int roleId);


    /**
     * 根据多个用户id获取用户——部门信息  admin_user_department
     */
    @Select(" <script>" +
            " select * from admin_user_department " +
            " where user_id IN " +
            " <foreach collection='userIds' item='id' open='('   separator=',' close=')'  >" +
            "  #{id} " +
            " </foreach>" +
            " and status!=100" +
            " </script>")
    List<AdminUserDepartment> findUserDepartmentByUserIds(@Param("userIds") List<Integer> userIds);


    /**
     * 根据用户ID查找部门ID
     */
    @Select("select department_id from admin_user_department where user_id=#{userId} and status!=100 limit 1")
    String findDepartmentIdByUserId(int userId);

    /**
     * 根据用户Id查找角色IDS
     */
    @Select("select role_id from admin_user_role where user_id=#{userId} and status!=100")
    List<Integer> findRoleIdsByUserId(int userId);
}
