package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.AssetCategory;
import com.smile.eam.dto.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface AssetCategoryMapper {
    /**
     * 判断软件列表目录表是否已经有对应的目录
     */
    @Select("select * from `asset_category` where `name`=#{name} and `parent_id`=#{parentId} and status !=" + AssetCategory.STATUS_DELETE)
    SoftwareCreateRequest existSoftware(SoftwareCreateRequest softwareCreateRequest);

    /**
     * 判断资产分类传入的父类id是否是3，以及是否存在
     */
    @Select("select * from asset_category where id=#{id} and parent_id= " + AssetCategory.CATEGORY_ID_SOFTWARE + " and status != " + AssetCategory.STATUS_DELETE)
    AssetCategory isExist(@Param("id") Integer id);


    /**
     * 输入软件信息，添加软件进 asset_category
     */
    @Insert("insert into `asset_category` (`name`,`description`,`parent_id`,`flag`) values (#{name},#{description},#{parentId}, " + AssetCategory.CATEGORY_ID_SOFTWARE + ")")
    void addSoftware(SoftwareCreateRequest software);

    /**
     * 修改asset_category数据
     */
    @Update("update `asset_category` set `name`=#{name}, `parent_id`=#{parentId} WHERE id = #{id}")
    int updateSoftware(UpdateSoftwareByIdRequest updateSoftwareByIdRequest);

    /**
     * 软件资产分类查找
     */
    @Select("select * from `asset_category` where `id`=#{id} and status != " + AssetCategory.STATUS_DELETE)
    AssetCategory findSoftware(DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest);

    /**
     * 是否有该软件分类
     */
    @Select("select * from `asset_category` where `id`=#{id} and status != " + AssetCategory.STATUS_DELETE)
    AssetCategory isCategory(@Param("id") Integer id);

    /**
     * 软件资产分类删除
     */
    @Update("update `asset_category` set `status`= " + AssetCategory.STATUS_DELETE + " WHERE id = #{id}")
    int deleteSoftware(DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest);

    /**
     * 软件资产分类列表
     */
    @Select("select * from `asset_category` where `parent_id`=#{asset.parentId} and status != " + AssetCategory.STATUS_DELETE +
            " order by id desc limit #{pagination.offset},#{pagination.limit} ")
    List<SoftwareCategoryResponse> findSoftwareCategory(@Param("asset") GetSoftwareCategoryById getSoftwareCategoryById, @Param("pagination") Pagination pagination);

    /**
     * 获取软件资产分类列表总数
     */
    @Select("select count(*) from `asset_category` where  `parent_id`= " + AssetCategory.CATEGORY_ID_SOFTWARE + " and status != " + AssetCategory.STATUS_DELETE)
    int softwareCategoryCount();

    /**
     * 获取所有分类列表
     */
    @Select("select * from asset_category where flag = " + AssetCategory.CATEGORY_ID_SOFTWARE + " and status != " + AssetCategory.STATUS_DELETE)
    List<TreeNode> getAll();

    /**
     * 批量删除软件分类列表
     */
    @Select("<script>" +
            "update asset_category set status= " + AssetCategory.STATUS_DELETE + " WHERE id in" +
            "<foreach collection='ids' item='item' index='index' open='(' separator=',' close=')'>#{item} </foreach>" +
            "</script>")
    void deleteSoftwareCategoryListByIds(@Param("ids") List<Integer> ids);

    /**
     * 软件分类模糊查找
     */
    @Select("select * from asset_category where concat(id,name) like '%' #{name} '%' and status != " + AssetCategory.STATUS_DELETE +
            " and parent_id= " + AssetCategory.CATEGORY_ID_SOFTWARE + " order by id desc limit #{pagination.offset},#{pagination.limit} ")
    List<AssetCategory> lookFor(@Param("name") String name, @Param("pagination") Pagination pagination);

    /**
     * 软件分类模糊查找
     */
    @Select("select count(*) from asset_category where concat(id,name) like '%' #{name} '%' and status != " + AssetCategory.STATUS_DELETE + " and parent_id= " + AssetCategory.CATEGORY_ID_SOFTWARE)
    int lookForCount(@Param("name") String name);

    /**
     * 通过CategoryId查询整个category
     */

    @Select(
            "<script>" +
                    "SELECT * FROM asset_category WHERE flag= " + AssetCategory.CATEGORY_ID_SOFTWARE + "  and status != " + AssetCategory.STATUS_DELETE + " and id IN" +
                    "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
                    "#{item} " +
                    "</foreach>" +
                    "</script>"
    )
    List<AssetCategory> findCategoryByIds(@Param("list") Set list);
}
