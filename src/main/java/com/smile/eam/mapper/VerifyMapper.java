package com.smile.eam.mapper;

import com.smile.eam.entity.Asset;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface VerifyMapper {

    //查看该厂商id是否存在
    @Select("SELECT * FROM vendor WHERE id = #{id} AND status != "+ Asset.STATUS_DELETE+ "  LIMIT 1")
    String findVendorById(@Param("id")int vendorId);

    //验证购买路径是否存在
    @Select("SELECT * FROM buy_router WHERE id = #{id} AND status != " + Asset.STATUS_DELETE + " LIMIT 1")
    String findBuyRouterById(@Param("id")int buyRouterId);

    //验证分类id是否存在..
    @Select("SELECT * FROM asset_category WHERE id = #{id} AND status != " + Asset.STATUS_DELETE  + "  LIMIT 1")
    String findCategoryById(@Param("id")int cateGoryId);

}
