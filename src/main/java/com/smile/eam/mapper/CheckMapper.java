package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.*;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CheckMapper {

    /**
     * 新建盘点项目
     */
    @Insert("INSERT INTO asset_check SET asset_category_id = #{categoryId},duty_id = #{userId},predict_at = #{predictAt},wait_count = #{waitCount},total_count = #{waitCount},lossPrice = #{lossPrice},findPrice = #{findPrice},status = "+ Check.STATUS_BEING_PROCESSED+"")
    int createCheck(@Param("userId")int userId,@Param("categoryId")int categoryId,@Param("predictAt")String predictAt,@Param("waitCount")int winCount,@Param("lossPrice")BigDecimal lossPrice,@Param("findPrice")BigDecimal findPrice);

    /**
     * 获得盘点项目表最新主键
     */
    @Select("SELECT id FROM asset_check WHERE status != "+Check.STATUS_DELETE+" ORDER BY id DESC LIMIT 1")
    int findCheckKey();

    /**
     * 插入盘点详情表
     */
    @Insert("INSERT INTO asset_check_detail SET check_id = #{checkId},asset_id=#{assetId},asset_category_id=#{assetCategoryId},status = #{status},asset_name = #{assetName},price = #{price}")
    int createCheckDetail(@Param("checkId")int checkId, @Param("assetId")int assetId, @Param("assetCategoryId")int assetCategoryId, @Param("status")int status, @Param("assetName")String assetName, @Param("price")BigDecimal price);

    /**
     * 通过List[categoryId]获得List<FixedAsset>
     */
    @Select("<script>"+
            "SELECT * FROM fixed_asset WHERE status != "+ FixedAsset.STATUS_DELETE+" AND asset_category_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<FixedAsset> findCategoryAsset(@Param("list")List list);

    /**
     * 通过status和categoryId验证进行中项目是否存在
     */
    @Select("SELECT COUNT(*) FROM asset_check WHERE asset_category_id = #{categoryId} AND status = "+Check.STATUS_BEING_PROCESSED+ " LIMIT 1")
    int findCheckExist(@Param("categoryId")int categoryId);

    /**
     * 通过userid查询盘点项目id
     */
    @Select("<script>"+
            "SELECT id FROM asset_check WHERE duty_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<Integer> findCheckIdByUserId(@Param("list")List userIdList);

    /**
     * 通过分类id查询盘点项目id
     */
    @Select("<script>"+
            "SELECT id FROM asset_check WHERE asset_category_id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<Integer> findCheckIdByCategoryId(@Param("list")List categoryIdList);

    /**
     * 通过盘点项目id找到对应的盘点项目
     */
    @Select("<script>"+
            "SELECT * FROM asset_check WHERE status != "+Check.STATUS_DELETE+" AND id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<Check> findCheckById(@Param("list")Set idSet);

    /**
     * 通过盘点项目id和状态找到对应的盘点项目
     */
    @Select("<script>"+
            "SELECT * FROM asset_check WHERE status = #{status} AND id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    List<Check> findCheckByIdStatus(@Param("list")Set idSet,@Param("status")int status);

    /**
     * 查出所有的盘点信息
     */
    @Select("SELECT * FROM asset_check WHERE status != "+Check.STATUS_DELETE+" ORDER BY id DESC LIMIT #{p.offset}, #{p.limit}")
    List<Check> findCheckAll(@Param("p") Pagination pagination);

    /**
     * 查出所有的盘点信息
     */
    @Select("SELECT * FROM asset_check WHERE id = #{id} AND status != "+Check.STATUS_DELETE+"")
    Check findCheckByIdOne(@Param("id")int id);

    /**
     * 查出所有的盘点信息
     */
    @Select("SELECT * FROM asset_check WHERE status = #{status}")
    List<Check> findCheckByStatus(@Param("status")int status);

    /**
     * 查出所有的盘点信息
     */
    @Select("SELECT * FROM asset_check WHERE status != "+Check.STATUS_DELETE+"")
    List<Check> findCheck();

    /**
     * 通过盘点项目id获取盘点信息
     */
    @Select("SELECT * FROM asset_check_detail WHERE check_id = #{checkId} AND status != "+Check.STATUS_DELETE+"")
    List<CheckDetail> findDetailByCheckId(@Param("checkId")int checkId);

    /**
     * 查看盘点项目id是否存在
     */
    @Select("SELECT COUNT(*) FROM asset_check WHERE id = #{id} AND status != "+Check.STATUS_DELETE+"")
    int findCheckIdExist(@Param("id")int id);

    /**
     * 查看盘点项目详情中是否存在盘点项目id对应的资产id
     */
    @Select("SELECT COUNT(*) FROM asset_check_detail WHERE check_id = #{checkId} AND asset_id = #{assetId} AND status != "+Check.STATUS_DELETE+"")
    int findCheckDetailIdExist(@Param("checkId")int checkId,@Param("assetId")int assetId);

    /**
     * 将盘点详情表中的资产状态改为以盘点
     */
    @Update("UPDATE asset_check_detail SET status = #{status} , description = #{description} , check_user_id = #{checkUserId} WHERE check_id = #{checkId} AND asset_id = #{assetId} ")
    int updateCheckDetail(@Param("checkId")int checkId,@Param("assetId")int assetId,@Param("description")String description,@Param("checkUserId")int checkUserId,@Param("status")int status);

    /**
     * 盘盈
     */
    @Update("UPDATE asset_check SET win_count = win_count+1 , wait_count = wait_count - 1 ,findPrice = findPrice + #{findPrice} WHERE id = #{checkId}")
    int updateCheckDetailCountUp(@Param("checkId")int checkId,@Param("findPrice")BigDecimal findPrice);

    /**
     * 盘亏
     */
    @Update("UPDATE asset_check SET loss_count = loss_count + 1 , wait_count = wait_count - 1 , lossPrice = lossPrice + #{lossPrice} WHERE id = #{checkId}")
    int updateCheckDetailCountDown(@Param("checkId")int checkId,@Param("lossPrice")BigDecimal bigDecimal);

    /**
     * 查找盘点详情价格
     */
    @Select("SELECT price FROM asset_check_detail WHERE check_id = #{checkId} AND asset_id = #{assetId} AND status = "+Check.STATUS_NONE_CHECK+"")
    BigDecimal findCheckDetailPrice(@Param("checkId")int checkId,@Param("assetId")int assetId);

    /**
     * 获取所有的盘点详情信息
     */
    @Select("SELECT * FROM asset_check_detail WHERE status != "+Check.STATUS_DELETE+" ORDER BY id DESC ")
    List<CheckDetail> findCheckDetailAll();

    /**
     * 查找盘点详情表中所有的资产id
     */
    @Select("SELECT check_id FROM asset_check_detail WHERE status != "+Check.STATUS_DELETE+"")
    Set<Integer> findCheckIdByDetail();

    /**
     * 查找盘点详情表中所有的资产id
     */
    @Select("<script>"+
            "SELECT duty_id FROM asset_check WHERE status != "+Check.STATUS_DELETE+" AND id IN"+
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item} "+
            "</foreach>"+
            "</script>"
    )
    Set<Integer> findDutyIdByCheckIds(@Param("list")Set list);

    /**
     * 更改Check表盘点项目状态
     */
    @Update("UPDATE asset_check_detail SET status = #{status} WHERE id = #{checkId}")
    int updateCheckStatus(@Param("checkId")int checkId,@Param("status")int status);

    /**
     * 查询所有的分类
     */
    @Select("SELECT * FROM asset_category WHERE status != "+ Category.STATUS_DELETE+"")
    List<Category> findCategoryAll();

    /**
     * 查看盘点详情中状态不为未盘点的值
     */
    @Select("SELECT asset_id FROM asset_check_detail WHERE check_id = #{checkId} AND status = #{status}")
    List<Integer> findCheckDetailNumber(@Param("checkId")int checkId,@Param("status")int status);

    /**
     * 查看盘点详情中全部的值
     */
    @Select("SELECT COUNT(*) FROM asset_check_detail WHERE check_id = #{checkId} AND status != "+Check.STATUS_DELETE+"")
    int findCheckDetailNumberAll(@Param("checkId")int checkId);

    /**
     * 将盘点表状态改为盘点完成
     */
    @Update("UPDATE asset_check SET status = #{status} , end_at = #{endAt}  WHERE id = #{id}")
    int updateCheckStatusOne(@Param("id")int id,@Param("endAt")Date endAt,@Param("status")int status);

    /**
     * 查看所有的用户
     */
    @Select("SELECT * FROM admin_user WHERE status != "+Check.STATUS_DELETE+"")
    List<AdminUser> findUserAll();
}
