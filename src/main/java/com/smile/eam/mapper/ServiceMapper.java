package com.smile.eam.mapper;


import com.smile.eam.common.Pagination;
import com.smile.eam.dto.*;
import com.smile.eam.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;


public interface ServiceMapper {

    /**
     * 查找设备表中的所有服务项
     */
    @Select("select * from fixed_asset where asset_category_id=" + Asset.CATEGORY_ID_SERVICE + " and status!=" + Asset.STATUS_DELETE + " limit #{pagination.offset},#{pagination.limit}")
    List<FixedAsset> getList(@Param("pagination") Pagination pagination);

    /**
     * 列表总条数
     */
    @Select("select count(*) from fixed_asset where asset_category_id=" + Asset.CATEGORY_ID_SERVICE + " and status!=" + Asset.STATUS_DELETE)
    Integer getCount();

    /**
     * 通过（多条）服务ID 查找（多条）厂商ID
     */
    @Select(" <script> " +
            " select buy_router_id from fixed_asset " +
            " where id in " +
            " <foreach collection='ids' item='id' open='(' separator=',' close=')' > " +
            "  #{id} " +
            " </foreach>" +
            " and status!= " + Asset.STATUS_DELETE +
            " </script> ")
    List<Integer> findBuyRouterIds(@Param("ids") List<Integer> ids);

    /**
     * 通过服务ID查找服务详情
     */
    @Select("<script>  " +
            " select * from fixed_asset " +
            " where id in " +
            " <foreach collection='ids' item='id' open='('  separator=','  close=')' > " +
            " #{id} " +
            " </foreach> " +
            " limit #{pagination.offset},#{pagination.limit} " +
            " </script>")
    List<FixedAsset> findByIds(@Param("ids") Set<Integer> ids, @Param("pagination") Pagination pagination);

    /**
     * 新增服务
     */
    @Insert("insert into fixed_asset (name,description,status,price,buy_at,expired_at,buy_router_id,asset_category_id,vendor_id) " +
            " values (#{dto.name},#{dto.description},#{dto.status},#{dto.price},#{dto.buyAt},#{dto.expiredAt},#{dto.buyRouterId}," + Asset.CATEGORY_ID_SERVICE + ",#{dto.vendorId})")
    int create(@Param("dto") ServiceCreateDto serviceCreateDto);

    /**
     * 根据名字查找服务项id
     */
    @Select("select id from fixed_asset where name=#{name} and status != " + Asset.STATUS_DELETE)
    String findByName(@Param("name") String name);

    /**
     * 编辑服务
     */
    @Update("update fixed_asset set " +
            " name=#{dto.name},description=#{dto.description},status=#{dto.status}, " +
            " price=#{dto.price},buy_at=#{dto.buyAt},expired_at=#{dto.expiredAt}, " +
            " buy_router_id=#{dto.buyRouterId},vendor_id=#{dto.vendorId},asset_category_id=" + Asset.CATEGORY_ID_SERVICE + "  where id=#{dto.id} ")
    int update(@Param("dto") ServiceUpdateDto serviceUpdateDto);

    /**
     * 查看服务详情
     */
    @Select("select * from fixed_asset where id=#{id}")
    FixedAsset getDetail(@Param("id") int id);

    /**
     * 根据服务ID  查找归属设备ID
     */
    @Select("select asset_affiliate from service_affiliate where asset_id=#{id} and status!=" + Asset.STATUS_DELETE)
    List<Integer> getAffiliates(@Param("id") int id);

    /**
     * 根据绑定设备ID 返回绑定设备名
     */
    @Select("<script>" +
            " select * from fixed_asset " +
            " where id in " +
            " <foreach collection='ids' item='id' open='('  separator=','  close=')'  > " +
            "  #{id} " +
            " </foreach>" +
            " and status!= " + Asset.STATUS_DELETE +
            " </script>")
    List<FixedAsset> getFixedAssetByIds(@Param("ids") List<Integer> ids);

    /**
     * 服务列表模糊搜索
     */
    @Select("select * from fixed_asset where concat (id,name,description,price) like '%' #{dto.name} '%' " +
            " and status!=" + Asset.STATUS_DELETE + " and asset_category_id=" + Asset.CATEGORY_ID_SERVICE + " order by id desc limit #{pagination.offset},#{pagination.limit}")
    List<FixedAsset> getSearchList(@Param("pagination") Pagination pagination, @Param("dto") ServiceSearchDto serviceSearchDto);

    /**
     * 搜索列表条数
     */
    @Select("select count(*) from fixed_asset where concat (id,name,description,price) like '%' #{dto.name} '%' " +
            " and status!=" + Asset.STATUS_DELETE + " and asset_category_id=" + Asset.CATEGORY_ID_SERVICE + " order by id desc ")
    Integer getSearchCount(@Param("dto") ServiceSearchDto serviceSearchDto);

    /**
     * 删除服务
     */
    @Update("update fixed_asset set status=" + Asset.STATUS_DELETE + " where id=#{id}")
    int delete(@Param("id") int id);

    /**
     * 批量删除
     */
    @Update("<script>" +
            " update fixed_asset set status= " + Asset.STATUS_DELETE +
            " where id in " +
            " <foreach collection='ids' item='id' open='(' separator=',' close=')' >" +
            " #{id} " +
            " </foreach> " +
            " </script> ")
    int deleteQuery(@Param("ids") List<Integer> ids);

    /**
     * 状态变化
     */
    @Update("update fixed_asset set status=#{code} where id=#{id}")
    void changeStatus(@Param("code") int code, @Param("id") int id);

    /**
     * 绑定设备
     */
    @Insert("insert into service_affiliate (asset_id,asset_affiliate) values (#{dto.assetId},#{dto.affiliateId})")
    int bind(@Param("dto") ServiceBindDto serviceBindDto);

    /**
     * 创建报告异常
     */
    @Insert("insert into service_error (asset_id,description,start_at) values (#{dto.assetId},#{dto.description},#{dto.startAt})")
    void createError(@Param("dto") ServiceErrorDto serviceErrorDto);

    /**
     * 查找 service——affiliate 获取所有已绑定的服务ID
     */
    @Select("select asset_id from service_affiliate where status!=" + Asset.STATUS_DELETE)
    List<Integer> getAllIds();

    /**
     * 查找 service——affiliate 获取所有已绑定的服务ID
     */
    @Select("select asset_id from service_affiliate where concat (asset_id,id) like '%' #{dto.name} '%' and  status!=" + Asset.STATUS_DELETE +
            " limit #{pagination.offset},#{pagination.limit} ")
    List<Integer> getAllSearchIds(@Param("dto") ServiceAffiliateSearchDto serviceAffiliateSearchDto, @Param("pagination") Pagination pagination);

    /**
     * 根据资产id列表,查找设备列表
     */
    @Select("<script>" +
            "SELECT * FROM service_affiliate WHERE asset_id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            " and status!=" + Asset.STATUS_DELETE +
            "</script>")
    List<ServiceAffiliate> findAssetAffiliateById(@Param("list") Set list);

    @Select(" select * from `service_affiliate` where asset_affiliate=#{id} and status !=" + ServiceAffiliate.STATUS_DELETE)
    List<DeviceAffiliate> findServiceAffiliateById(@Param("id") int id);

    /**
     * 解除该设备所有归属（当该设备被删除也要调用）
     */
    @Update("update service_affiliate set status=" + Asset.STATUS_DELETE + " where asset_id=#{id}")
    void deleteAffiliate(@Param("id") int id);

    /**
     * 归属的服务删除的时候删除
     */
    @Update("update service_affiliate set status=" + Asset.STATUS_DELETE + " where asset_affiliate=#{id}")
    void deleteAffiliate2(@Param("id") int id);

    /**
     * 查service_error表 获取所有服务异常列表
     */
    @Select("select * from service_error where status!=" + Asset.STATUS_DELETE + " limit #{pagination.offset},#{pagination.limit}")
    List<ServiceError> getAllError(@Param("pagination") Pagination pagination);

    /**
     * 模糊搜索查service_error表
     */
    @Select("select * from service_error where concat (asset_id,id,description) like '%' #{dto.name} '%' and status!= " + Asset.STATUS_DELETE +
            " limit #{pagination.offset},#{pagination.limit}")
    List<ServiceError> searchError(@Param("dto") ServiceAffiliateSearchDto serviceAffiliateSearchDto, @Param("pagination") Pagination pagination);

    /**
     * 异常处理
     */
    @Update("update service_error set error_status=" + Asset.STATUS_LEISURE + " , end_at=#{endAt} where id=#{id}")
    void deleteError(ServiceErrorResolveDto serviceErrorResolveDto);

    /**
     * 异常删除
     */
    @Update("update service_error set status=" + Asset.STATUS_DELETE + " where asset_id=#{id}")
    void deleteError2(@Param("id") int id);
}
