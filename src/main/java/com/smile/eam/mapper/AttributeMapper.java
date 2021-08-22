package com.smile.eam.mapper;

import com.smile.eam.dto.DeviceBindUserDto;
import com.smile.eam.dto.DeviceLendDto;
import com.smile.eam.entity.AssetTrack;
import com.smile.eam.entity.FixedAsset;
import com.smile.eam.entity.FixedAssetTrack;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


public interface AttributeMapper {

    /**
     * 设备归属给用户  并自动解除与之前用户的归属
     */
    @Insert(" INSERT INTO `fixed_asset_track` (user_id,lend_user_id,fixed_asset_id,binding_start_at," +
            " created_at,updated_at,status) VALUES (#{bind.userId},0,#{bind.id}," +
            " #{time},#{time},#{time}," + FixedAssetTrack.STATUS_AFFILIATION + " )")
    int insertBindUser(@Param("bind") DeviceBindUserDto dto, @Param("time") String name);

    @Select(" select * from `fixed_asset_track` where fixed_asset_id = #{deviceId} and (status = " + FixedAssetTrack.STATUS_AFFILIATION + " OR status =" + FixedAssetTrack.STATUS_BORROW + ")")
    AssetTrack findFrontAttributeById(@Param("deviceId") int id);

    @Update(" UPDATE `fixed_asset` SET user_id = #{userId},updated_at = #{updatedAt},status = " + FixedAssetTrack.STATUS_AFFILIATION + " WHERE id = #{id}")
    void updateDeviceBindUser(@Param("id") int id, @Param("userId") int userId, @Param("updatedAt") String date);

    /**
     * 设备被借用
     */
    @Insert(" INSERT INTO `fixed_asset_track` (lend_user_id,fixed_asset_id,lend_start_at,lend_plan_return_at," +
            " lend_description,created_at,updated_at,status) VALUES (#{lend.lendUserId},#{lend.id}," +
            " #{time},#{lend.lendPlanReturnAt},#{lend.lendDescription},#{time},#{time}," + FixedAssetTrack.STATUS_BORROW + " )")
    int insertLendUser(@Param("lend") DeviceLendDto dto, @Param("time") String time);

    @Update("UPDATE `fixed_asset` SET user_id = 0,lend_user_id = #{userId},updated_at = #{updatedAt},status = " + FixedAssetTrack.STATUS_BORROW + " WHERE id = #{id}")
    int updateDeviceLendUser(@Param("id") int id, @Param("userId") int userId, @Param("updatedAt") String date);

    /**
     * 设备归属列表
     */
    @Select("SELECT * FROM `fixed_asset_track` WHERE status = " + FixedAssetTrack.STATUS_AFFILIATION + " OR status = " + FixedAssetTrack.STATUS_BORROW)
    List<AssetTrack> findListByStatus();

    /**
     * 设备解除归属
     */
    @Update("UPDATE `fixed_asset` SET user_id = 0,status = " + FixedAsset.STATUS_LEISURE + " WHERE id = #{id}")
    int returnAttribute(@Param("id") int id);

    @Update("UPDATE `fixed_asset_track` SET binding_end_at = #{time},updated_at = #{time},status=" + FixedAssetTrack.STATUS_DELETE + " WHERE status!=" + FixedAssetTrack.STATUS_DELETE + " and fixed_Asset_id =#{id}")
    void removeAttribute(@Param("time") String date, @Param("id") int id);

    /**
     * 用户归还设备
     */
    @Update("UPDATE `fixed_asset` SET lend_user_id = 0,status = " + FixedAssetTrack.STATUS_LEISURE + " WHERE id = #{id}")
    int lendOverDevice(@Param("id") int id);

    @Update("UPDATE `fixed_asset_track` SET lend_return_at = #{time},updated_at = #{time}," +
            " lend_end_description=#{description},status=" + FixedAssetTrack.STATUS_DELETE + " WHERE fixed_asset_id =#{id}")
    void lendBackDevice(@Param("time") String time, @Param("description") String description, @Param("id") int id);

}
