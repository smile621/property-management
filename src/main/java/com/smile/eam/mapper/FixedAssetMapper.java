package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.FixedAsset;
import com.smile.eam.dto.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface FixedAssetMapper {

    /**
     * 软件资产新增
     */
    @Insert("insert into `fixed_asset` (`name`,`asset_category_id`,`price`,`buy_router_id`,`description`," +
            "`vendor_id`,`version`,`issue_id`,`warranty_number`,`start_at`,`end_at`,`buy_at`,`expired_at`,`status`,`flag`) " +
            "values (#{name},#{assetCategoryId},#{price},#{buyRouterId},#{description},#{vendorId}," +
            "#{version},#{issueId},#{warrantyNumber},#{startAt},#{endAt},#{buyAt},#{expiredAt}," + FixedAsset.STATUS_LEISURE + "," + FixedAsset.CATEGORY_ID_SOFTWARE + ")" )
    void addSoftwareProperty(SoftwarePropertyCreateRequest softwarePropertyCreateRequest);

    /**
     * 软件资产编辑
     */
    @Update("update `fixed_asset` set `name`=#{softwareProperty.name},`asset_category_id`=#{softwareProperty.assetCategoryId},`price`=#{softwareProperty.price}," +
            "`buy_router_id`=#{softwareProperty.buyRouterId},`description`=#{softwareProperty.description}," +
            "`vendor_id`=#{softwareProperty.vendorId},`version`=#{softwareProperty.version},`issue_id`=#{softwareProperty.issueId}," +
            "`warranty_number`=#{softwareProperty.warrantyNumber},`start_at`=#{softwareProperty.startAt},`end_at`=#{softwareProperty.endAt},expired_at=#{softwareProperty.expiredAt} where id=#{id.id}")
    int editSoftwareProperty(@Param("softwareProperty") SoftwarePropertyCreateRequest softwarePropertyCreateRequest, @Param("id") UpdateSoftwarePropertyById updateSoftwarePropertyById);


    /**
     * 获取软件资产详情
     */
    @Select("select * from fixed_asset where id=#{id} and status != " + FixedAsset.STATUS_DELETE)
    FixedAsset getSoftwareDetail(@Param("id") Integer id);

    /**
     * 软件资产删除
     */
    @Update("update `fixed_asset` set `status`= " + FixedAsset.STATUS_DELETE + " where id =#{id}")
    int deleteSoftwareProperty(UpdateSoftwarePropertyById updateSoftwarePropertyById);


    /**
     * 根据软件分类获取软件列表
     */
    @Select("select * from `fixed_asset` where `flag`= " + FixedAsset.CATEGORY_ID_SOFTWARE + " and status != " + FixedAsset.STATUS_DELETE +
            " order by id desc limit #{pagination.offset},#{pagination.limit}")
    List<FixedAsset> softwarePropertyList(@Param("pagination") Pagination pagination);

    /**
     * 修改资产状态为：0：正常
     */
    @Update("update `fixed_asset` set malfunction_status = " + FixedAsset.STATUS_NORMAL + " where id=#{id} and status != " + FixedAsset.STATUS_DELETE)
    int updateAssetStatus0(@Param("id") Integer id);

    /**
     * 修改资产状态为：6：处理中
     */
    @Update("update `fixed_asset` set malfunction_status = " + FixedAsset.STATUS_BEING_PROCESSED + " where id=#{id} and status != " + FixedAsset.STATUS_DELETE)
    int updateAssetStatus6(@Param("id") Integer id);

    /**
     * 修改资产状态为：1：闲置
     */
    @Update("update `fixed_asset` set status = " + FixedAsset.STATUS_LEISURE + " where id=#{id} and status != " + FixedAsset.STATUS_DELETE)
    void updateAssetStatus(@Param("id") Integer id);

    /**
     * 修改资产状态为：2：归属
     */
    @Update("update `fixed_asset` set status= " + FixedAsset.STATUS_AFFILIATION + " where id=#{assetId} and status != " + FixedAsset.STATUS_DELETE)
    int updateAssetStatusById(BindSoftwareRequest bindSoftwareRequest);

    /**
     * 传入软件id是否有返回数据
     */
    @Select("select * from `fixed_asset` where id=#{assetId} and status != " + FixedAsset.STATUS_DELETE)
    FixedAsset existBindSoftwarePropertyAssetId(BindSoftwareRequest bindSoftwareRequest);

    /**
     * 传入软件id是否有返回数据
     */
    @Select("select * from `fixed_asset` where id=#{asset_id} and status != " + FixedAsset.STATUS_DELETE)
    List<FixedAsset> softwareDetail(@Param("asset_id") Integer id);

    /**
     * 传入设备id是否有返回数据
     */
    @Select("select * from `fixed_asset` where id=#{assetAffiliate} and status != " + FixedAsset.STATUS_DELETE)
    FixedAsset existBindSoftwarePropertyAssetAffiliate(BindSoftwareRequest bindSoftwareRequest);

    /**
     * 获取软件列表总数,且userId对应
     */
    @Select("select count(*) from `fixed_asset` where `flag`= " + FixedAsset.CATEGORY_ID_SOFTWARE + " and status != " + FixedAsset.STATUS_DELETE)
    int softwarePropertyCount();

    /**
     * 获取软件列表
     */
    @Select("select * from `fixed_asset` where status != " + FixedAsset.STATUS_DELETE)
    List<FixedAsset> getAllFixedAssetList();

    /**
     * 查询所有的设备
     */
    @Select(" SELECT * FROM `fixed_asset` WHERE user_id = #{id} " +
            " LIMIT #{pagination.offset},#{pagination.limit}")
    List<DeviceDto> findAllDeviceByUserId(@Param("id") int userId, @Param("pagination") Pagination pagination);

    /**
     * 根据资产Id,循环查找对应资产名称
     */
    @Select(
            "<script>" +
                    "SELECT * FROM fixed_asset WHERE status != " + FixedAsset.STATUS_DELETE + " and id IN " +
                    "<if test='list != null'>" +
                    "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
                    "#{item}" +
                    "</foreach> " +
                    "</if>" +
                    "</script>"
    )
    List<FixedAsset> findFixedAssetById(@Param("list") Set list);


    /**
     * 根据资产Id,循环查找对应资产名称
     */
    @Select(
            "<script>" +
                    "SELECT * FROM fixed_asset WHERE status != " + FixedAsset.STATUS_DELETE + " and id IN " +
                    "<if test='list != null'>" +
                    "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
                    "#{item}" +
                    "</foreach> " +
                    "</if>" +
                    "</script>"
    )
    List<GetFixedAsset> findFixedAssetByIds(@Param("list") Set list);

    /**
     * 软件模糊查找..
     */
    @Select(
            "SELECT * FROM fixed_asset " +
                    "where concat (name,version,price,id,expired_date,buy_at) like '%' #{name} '%' and status != " + FixedAsset.STATUS_DELETE + " and flag= " + FixedAsset.CATEGORY_ID_SOFTWARE +
                    " ORDER BY id DESC" +
                    " LIMIT #{pagination.offset},#{pagination.limit}"
    )
    List<FixedAsset> findFixedAsset(@Param("asset_category_id") Integer assetCategoryId, @Param("name") String input, @Param("pagination") Pagination pagination);

    /**
     * 软件模糊查找，返回总条数.
     */
    @Select("SELECT count(*) FROM fixed_asset " +
            "where concat (name,version,price,id,expired_date) like '%' #{name} '%' and status != " + FixedAsset.STATUS_DELETE + " and asset_category_id= " + FixedAsset.CATEGORY_ID_SOFTWARE)
    int findFixedAssetCount(@Param("asset_category_id") Integer assetCategoryId, @Param("name") String input, @Param("pagination") Pagination pagination);

    /**
     * 输入软件资产id，判断是否有对应id
     */
    @Select("select * from `fixed_asset` where id=#{id} and status != " + FixedAsset.STATUS_DELETE)
    FixedAsset findSoftwarePropertyById(UpdateSoftwarePropertyById updateSoftwarePropertyById);


    /**
     * 软件资产是否重复新增
     */
    @Select("select * from `fixed_asset` where `name`=#{name} and `asset_category_id`=#{assetCategoryId} " +
            "and `vendor_id`=#{vendorId} and `version`=#{version}")
    FixedAsset existSoftwareProperty(SoftwarePropertyCreateRequest softwarePropertyCreateRequest);

    /**
     * 软件资产列表批量删除
     */
    @Select("<script>" +
            "update fixed_asset set status= " + FixedAsset.STATUS_DELETE + " WHERE id in" +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'>#{item} </foreach>" +
            "</script>")
    void deleteSoftwareListByIds(@Param("ids") List<Integer> ids);

    /**
     * 更新过保时间
     */
    @Update("update fixed_asset set `expired_date` = datediff(`expired_at`,now()) where flag = " + FixedAsset.CATEGORY_ID_SOFTWARE + " and status != " + FixedAsset.STATUS_DELETE)
    void updateExpiredDate();

    /**
     * 即将过保软件总数
     */
    @Select("select count(*) from fixed_asset where `expired_date`<10 and `expired_date`>0 and asset_category_id= " + FixedAsset.CATEGORY_ID_SOFTWARE + " and status != " + FixedAsset.STATUS_DELETE)
    int willPast();

    /**
     * 即将过保软件总数
     */
    @Select("select count(*) from fixed_asset where `expired_date`<=0 and asset_category_id= " + FixedAsset.CATEGORY_ID_SOFTWARE + " and status != " + FixedAsset.STATUS_DELETE)
    int past();

    /**
     * 获取某一年全部数据
     */
    @Select("select * from fixed_asset where date_format(created_at,'%y')=#{year} and status != " + FixedAsset.STATUS_DELETE)
    List<FixedAsset> allYearValue(@Param("year") Integer year);

    /**
     * 删除厂商时更新资产的厂商信息
     */
    @Update(" UPDATE `fixed_asset` SET vendor_id=0 WHERE vendor_id = #{id}")
    void updateVendorIdWhenDelete(@Param("id") int id);

    /**
     * 资产表中对应分类id设置为0
     */
    @Update("update fixed_asset set asset_category_id= 0 where flag= " + FixedAsset.CATEGORY_ID_SOFTWARE + " and asset_category_id=#{id} and status != " + FixedAsset.STATUS_DELETE)
    void edit(@Param("id") Integer id);
}
