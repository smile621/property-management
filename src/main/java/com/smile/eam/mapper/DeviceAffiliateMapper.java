package com.smile.eam.mapper;

import com.smile.eam.entity.AssetAffiliate;
import com.smile.eam.entity.DeviceAffiliate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface DeviceAffiliateMapper {

    @Select(" SELECT * FROM `asset_affiliate` WHERE asset_affiliate=#{id} AND status !=" + AssetAffiliate.STATUS_DELETE)
    List<DeviceAffiliate> findDeviceAffiliateById(@Param("id") int id);

    /**
     * 删除设备时需要删除归属在该设备下的绑定记录 asset_affiliate表
     */
    @Update(" <script>" +
            " UPDATE `asset_affiliate` SET status=" + AssetAffiliate.STATUS_DELETE + " WHERE status=" + AssetAffiliate.STATUS_NORMAL + " AND asset_id IN " +
            " <foreach item ='item' collection='ids' index='index' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    void deleteAttributeAffiliate(@Param("ids") Set<Integer> ids);
}
