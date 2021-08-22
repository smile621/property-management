package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.*;
import com.smile.eam.dto.UserCreateDto;
import com.smile.eam.dto.UserIdResponse;
import com.smile.eam.dto.UserSearchDto;
import com.smile.eam.entity.AdminUser;
import com.smile.eam.dto.UserUpdateDto;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


import static com.smile.eam.entity.AdminUser.STATUS_DELETE;

@Repository
public interface UserMapper {

    /**
     * 编辑当前用户
     */
    @Update("update admin_user set password_hash=#{newPassword}," +
            "nickname=#{nickname}" +
            "where username=#{username}")
    void updateNowUser(NowUserUpdateDto nowUserUpdateDto);

    /**
     * 通过userID查找对应的角色id
     */
    @Select("<script>" +
            "SELECT permission_id FROM admin_role_permission WHERE status != #{status} AND role_id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Integer> findPermissionIdByRoleId(@Param("list") List list, @Param("status") int status);

    /**
     * 通过userID查找对应的角色id
     */
    @Select("SELECT role_id FROM admin_user_role WHERE user_id = #{id} AND status != #{status}")
    List<Integer> findRoleListByUserId(@Param("id") int id, @Param("status") int status);

    /**
     * 通过user
     */
    @Select("<script>" +
            "SELECT * FROM admin_role WHERE status != " + STATUS_DELETE + " AND id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<RoleIdNameDto> findRoleNameById(@Param("list") List list);

    /**
     * 通过userid查找部门id
     */
    @Select("SELECT department_id FROM admin_user_department WHERE user_id =#{id} AND status != #{status}")
    Integer findDepartmentIdByUserId(@Param("id") int id, @Param("status") int status);

    /**
     * 通过id查找用户信息
     */
    @Select(" SELECT * FROM `admin_user` WHERE id = #{id} AND status != "+ AdminUser.STATUS_DELETE +"")
    AdminUser findUserById(@Param("id") int id);

    /**
     * 输入用户名、密码，验证sql是否有对应的数据
     */
    @Select("select * from `admin_user` where `username`=#{username} and `password_hash`=#{password_hash}")
    UserIdResponse findUserByUsernameAndPassword(@Param("username") String username, @Param("password_hash") String passHash);

    /**
     * 获取所有用户列表
     */
    @Select("select * from admin_user where status!=" + STATUS_DELETE + "  limit #{pagination.offset}, #{pagination.limit} ")
    List<AdminUser> findAllUsers(@Param("pagination") Pagination pagination);

    /**
     * 获取所有用户列表,不传参
     */
    @Select("select * from admin_user where status!=" + STATUS_DELETE)
    List<AdminUser> findAllUsersList();

    /**
     * 获取用户列表总条数
     */
    @Select("select count(*) from admin_user  where status!=" + STATUS_DELETE)
    long userCount();


    /**
     * 用户姓名模糊搜索
     */
    @Select("select * from admin_user where concat (id,username,nickname,sex) like '%' #{userSearchDto.username} '%' " +
            "and status!=" + STATUS_DELETE + " order by id desc limit #{pagination.offset},#{pagination.limit} ")
    List<AdminUser> findSearchUsers(@Param("userSearchDto") UserSearchDto userSearchDto, @Param("pagination") Pagination pagination);


    /**
     * 获取用户搜索列表总条数  姓名模糊搜索
     */
    @Select("select count(*) from admin_user where " +
            "concat (id,username,nickname,sex) like '%' #{userSearchDto.username} '%' " +
            "and status!= " + STATUS_DELETE)
    long userSearchCount(@Param("userSearchDto") UserSearchDto userSearchDto);


    /**
     * 插入新用户 admin_user
     */
    @Insert("insert into admin_user (username,nickname,sex,password_hash,avatar,job,phone,email) values (#{username},#{nickname},#{sex},#{passwordHash},#{avatar},#{job},#{phone},#{email})")
    int createUser(UserCreateDto u);

    /**
     * 编辑用户表 admin_user
     */
    @Update("update admin_user set password_hash=#{passwordHash},job=#{job}," +
            "phone=#{phone},email=#{email},username=#{username},nickname=#{nickname},sex=#{sex}" +
            "where id=#{userId}")
    int updateUser(UserUpdateDto userUpdateDto);

    /**
     * 删除用户 admin_user
     */
    @Update("update admin_user set status=" + STATUS_DELETE + " where id=#{userId}")
    void deleteUser(@Param("userId") int userId);


    /**
     * 通过username返回用户id
     */
    @Select("select id from admin_user where username=#{username} and status!=" + STATUS_DELETE + " limit 1")
    String findIdByUsername(@Param("username") String name);

    /**
     * 通过username返回用户信息
     */
    @Select("select * from admin_user where username=#{username} and status!=" + STATUS_DELETE + " limit 1")
    AdminUser findByUsername(@Param("username") String name);

    /**
     * 通过userId查找username
     */
    @Select("<script>" +
            "SELECT * FROM admin_user WHERE status !=" + STATUS_DELETE + " and id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<AdminUser> findUserListById(@Param("list") Set list);

    /**
     * 模糊查询盘点负责人名
     */
    @Select("SELECT * FROM admin_user WHERE username like CONCAT('%', #{title}, '%') AND status != "+STATUS_DELETE+"")
    List<AdminUser> findUserDim(@Param("title") String title);

    /**
     * 批量删除user
     */
    @Update("<script>" +
            " update admin_user set status= " + STATUS_DELETE +
            " where  id in " +
            " <foreach collection='ids' item='id' open='('  separator=','  close=')' > " +
            " #{id}" +
            " </foreach>" +
            "</script>")
    void deleteUsers(@Param("ids") List<Integer> ids);

    /**
     * 更改头像
     */
    @Update("update admin_user set avatar=#{avatar} where id=#{id}")
    int updateUserAvatar(@Param("id") Integer id, @Param("avatar") String avatar);
}
