package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Category;
import com.smile.eam.entity.Part;
import com.smile.eam.entity.Track;
import com.smile.eam.dto.*;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface PartMapper {

    /**
     *通过分类id列表查询当前类别下的配件
     */
    @Select({"<script>",
            "SELECT * FROM fixed_asset WHERE status != #{status} AND asset_category_id ",
            "  IN <foreach item='item' collection='list' separator=',' open='(' close=')' > #{item} </foreach>",
            "<if test='partSearchDto.title != null' > AND name like CONCAT('%', #{partSearchDto.title}, '%')  </if> ",
            "<if test='partSearchDto.title != null' > OR description like CONCAT('%', #{partSearchDto.title}, '%')  </if> ",
            "<if test='partSearchDto.title != null' > OR price like CONCAT('%', #{partSearchDto.title}, '%')  </if> ",
            "ORDER BY id ASC LIMIT #{p.offset}, #{p.limit}",
            "</script>"})
    List<Part> findPartList(@Param("list")List<String> list, @Param("status")int delStatus, @Param("partSearchDto") PartSearchDto partSearchDto , @Param("p") Pagination pagination);

    @Select({"<script>",
            "SELECT * FROM fixed_asset WHERE status != #{status} AND asset_category_id ",
            "  IN <foreach item='item' collection='list' separator=',' open='(' close=')' > #{item} </foreach>",
            "ORDER BY id ASC LIMIT #{p.offset}, #{p.limit}",
            "</script>"})
    List<Part> findPartListCopy(@Param("list")List<String> list, @Param("status")int delStatus , @Param("p")Pagination pagination);

    @Select({"<script>",
            "SELECT * FROM fixed_asset WHERE flag = "+Part.CATEGORY_ID_PART+" AND status != "+Part.STATUS_DELETE+" ",
            "<if test='partSearchDto.title != null ' > AND ( name like CONCAT('%', #{partSearchDto.title}, '%') ",
            " OR description like CONCAT('%', #{partSearchDto.title}, '%') ",
            " OR price like CONCAT('%', #{partSearchDto.title}, '%') ) </if> ",
            "ORDER BY id ASC LIMIT #{p.offset}, #{p.limit}",
            "</script>"})
    List<Part> findParts(@Param("partSearchDto") PartSearchDto partSearchDto , @Param("p")Pagination pagination);
    /**
     *向fixed_asset固定资产表插入配件数据
     */
    @Insert("INSERT INTO fixed_asset (asset_category_id,flag,buy_router_id,vendor_id,name,description,price,buy_at,expired_at) " +
            "VALUES (#{partCreateDto.assetCategoryId},#{flag},#{partCreateDto.buyRouterId},#{partCreateDto.vendorId},#{partCreateDto.name}," +
            "#{partCreateDto.description},#{partCreateDto.price},#{partCreateDto.buyAt},#{partCreateDto.expiredAt})")
    int create(@Param("partCreateDto") PartCreateDto partCreateDto, @Param("flag")int flag);

    /**
     *   删除配件
     */
    @Update(
            "<script>" +
                    "UPDATE fixed_asset SET status = #{status} WHERE id IN" +
                    "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
                    "#{item}" +
                    "</foreach>" +
                    "</script>"
    )
    int deletePart(@Param("list") List list,@Param("status")int status);

    /**
     *  更新配件
     */
    @Update(
            "UPDATE fixed_asset SET asset_category_id = #{part.assetCategoryId},buy_router_id = #{part.buyRouterId},vendor_id=#{part.vendorId},"+
                    "description = #{part.description},price = #{part.price},buy_at = #{part.buyAt},expired_at = #{part.expiredAt} "+
                    "WHERE id = #{part.assetId}"
    )
    int updatePart(@Param("part") PartUpdateDto partUpdateDto);

    /**
     * 通过List[categoryId]获得List<Part>
     */
    @Select("<script>"+
            "SELECT * FROM fixed_asset WHERE status != #{status} AND asset_category_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}"+
            "</foreach>"+
            "</script>"
    )
    List<Part> findPartsByCategoryIds(@Param("list")List list,@Param("status")int status);

    /**
     * 通过配件id列表查归属记录
     */
    @Select("<script>"+
            "SELECT * FROM fixed_asset_track WHERE fixed_asset_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}"+
            "</foreach>"+
            "ORDER BY id ASC LIMIT #{p.offset}, #{p.limit}"+
            "</script>"
    )
    List<Track> findTrackByAssetId(@Param("list")List list, @Param("p")Pagination pagination);

    /**
     * 验证配件分类是否存在
     */
    @Select("SELECT * FROM asset_category WHERE id =#{id} LIMIT 1")
    String findPartCategoryExist(@Param("id")int id);

    /**
     * 验证分类名是否重复
     */
    @Select("SELECT COUNT(*) FROM asset_category WHERE name = #{name} AND flag = "+Part.CATEGORY_ID_PART+" AND status != "+Part.STATUS_DELETE+"")
    int findPartCategoryNameExist(@Param("name")String name);

    /**
     * 新增配件分类
     */
    @Insert("INSERT INTO asset_category (parent_id,name,description,flag) VALUES (#{part.parentId},#{part.name},#{part.description},#{flag})")
    int createPartCategory(@Param("part") PartCategoryCreateDto partCategoryCreateDto, @Param("flag")int flag);

    /**
     * 编辑配件分类
     */
    @Update("UPDATE asset_category SET name = #{part.name},description = #{part.description},parent_id=#{part.parentId} WHERE id = #{part.categoryId}")
    int updatePartCategory(@Param("part") PartCategoryUpdateDto partCategoryUpdateDto);

    /**
     * 给资产绑定设备id
     */
    @Update("UPDATE fixed_asset SET device_id = #{deviceId},status = #{status},start_at = #{startTime} WHERE id = #{assetId}")
    int updatePartBind(@Param("assetId")int assetId, @Param("deviceId")int deviceId, @Param("status")int status, @Param("startTime")Date startTime);

    /**
     * 添加设备归属绑定
     */
    @Insert("INSERT INTO asset_affiliate  (asset_id , asset_affiliate) VALUES (#{assetId},#{assetAffiliate})")
    int createAffiliate(@Param("assetId")int assetId,@Param("assetAffiliate")int assetAffiliate);

    /**
     * 添加归属记录
     */
    @Insert("INSERT INTO fixed_asset_track (fixed_asset_id,user_id,device_id,binding_start_at,status) VALUES (#{assetId},#{userId},#{deviceId},#{bindAt},#{status})")
    int createBindTrack(@Param("assetId")int assetId,@Param("userId")int userId,@Param("deviceId")int deviceId,@Param("bindAt")Date bindAt,@Param("status")int status);

    /**
     * 验证记录当中当前配件最新一条是否状态是否为归属状态
     */
    @Select("SELECT status FROM fixed_asset_track WHERE fixed_asset_id = #{id} ORDER BY id DESC LIMIT 1")
    String findTrackStatus(@Param("id")int id);

    /**
     * 通过id更新归属
     */
    @Update("UPDATE asset_affiliate SET status = #{status} WHERE asset_id = #{id}")
    int updatePartAffiliate(@Param("status")int status,@Param("id")int id);

    /**
     * 验证设备id
     */
    @Select("SELECT COUNT(*) FROM fixed_asset WHERE status != #{status} AND id = #{id}")
    int findDeviceExist(@Param("status")int status,@Param("id")int id);

    /**
     * 将最新的绑定记录状态改为删除状态
     */
    @Update("UPDATE fixed_asset_track SET status = #{status},binding_end_at = #{bindEndAt} WHERE fixed_asset_id = #{id} ORDER BY id DESC LIMIT 1")
    int updateTrackStatus(@Param("id")int id,@Param("status")int status,@Param("bindEndAt")Date bindEndAt);

    /**
     * 删除故障表信息
     */
    @Update("UPDATE malfunction SET status = #{status} WHERE fixed_asset_id = #{assetId} ")
    int updateMalfunction(@Param("status")int status,@Param("assetId")int assetId);

    /**
     *更改资产表状态
     */
    @Update("UPDATE fixed_asset SET malfunction_status = #{status}  WHERE id = #{id}")
    int updateAssetStatus(@Param("id")int id,@Param("status")int status);

    /**
     *更改解除归属状态
     */
    @Update("UPDATE fixed_asset SET status = #{status} , device_id = #{deviceId} WHERE id = #{id}")
    int updateAffiliateStatus(@Param("id")int id,@Param("deviceId")int deviceId,@Param("status")int status);

    /**
     * 更改归属表状态
     */
    @Update("UPDATE asset_affiliate SET status = #{status} WHERE asset_id = #{assetId}")
    int updateAssetAffiliate(@Param("status")int status,@Param("assetId")int assetAffiliate);

    /**
     * 删除验证是否有此资产
     */
    @Select("SELECT COUNT(*) FROM fixed_asset WHERE id = #{id} AND flag = "+Part.CATEGORY_ID_PART+" AND status != "+Part.STATUS_DELETE+"")
    int findDeletePartExist(@Param("id")int id);

    /**
     * 添加故障报修
     */
    @Insert("INSERT INTO malfunction (fixed_asset_id,malfunction_description,status)VALUES(#{assetId},#{description},#{status}) ")
    int insertMalfunction(@Param("assetId")int assetId,@Param("description")String description,@Param("status")int status);

    /**
     * 验证资产id是否存在
     */
    @Select("SELECT id FROM fixed_asset WHERE id = #{id} AND status != #{status}")
    String findAssetExist(@Param("id")int id,@Param("status")int status);

    /**
     * 查询资产故障状态
     */
    @Select("SELECT malfunction_status FROM fixed_asset WHERE id = #{id}")
    int findAssetStatus(@Param("id")int id);

    /**
     * 通过资产id查找Part
     */
    @Select("<script>"+
            "SELECT * FROM fixed_asset WHERE status != #{status} AND id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach> "+
            "</script>"
    )
    List<Part> findPartById(@Param("list")Set set,@Param("status")int status);

    /**
     * 删除分类
     */
    @Update("<script>"+
            "UPDATE asset_category SET status=#{status} WHERE id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach> "+
            "</script>"
    )
    int deletePartCategory(@Param("list")List list,@Param("status")int status);

    /**
     * 查看是否绑定资产
     */
    @Select("<script>"+
            "SELECT COUNT(*) FROM fixed_asset WHERE status != "+Part.STATUS_DELETE+" AND flag = "+Part.CATEGORY_ID_PART+" AND asset_category_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach> "+
            "</script>"
    )
    int selectPartBinding(@Param("list")List list);

    /**
     * 模糊查询配件分类
     */
    @Select("SELECT id FROM asset_category WHERE status != "+ Category.STATUS_DELETE +
            " AND flag = "+Part.CATEGORY_ID_PART+" AND name LIKE CONCAT('%',#{title},'%')")
    List<Integer> findPartCategoryVague(@Param("title")String title);

    /**
     * 通过List[categoryId]获得List<Part>
     */
    @Select("<script>" +
            "SELECT * FROM fixed_asset WHERE status != "+ Category.STATUS_DELETE +" AND flag = "+Part.CATEGORY_ID_PART+" AND asset_category_id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Part> findCategoryList(@Param("list") List list);

}
