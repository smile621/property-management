package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.BindSoftwareRequest;
import com.smile.eam.entity.AssetAffiliate;
import com.smile.eam.dto.UpdateSoftwarePropertyById;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface AssetAffiliateMapper {

    /**
     * 根据资产id列表,查找设备列表
     */
    @Select("<script>" +
            "SELECT * FROM asset_affiliate WHERE status != " + AssetAffiliate.STATUS_DELETE + " and asset_id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} " +
            "</foreach>" +
            "</script>")
    List<AssetAffiliate> findAssetAffiliateById(@Param("list") Set list);

    /**
     * 软件资产绑定
     */
    @Insert("insert into `asset_affiliate` (`asset_id`,`asset_affiliate`) values (#{assetId},#{assetAffiliate})")
    void bindSoftware(BindSoftwareRequest bindSoftwareRequest);

    /**
     * 判断该软件有没有绑定过该设备
     */
    @Select("select * from `asset_affiliate` where asset_id=#{assetId} and asset_affiliate=#{assetAffiliate} and status != " + AssetAffiliate.STATUS_DELETE)
    AssetAffiliate isSoftwareBindDevice(BindSoftwareRequest bindSoftwareRequest);

    /**
     * 传入id,查找软件绑定列表，对应的asset_id
     */
    @Select("select * from `asset_affiliate` where id=#{id} and status != " + AssetAffiliate.STATUS_DELETE)
    AssetAffiliate findAssetIdById(@Param("id") Integer id);

    /**
     * 根据软件资产Id,判断`asset_affiliate`表中，是否还有其他的绑定设备
     */
    @Select("select count(*) from `asset_affiliate` where asset_id=#{asset_id} and status != " + AssetAffiliate.STATUS_DELETE)
    int isExistAssetId(@Param("asset_id") Integer assetId);

    /**
     * 取消软件资产绑定
     */
    @Update("update  `asset_affiliate` set `status`=100 where id=#{id} and status != " + AssetAffiliate.STATUS_DELETE)
    int cancelBindSoftware(@Param("id") Integer id);

    /**
     * 软件资产归属列表
     */
    @Select("select * from `asset_affiliate`  where status != " + AssetAffiliate.STATUS_DELETE + "  order by id desc limit #{pagination.offset}, #{pagination.limit}")
    List<AssetAffiliate> bindSoftwareList(@Param("pagination") Pagination pagination);

    /**
     * 获取软件资产绑定列表全部list
     */
    @Select("select * from `asset_affiliate`  where status != " + AssetAffiliate.STATUS_DELETE)
    List<AssetAffiliate> allSoftwareList();

    /**
     * 对应的软件绑定列表也要删除相应软件
     */
    @Update("update `asset_affiliate` set status = " + AssetAffiliate.STATUS_DELETE + " where asset_id=#{id}")
    int deleteByAssetId(UpdateSoftwarePropertyById updateSoftwarePropertyById);

    /**
     * 软件资产绑定列表批量删除
     */
    @Select("<script>" +
            "update asset_affiliate set status= " + AssetAffiliate.STATUS_DELETE + " WHERE id in" +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'>#{item} </foreach>" +
            "</script>")
    void deleteSoftwareBindListByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据软件资产list id 查找对应的绑定列表
     */
    @Select(
            "<script>" +
                    "SELECT * FROM asset_affiliate WHERE status != " + AssetAffiliate.STATUS_DELETE + " and asset_id IN " +
                    "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
                    "#{item}" +
                    "</foreach>" +
                    "order by id desc limit #{pagination.offset},#{pagination.limit}" +
                    "</script>")
    List<AssetAffiliate> findListByAssetIds(@Param("list") List<Integer> assetIds, @Param("pagination") Pagination pagination);

    /**
     * 根据软件资产list id 查找对应的绑定列表
     */
    @Select(
            "<script>" +
                    "SELECT * FROM asset_affiliate WHERE status != " + AssetAffiliate.STATUS_DELETE + " and asset_affiliate IN " +
                    "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
                    "#{item}" +
                    "</foreach>" +
                    "order by id desc limit #{pagination.offset},#{pagination.limit}" +
                    "</script>")
    List<AssetAffiliate> findListByAssetAffiliates(@Param("list") List<Integer> assetAffiliates, @Param("pagination") Pagination pagination);

    /**
     * 软件模糊查找  byname
     */
    @Select("select * from asset_affiliate where concat (asset_id,created_at,id) like '%' #{name} '%' and status != " + AssetAffiliate.STATUS_DELETE +
            " order by id desc limit #{pagination.offset},#{pagination.limit} ")
    List<AssetAffiliate> findByName(@Param("name") String name, @Param("pagination") Pagination pagination);
}
