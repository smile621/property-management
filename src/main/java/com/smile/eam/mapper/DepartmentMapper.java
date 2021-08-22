package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.DepartmentCreateDto;
import com.smile.eam.dto.DepartmentUpdateDto;
import com.smile.eam.entity.AdminDepartment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

import static com.smile.eam.entity.AdminDepartment.STATUS_DELETE;
import static com.smile.eam.entity.AdminDepartment.STATUS_NORMAL;

public interface DepartmentMapper {

    /**
     * 获取父级部门列表
     */
    @Select("<script>" +
            "select * from admin_department where status!=" + STATUS_DELETE + " and parent_id= " + STATUS_NORMAL +
            " order by id desc " +
            " limit #{pagination.offset},#{pagination.limit}" +
            "</script>")
    List<AdminDepartment> findAllDepartment(@Param("pagination") Pagination pagination);

    /**
     * 获取父级部门列表总条数
     */
    @Select("select count(*) from admin_department where status !=" + STATUS_DELETE + " and parent_id=" + STATUS_NORMAL)
    long departmentCount();

    /**
     * 获取对应子集部门列表
     */
    @Select(" <script>" +
            " select * from admin_department " +
            " where parent_id IN " +
            " <foreach collection='parentIds' item='id' open='('   separator=',' close=')'  >" +
            "  #{id} " +
            " </foreach>" +
            " and status !=  " + STATUS_DELETE +
            " </script>")
    List<AdminDepartment> findSonDepartment(@Param("parentIds") Set<Integer> parentIds);

    /**
     * 根据多个部门id获取部门信息
     */
    @Select(" <script>" +
            " select * from admin_department " +
            " where id IN " +
            " <foreach collection='departmentIds' item='id' open='('   separator=',' close=')'  >" +
            "  #{id} " +
            " </foreach>" +
            " and status !=" + STATUS_DELETE +
            " </script>")
    List<AdminDepartment> findDepartmentByDepartmentIds(@Param("departmentIds") List<Integer> departmentIds);

    /**
     * 获得所有的部门信息
     */
    @Select("SELECT * FROM admin_department WHERE status != " + STATUS_DELETE)
    List<AdminDepartment> findDepartmentAll();

    /**
     * 部门删除 admin_department
     */
    @Update("update admin_department set status=" + STATUS_DELETE + " where id=#{departmentId}")
    void deleteDepartment(int departmentId);

    /**
     * 部门删除 admin_user_department
     */
    @Update("update admin_user_department set status=" + STATUS_DELETE + " where department_id=#{departmentId}")
    void deleteUserDepartment(int departmentId);

    /**
     * 通过部门名查找部门id
     */
    @Select("select id from admin_department where name=#{name} and status!=" + STATUS_DELETE + "  limit 1")
    String findDepartmentIdByName(String name);

    /**
     * 插入 新部门
     */
    @Insert("insert into admin_department (name,parent_id,principal_person_id) values (#{name},#{parentId},#{principalPersonId})")
    int createDepartment(DepartmentCreateDto departmentCreateDto);

    /**
     * 插入用户部门表 admin_user_department
     */
    @Insert("insert into admin_user_department  (department_id,user_id) values(#{departmentId},#{userId})")
    void updateUserDepartment2(@Param("departmentId") int departmentId, @Param("userId") int userId);

    /**
     * 删除用户部门表
     */
    @Update("update admin_user_department set status=" + STATUS_DELETE + " where user_id=#{userId}")
    void updateUserDepartment1(@Param("departmentId") int departmentId, @Param("userId") int userId);

    /**
     * 编辑部门表 admin_department
     */
    @Update("update admin_department set name=#{departmentName},parent_id=#{parentId},principal_person_id=#{principalPersonId} where id=#{departmentId}")
    int updateDepartment(DepartmentUpdateDto departmentUpdateDto);

    /**
     * 通过部门ID查找部门名
     */
    @Select("select name from admin_department where id=#{departmentId} and status!=" + STATUS_DELETE + " limit 1")
    String findDepartmentById(int departmentId);

    /**
     * 根据id查部门详情
     */
    @Select("select * from admin_department where id=#{departmentId} and status!=" + STATUS_DELETE + " limit 1")
    AdminDepartment getDetail(@Param("departmentId") int departmentId);
}
