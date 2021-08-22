package com.smile.eam.mapper;


import com.smile.eam.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface HomeMapper {

    /**
     * 查询资产表当中归属的设备
     */
    @Select("<script>"+
            "SELECT * FROM fixed_asset WHERE status != #{status} AND device_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<FixedAsset> findAssetByDeviceIds(@Param("list")List list, @Param("status")int status);

    /**
     * 通过userid查找资产
     */
    @Select("SELECT * FROM fixed_asset WHERE user_id = #{userId} AND status != #{status}")
    List<FixedAsset> findAssetByUserId(@Param("userId")int userId, @Param("status")int status);

    /**
     * 获取代办列表中不同状态的个数
     */
    @Select("SELECT COUNT(*) FROM todo_record WHERE status != #{deleteStatus} AND status != #{status} AND  solve_id = #{userId}")
    int findTodoStatus(@Param("status")int status,@Param("deleteStatus")int deleteStatus,@Param("userId")int userId);

    /**
     * 获取故障个数
     */
    @Select("SELECT COUNT(*) FROM malfunction WHERE status = #{status} AND solve_id = #{userId}")
    int findMalfunction(@Param("userId")int userId,@Param("status")int status);

    /**
     * 获取服务异常个数
     */
    @Select("SELECT COUNT(*) FROM fixed_asset WHERE asset_category_id = #{categoryId} AND  status = #{status}")
    int findServiceErrorNumber(@Param("categoryId")int categoryId,@Param("status")int status);

    /**
     * 通过分类id查找所有的List<价格>
     */
    @Select("<script>"+
            "SELECT price FROM fixed_asset WHERE status != #{status} AND asset_category_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<BigDecimal> findPriceListByCategoryId(@Param("list")List categoryId, @Param("status")int status);

    /**
     * 查询所有的资产价格和时间
     */
    @Select("SELECT * FROM fixed_asset WHERE status != #{status}")
    List<PriceDate> findPriceDateAll(@Param("status")int status);

    /**
     * 查询所有的资产价格和时间
     */
    @Select("<script>"+
            "SELECT * FROM fixed_asset WHERE status != #{status} AND asset_category_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<PriceDate> findPriceDateByCategoryList(@Param("list")List list,@Param("status")int status);

    /**
     * 查询最大时间
     */
    @Select("SELECT created_at FROM fixed_asset ORDER BY created_at DESC LIMIT 1")
    Date findMaxDate();

    /**
     * 查询最小时间
     */
    @Select("SELECT created_at FROM fixed_asset ORDER BY created_at ASC LIMIT 1")
    Date findMinDate();

    /**
     * 查询故障表最小时间
     */
    @Select("SELECT created_at FROM malfunction ORDER BY created_at ASC LIMIT 1")
    Date findMinDateByMalfunction();

    /**
     * 查询故障表最大时间
     */
    @Select("SELECT created_at FROM malfunction ORDER BY created_at DESC LIMIT 1")
    Date findMaxDateByMalfunction();

    /**
     * 查故障表里所有的创建时间
     */
    @Select("SELECT created_at FROM malfunction")
    List<Date> findMalfunctionCreatedAll();

    /**
     * 查询软件归属表中的所有信息
     */
    @Select("SELECT * FROM service_affiliate WHERE status != #{status}")
    List<ServiceAffiliate> findServiceAffiliate(@Param("status")int status);

    /**
     * 通过idSet查资产
     */
    @Select("<script>"+
            "SELECT * FROM fixed_asset WHERE status != #{status} AND id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<ServiceHome> findAsset(@Param("list")Set set, @Param("status")int status);

    /**
     * 通过服务id查服务异常
     */
    @Select("<script>"+
            "SELECT * FROM service_affiliate WHERE status != #{status} AND asset_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<ServiceError> findServiceError(@Param("list")Set set, @Param("status")int status);

}
