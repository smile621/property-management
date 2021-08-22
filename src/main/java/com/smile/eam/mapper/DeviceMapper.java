package com.smile.eam.mapper;

import com.smile.eam.dto.DeviceCreateDto;
import com.smile.eam.dto.DeviceSearchDto;
import com.smile.eam.dto.DeviceUpdateDto;
import com.smile.eam.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

public interface DeviceMapper {

    /**
     * 查询设备
     */
    @Select(" SELECT * FROM `fixed_asset` WHERE id = #{id}")
    Device findDeviceById(@Param("id") int id);

    /**
     * 增加设备
     */
    @Insert(" INSERT INTO `fixed_asset` (name,asset_category_id,user_id," +
            " buy_router_id,vendor_id,depreciate_id,description,price,mac,ip," +
            " buy_at,expired_at) VALUES " +
            " (#{device.name},#{device.assetCategoryId},#{device.userId}," +
            " #{device.buyRouterId},#{device.vendorId},#{device.depreciateId},#{device.description}," +
            " #{device.price},#{device.mac},#{device.ip}," +
            " #{device.buyAt},#{device.expiredAt})")
    int insertDevice(@Param("device") DeviceCreateDto device);

    /**
     * 删除设备
     */
    @Update(" UPDATE `fixed_asset` SET status = " + FixedAsset.STATUS_DELETE + " WHERE id = #{id}")
    int deleteDevice(@Param("id") int id);

    /**
     * 编辑设备
     */
    @Update(" UPDATE `fixed_asset` SET buy_router_id=#{device.buyRouterId},name=#{device.name},asset_category_id=#{device.assetCategoryId}," +
            " vendor_id=#{device.vendorId},price=#{device.price},ip=#{device.ip},mac=#{device.mac},buy_at=#{device.buyAt}," +
            " expired_at=#{device.expiredAt},updated_at=#{updatedAt},description=#{device.description}," +
            " depreciate_id=#{device.depreciateId} WHERE id=#{device.id} ")
    int editDevice(@Param("device") DeviceUpdateDto deviceUpdateDto, @Param("updatedAt") String updatedAt);

    /**
     * 根据category_id查询设备
     */
    @Select(" <script>" +
            " SELECT * FROM `fixed_asset` WHERE status!=" + FixedAsset.STATUS_DELETE + " AND asset_category_id IN " +
            " <foreach item ='item' collection='ids' index='index' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " ORDER BY id DESC" +
            " </script>")
    List<Device> findDeviceByCategoryId(@Param("ids") Set<Integer> ids);

    @Select(" <script>" +
            " SELECT * FROM `fixed_asset` WHERE status!=" + FixedAsset.STATUS_DELETE + " AND id IN " +
            " <foreach item ='item' collection='ids' index='index' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " ORDER BY id DESC" +
            " </script>")
    List<Device> findDevicesByIds(@Param("ids") Set<Integer> ids);

    /**
     * 设备搜索  模糊查找
     */
    @Select(" <script> " +
            " SELECT * FROM `fixed_asset` " +
            " <where> status !=  " + FixedAsset.STATUS_DELETE +
            " <if test = 'device.name != null '> AND name LIKE CONCAT('%',#{device.name},'%') </if>" +
            " <if test = 'device.description != null '> OR description LIKE CONCAT('%',#{device.description},'%') </if>" +
            " </where> ORDER BY id DESC" +
            " </script> ")
    List<Device> searchDevice(@Param("device") DeviceSearchDto str);

    @Select(" SELECT * FROM `vendor` WHERE status !=" + Vendor.STATUS_DELETE + " AND name LIKE CONCAT ('%',#{name},'%') ")
    List<Vendor> searchVendorIdsByName(@Param("name") String name);


    @Select(" SELECT * FROM `asset_category` WHERE  id = #{id} AND status != " + Category.STATUS_DELETE)
    List<Category> findAllCategories(@Param("id") int id);

    /**
     * 设备统计
     */
    @Select(" <script>" +
            " SELECT * FROM `fixed_asset` WHERE status!=" + FixedAsset.STATUS_DELETE + " AND status !=" + FixedAsset.STATUS_LOSE + " AND asset_category_id IN " +
            " <foreach item ='item' collection='ids' index='index' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<Device> statisticDeviceByCategoryId(@Param("ids") List<Integer> ids);

    /**
     * 模糊搜索厂商名
     */
    @Select(" <script> " +
            " SELECT * FROM `fixed_asset` WHERE status !=" + FixedAsset.STATUS_DELETE + " AND vendor_id IN" +
            " <foreach collection='ids' item='item' index='index' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<Device> searchLikeVendor(@Param("ids") Set<Integer> ids);

    /**
     * 设备故障状态查询
     */
    @Select(" SELECT * FROM `fixed_asset` WHERE id = #{id} AND ( malfunction_status =" + Malfunction.STATUS_MALFUNCTION +
            " OR status = " + Malfunction.STATUS_BEING_PROCESSED + " OR status = " + Malfunction.STATUS_PENDING + ")")
    Device findMalfunctionStatus(@Param("id") int id);
}
