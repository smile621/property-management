package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.AssetCapital;
import com.smile.eam.entity.Depreciation;
import com.smile.eam.entity.DepreciationDetailItem;
import com.smile.eam.entity.DepreciationItem;
import com.smile.eam.dto.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface DepreciationMapper {
    /**
     * 验证购入途径是否存在
     */
    //验证购入途径是否存在..
    @Select("SELECT * FROM depreciation WHERE id =#{id] LIMIT 1")
    String findDepreciationExist(@Param("id") int id);

    /**
     * list根据折旧名称ID获取折旧规则名称
     */
    @Select(" <script>" +
            " SELECT * FROM `depreciation` WHERE status != " + Depreciation.STATUS_DELETE + " AND id IN" +
            " <foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " </script>"
    )
    List<Depreciation> findDepreciateByIds(@Param("list") Set<Integer> list);

    /**
     *通过设备id获得折旧模板
     */
    @Select(" SELECT * FROM `depreciation_template` WHERE status !=" + Depreciation.STATUS_DELETE + " AND id= #{id}")
    DepreciationDetailItem findDepreciateTemplateById(@Param("id") int id);

    /**
     *通过分类id获取折旧项目
     */
    @Select(" SELECT name FROM `depreciation` WHERE status !=" + Depreciation.STATUS_DELETE + " AND asset_category_id= #{id}")
    Depreciation findDepreciateByCategoryId(@Param("id") int id);

    /**
     *插入默认折旧模板
     */
    @Insert(" insert into `depreciation_template` (depreciation_id,period,measure,rate) values (#{id},1,3,1)")
    void insertDepreciationTemplate(@Param("id") int id);


    /**
     * 编辑设备的折旧规则
     */
    @Update(" Update `depreciation_template` set depreciation_id=#{depreciateId} where id=#{id}")
    void updateDepreciate(@Param("depreciateId") int status, @Param("id") int id);


    /**
     * 获取折旧规则列表
     */
    @Select("select * from depreciation where status != "+Depreciation.STATUS_DELETE+"")
    List<Depreciation> getList();

    /**
     * 折旧规则模糊搜索
     */
    @Select("<script> " +
            " select * from depreciation " +
            " <where> " +
            " <if test='depreciation.name != null'> and name like concat('%',#{depreciation.name},'%') </if> " +
            " and status!="+Depreciation.STATUS_DELETE+" " +
            " </where>" +
            " order by id desc " +
            " limit #{pagination.offset},#{pagination.limit} " +
            " </script>")
    List<DepreciationResponseDto> getSearchList(@Param("depreciation") DepreciationSearchDto depreciationSearchDto, @Param("pagination") Pagination pagination);

    /**
     * 获取模糊搜索折旧规则总数
     */
    @Select("<script>" +
            "select count(*) from depreciation " +
            " <where>" +
            " <if test='depreciation.name != null'> and name like concat('%',#{depreciation.name},'%') </if>" +
            " and status != "+Depreciation.STATUS_DELETE+" " +
            " </where>" +
            "</script>")
    long searchDepreciationCount(@Param("depreciation") DepreciationSearchDto depreciationSearchDto);

    /**
     * 新增折旧项目  depreciation
     */
    @Insert("insert into depreciation set name=#{depreciation.name},description=#{depreciation.description}")
    void create(@Param("depreciation") DepreciationCreateDto depreciationCreateDto);

    /**
     * 根据折旧项目名查找折旧项目id
     */
    @Select("select id from depreciation where name=#{name} and status!="+Depreciation.STATUS_DELETE+" ")
    String findIdByName(@Param("name") String name);

    /**
     * 删除折旧规则
     */
    @Update("update depreciation set status = #{status} where id=#{id}")
    int delete(@Param("id") int id, @Param("status") int status);

    /**
     * 批量删除
     */
    @Update("<script>" +
            " update depreciation set status="+Depreciation.STATUS_DELETE+" " +
            " where id in " +
            " <foreach collection='ids' item='id' open='('  separator = ',' close=')'  >" +
            " #{id} " +
            "</foreach>" +
            " </script> ")
    int deleteQuery(@Param("ids") List<Integer> ids);

    /**
     * 查看详情
     */
    @Select("select * from depreciation where id=#{id} and status!="+Depreciation.STATUS_DELETE+"")
    DepreciationCreateDto detail(@Param("id") int id);

    /**
     * 编辑depreciation
     */
    @Update("update depreciation set name=#{depreciationUpdateDto.name},description=#{depreciationUpdateDto.description}  where id=#{depreciationUpdateDto.id}")
    int update(@Param("depreciationUpdateDto") DepreciationUpdateDto depreciationUpdateDto);

    /**
     * 批量插入deprecia——template
     */
    @Insert("<script>" +
            " insert into depreciation_template(depreciation_id,period,measure,rate)" +
            " values" +
            " <foreach collection='list' item='i' separator=',' > " +
            " (#{depreciationId},#{i.period},#{i.measure},#{i.rate})" +
            " </foreach>" +
            " </script>")
    void createTemplate(@Param("list") List<DepreciationTemplateCreateDto> list, @Param("depreciationId") int depreciationId);

    /**
     * 删除depreciation_template
     */
    @Update("update depreciation_template set status="+Depreciation.STATUS_DELETE+" where depreciation_id=#{id}")
    int deleteTemplate(@Param("id") int depreciationId);

    /**
     *通过折旧id查看折旧表
     */
    @Select("SELECT * FROM depreciation WHERE id = #{id} AND status !="+ Depreciation.STATUS_DELETE+"")
    DepreciationItem findDepreciation(@Param("id") int id);

    /**
     *通过折旧id查看折旧表
     */
    @Select("SELECT * FROM depreciation_template WHERE depreciation_id = #{id} AND status != "+Depreciation.STATUS_DELETE+"")
    List<DepreciationDetailItem> findDepreciationDetail(@Param("id") int id);

    /**
     *查看所有的折旧模板
     */
    @Select("SELECT * FROM depreciation_template WHERE status != #{status}")
    List<DepreciationDetailItem> findTemplateAll(@Param("status") int status);

    /**
     *验证分类id在折旧表中是否存在
     */
    @Select("SELECT COUNT(*) FROM depreciation WHERE asset_category_id = #{categoryId} AND status != "+Depreciation.STATUS_DELETE+"")
    int findDepreciationCategoryIdExist(@Param("categoryId") int categoryId);

    /**
     *绑定折旧规则和分类
     */
    @Update("UPDATE depreciation SET asset_category_id = #{categoryId} WHERE id = #{depreciationId} AND status != "+Depreciation.STATUS_DELETE+"")
    int updateDepreciationCategory(@Param("categoryId") int categoryId, @Param("depreciationId") int depreciationId);

    /**
     *资产和折旧模板绑定
     */
    @Update("UPDATE fixed_asset SET depreciate_id = #{depreciationTemplateId},out_price = #{outPrice} WHERE id = #{assetId} AND status != "+Depreciation.STATUS_DELETE+"")
    int updateDepreciationAsset(@Param("assetId") int assetId, @Param("depreciationTemplateId") int depreciationTemplateId, @Param("outPrice") BigDecimal outPrice);

    /**
     * 通过分类Id查出折旧规则
     */
    @Select("SELECT * FROM depreciation WHERE asset_category_id = #{categoryId} AND status != "+Depreciation.STATUS_DELETE+"")
    DepreciationItem findDepreciationListByCategoryId(@Param("categoryId") int categoryId);

    /**
     * 通过分类Id查出折旧规则
     */
    @Insert("INSERT INTO depreciation SET name = #{name} , description = #{description} , asset_category_id = #{categoryId}")
    int createDepreciation(@Param("name") String name, @Param("description") String description, @Param("categoryId") int categoryId);

    /**
     *创建折旧模板折旧详情
     */
    @Insert("INSERT INTO depreciation_template SET templateName = #{name},depreciation_id = #{depreciationId},period = #{period},measure = #{measure},rate = #{rate}")
    int createDepreciationDetail(@Param("name") String name, @Param("depreciationId") int depreciationId, @Param("period") int period, @Param("measure") int measure, @Param("rate") String rate);

    /**
     *删除折旧模板折旧详情
     */
    @Update("UPDATE depreciation_template SET status = #{status} WHERE id = #{depreciationTemplateId}")
    int updateDepreciationTemplate(@Param("status") int status, @Param("depreciationTemplateId") int depreciationTemplateId);

    /**
     *通过折旧模板id查找折旧模板
     */
    @Select("SELECT * FROM depreciation_template WHERE id = #{id} AND status != "+Depreciation.STATUS_DELETE+"")
    DepreciationDetailItem findTemplateById(@Param("id") int templateId);

    /**
     *查看资产本金
     */
    @Select("SELECT * FROM fixed_asset WHERE id = #{id} AND status != "+Depreciation.STATUS_DELETE+"")
    AssetCapital findAssetCapital(@Param("id") int id);
}

