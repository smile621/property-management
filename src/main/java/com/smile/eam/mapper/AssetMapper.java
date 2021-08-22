package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Part;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AssetMapper {

    //通过分类id列表查询当前类别下的资产数据
    @Select({"<script>",
            "SELECT * FROM fixed_asset WHERE asset_category_id IN ",
            "<foreach item='item' collection='list' separator=',' open='(' close=')' > #{item} </foreach>",
            "AND delete_id = 0",
            "ORDER BY id ASC LIMIT #{pagination.offset}, #{pagination.limit}",
            "</script>"})
    List<Part> findPartList(@Param("list")List<String> list, @Param("pagination") Pagination pagination);

    //获取某个分类的子分类Id
    @Select("SELECT * FROM asset_category WHERE parent_id = #{CategoryId}")
    List<String> findCategoryId(@Param("CategoryId")int CategoryId);

}
