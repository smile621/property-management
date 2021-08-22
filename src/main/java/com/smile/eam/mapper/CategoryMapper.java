package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.CategoryCreateDto;
import com.smile.eam.dto.CategoryItemDto;
import com.smile.eam.dto.DeviceCategoryUpdateDto;
import com.smile.eam.dto.categoryParentDto;
import com.smile.eam.entity.AssetCategory;
import com.smile.eam.entity.Category;
import com.smile.eam.entity.Consumable;
import com.smile.eam.entity.Depreciation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface CategoryMapper {

    /**
     * 验证分类id是否存在
     */
    @Select("SELECT COUNT(*) FROM asset_category WHERE id = #{id} AND flag = #{flag} AND status != "+ Category.STATUS_DELETE+"")
    int findCategoryIdExist(@Param("id")int id,@Param("flag")int flag);

    /**
     * 通过CategoryId查询整个category
     */
    @Select("<script>" +
            "SELECT * FROM `asset_category` WHERE status !=" + Category.STATUS_DELETE + " AND id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}  " +
            "</foreach>" +
            "</script>")
    List<Category> findCategoriesByIds(@Param("list") Set list);

    /**
     * 通过CategoryId和flag查询整个category
     */
    @Select("<script>" +
            "SELECT * FROM asset_category WHERE status != "+Category.STATUS_DELETE+" AND flag=#{flag} and id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Category> findCategoryByIds(@Param("list") Set list, @Param("flag") int flag);

    /**
     * 查询id和parentId关系
     */
    @Select("SELECT * FROM asset_category WHERE status != "+Category.STATUS_DELETE+"")
    List<categoryParentDto> findIdParentId();

    /**
     * 通过分类id查询父类id
     */
    @Select("SELECT parent_id FROM asset_category WHERE  status != "+Category.STATUS_DELETE+" AND id = #{id}")
    int findCategoryParentId(@Param("id") int id);

    /**
     * 模糊查询分类名
     */
    @Select("SELECT id FROM asset_category WHERE status != "+Category.STATUS_DELETE+" AND name like CONCAT('%',#{title},'%')")
    List<Integer> findCategoryIdDim(@Param("title") String title);

    /**
     * 查询子类的分类对象
     */
    @Select("SELECT * FROM asset_category WHERE parent_id = #{id} AND status != "+ Category.STATUS_DELETE +"")
    List<Category> findCategorySonList(@Param("id") int id);

    /**
     * 删除id对应的分类
     */
    @Update("UPDATE asset_category SET status = " + Category.STATUS_DELETE + " WHERE id = #{id}")
    int deleteCategory(@Param("id") int id);

    //查询所有的分类
    @Select("SELECT * FROM asset_category WHERE status != "+ Depreciation.STATUS_DELETE+"")
    List<CategoryItemDto> findCategoryAll();


    /**
     * asset_category
     */
    @Select(" SELECT * FROM `asset_category` WHERE id = #{id} AND status !=" + Category.STATUS_DELETE)
    Category findOneById(@Param("id") int id);

    /**
     * 通过耗材静态默认ID查找分类
     */
    @Select(" SELECT * FROM `asset_category` WHERE id = " + Consumable.CATEGORY_ID_CONSUMABLE + " AND status !=" + Category.STATUS_DELETE)
    Category findTopConsumable();

    /**
     * 查询所有的设备列表
     */
    @Select(" <script> " +
            " SELECT * FROM `asset_category` WHERE status != " + Category.STATUS_DELETE + " AND id IN" +
            " <foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<Category> findAllByIds(@Param("list") Set<Integer> id);

    /**
     * 查询所有的设备
     */
    @Select(" SELECT * FROM `asset_category` WHERE  parent_id = #{id} AND status != " + Category.STATUS_DELETE + " LIMIT #{pagination.offset},#{pagination.limit}")
    List<Category> findAllByCategoryId(@Param("id") int id, @Param("pagination") Pagination pagination);

    @Select(" SELECT * FROM `asset_category` WHERE  parent_id = #{id} AND status != " + Category.STATUS_DELETE)
    List<Category> findAllByCategoryIds(@Param("id") int id);


    /**
     * 模糊查找设备分类
     */
    @Select(" select * from `asset_category` where status!=" + Category.STATUS_DELETE + " and name like concat('%',#{str},'%') LIMIT #{pagination.offset},#{pagination.limit}")
    List<Category> searchCategories(@Param("str") String str, @Param("pagination") Pagination pagination);

    /**
     * 删除设备列表
     */
    @Update(" UPDATE `asset_category` SET status = " + Category.STATUS_DELETE + "WHERE id = #{categoryId}")
    int deleteDeviceCategory(@Param("categoryId") int id);

    /**
     * 编辑设备目录
     */
    @Update(" update `asset_category` SET name = #{device.name},description=#{device.description}," +
            " parent_id = #{device.parentId}, updated_at = #{updatedAt} WHERE id = #{dto.id} AND status !=" + Category.STATUS_DELETE)
    int updateDeviceCategory(@Param("device") DeviceCategoryUpdateDto dto, @Param("updatedAt") String updatedAt);

    /**
     * 新增设备目录
     */
    @Insert(" INSERT INTO `asset_category` (name,description,parent_id) " +
            " VALUES (#{device.name},#{device.description},#{device.parentId})")
    int insertCategory(@Param("device") DeviceCategoryUpdateDto dto);


    /**
     * 新增耗材分类
     */
    @Insert(" INSERT INTO `asset_category` (name,description,parent_id) " +
            " VALUES (#{device.name},#{device.description},#{device.parentId})")
    int insertConsumableCategory(@Param("device") CategoryCreateDto dto);

    @Update(" update `asset_category` SET name = #{consumable.name},description=#{consumable.description}," +
            " parent_id = #{consumable.parentId}, updated_at = #{device.updatedAt} WHERE id = #{id} AND status !=" + AssetCategory.STATUS_DELETE)
    int updateCategory(@Param("consumable") CategoryCreateDto dto, @Param("id") int id);


}
