package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.VendorCreateRequest;
import com.smile.eam.dto.VendorDto;
import com.smile.eam.dto.VendorUpdateDto;
import com.smile.eam.dto.VendorUserDto;
import com.smile.eam.entity.Part;
import com.smile.eam.entity.Vendor;
import com.smile.eam.entity.VendorDetail;
import com.smile.eam.entity.VendorUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface VendorMapper {

    //     通过vendorId查询整个Vendor
    @Select(
            "<script>" +
                    "SELECT * FROM vendor WHERE status != 100 and id IN" +
                    "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
                    "#{item}" +
                    "</foreach>" +
                    "</script>"
    )
    List<Vendor> findVendorById(@Param("list") Set list);

    //模糊查询厂商名..
    @Select("SELECT id FROM vendor WHERE name like CONCAT('%',#{title},'%')")
    List<Integer> findVendorIdVague(@Param("title") String title);

    /**
     * 模糊查询厂商   厂商删除但是耗材不需要删除，所以这里查询所有的厂商  包括已删除的
     */
    @Select("SELECT * FROM vendor WHERE name like CONCAT('%',#{title},'%')")
    List<Vendor> findVendorsByName(@Param("title") String titles);

    //返回厂商的详细信息
    @Select("SELECT * FROM vendor WHERE name like CONCAT('%',#{title},'%') LIMIT #{pagination.offset},#{pagination.limit}")
    List<VendorDetail> findVendorDetailByName(@Param("title") String title, @Param("pagination") Pagination pagination);

    //通过List[vendorId]获得List<Part>
    @Select("<script>" +
            "SELECT * FROM fixed_asset WHERE status != "+Vendor.STATUS_DELETE+" AND flag = " + Part.CATEGORY_ID_PART+ " AND vendor_id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>"
    )
    List<Part> findPartsByVendorIds(@Param("list") List list);

    /**
     * 新增厂商信息
     */
    @Insert(" INSERT INTO `vendor`(name,description,location,created_at,updated_at) " +
            " VALUES (#{vendor.name},#{vendor.description},#{vendor.location},#{time}" +
            " ,#{time})")
    @Options(useGeneratedKeys = false, keyProperty = "id")
    int insertVendor(@Param("vendor") VendorCreateRequest vendor, @Param("time") String time);

    /**
     * 插入厂商信息，并将主键ID返回，供后续当做字段插入厂商联系人表
     */
    @Insert(" INSERT INTO `vendor_user`(vendor_id,name,email,job,created_at,updated_at) " +
            " VALUES (#{vendorId},#{user.name},#{user.email},#{user.job},#{time}," +
            " #{time})")
    int insertVendorUser(@Param("vendorId") int id, @Param("user") VendorUserDto vendorUser, @Param("time") String name);

    /**
     * 删除厂商
     */
    @Update(" UPDATE `vendor` SET status = 100 WHERE id = #{id}")
    int deleteVendor(@Param("id") int id);

    /**
     * 删除厂商联系人
     */
    @Update(" UPDATE `vendor_user` SET status = 100 WHERE vendor_id = #{id}")
    void deleteVendorUser(@Param("id") int id);

    @Select(" SELECT * FROM `vendor_user` WHERE status !=" + VendorUser.STATUS_DELETE + " AND vendor_id=#{id}")
    List<VendorUser> findVendorUserByVendorId(@Param("id") int id);

    /**
     * 获取所有的厂商信息
     */
    @Select(" SELECT * FROM `vendor` WHERE status != " + Vendor.STATUS_DELETE)
    List<Vendor> findAllVendor();

    /**
     * 通过厂商id获取厂商信息   list
     */
    @Select(" <script> " +
            " select * from `vendor` where status != " + Vendor.STATUS_DELETE + " and id in" +
            " <foreach item = 'item' collection = 'list' separator = ',' open ='(' close = ')'>" +
            " #{item}" +
            " </foreach>" +
            " </script> ")
    List<Vendor> findAllByIds(@Param("list") Set<Integer> list);

    /**
     * 编辑厂商信息
     */
    @Update(" UPDATE `vendor` SET name=#{vendor.name},updated_at =#{updatedAt}" +
            " description=#{vendor.description},location=#{vendor.location} " +
            " WHERE id=#{vendor.id} AND status !=100")
    int editVendor(@Param("vendor") VendorUpdateDto vendorUpdateDto, @Param("updatedAt") String updatedAt);

    /**
     * 编辑厂商联系人信息
     */
    @Update(" UPDATE `vendor_user` SET name=#{vendor.name},updated_at =#{updatedAt}" +
            " phone=#{vendor.description},job=#{vendor.location},email=#{vendor.email} " +
            " WHERE id=#{vendor.id} AND status !=100 ")
    int editVendorUser(@Param("vendor") VendorUserDto vendorUserDto, @Param("updatedAt") String updatedAt);

    /**
     * 通过id去查厂商是否存在
     */
    @Select(" SELECT * FROM `vendor` WHERE id = #{id} and status !=" + Vendor.STATUS_DELETE)
    VendorDto findOneById(@Param("id") int id);

    /**
     * 查询厂商联系人信息列表
     */
    @Select(" SELECT * FROM `vendor_user` where vendor_id=#{id} and status !=" + VendorUser.STATUS_DELETE)
    List<VendorUser> findAllVendorUserById(@Param("id") int id);

    /**
     * 通过id去查厂商是否存在
     */
    @Select(" SELECT * FROM `vendor` WHERE id = #{id} and status != " + Vendor.STATUS_DELETE)
    Vendor isVendor(@Param("id") Integer id);
}
