package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.BuyRouterCreateDto;
import com.smile.eam.dto.BuyRouterSearchDto;
import com.smile.eam.dto.BuyRouterUpdateDto;
import com.smile.eam.entity.BuyRouter;
import com.smile.eam.entity.Part;
import com.smile.eam.entity.Vendor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

public interface BuyRouterMapper {

    /**
     * 通过buyRouterId查询整个buyRouter
     */
    @Select("<script>" +
            "SELECT * FROM buy_router WHERE status != 100 and id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<BuyRouter> findBuyRouterById(@Param("list") Set list);

    /**
     * 模糊查询购入途径名
     */
    @Select("SELECT id FROM buy_router WHERE name like CONCAT('%',#{title},'%')")
    List<Integer> findBuyRouterIdVague(@Param("title") String title);

    /**
     * 通过List[buyRouterId]获得List<Part>
     */
    @Select("<script>" +
            "SELECT * FROM fixed_asset WHERE status != "+ Vendor.STATUS_DELETE +" AND flag = "+ Part.CATEGORY_ID_PART+" AND buy_router_id IN" +
            "<foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Part> findPartsByBuyRouterIds(@Param("list") List list);

    /**
     * 新建购入途径
     */
    @Insert("insert into buy_router (name,description) values (#{buyRouterCreateDto.name},#{buyRouterCreateDto.description})")
    int createBuyRouter(@Param("buyRouterCreateDto") BuyRouterCreateDto buyRouterCreateDto);

    /**
     * 通过购入途径名字查找ID 判断名字是否已存在
     */
    @Select("select id from buy_router where name=#{name} and status!=" + BuyRouter.STATUS_DELETE)
    String findIdByName(@Param("name") String name);

    /**
     * 删除购入途径
     */
    @Update("update buy_router set status=" + BuyRouter.STATUS_DELETE + " where id=#{id}")
    void deleteBuyRouter(@Param("id") int id);

    /**
     * 清空所有设备中的已删除购买途径
     */
    @Update("update fixed_asset set buy_router_id=" + BuyRouter.STATUS_NORMAL + " where buy_router_id=#{id} ")
    void deleteBuyRouterBind(@Param("id") int id);

    /**
     * 编辑购买途径
     */
    @Update("update buy_router set name=#{buyRouterUpdateDto.name},description=#{buyRouterUpdateDto.description} " +
            " where id=#{buyRouterUpdateDto.id}")
    int updateBuyRouter(@Param("buyRouterUpdateDto") BuyRouterUpdateDto buyRouterUpdateDto);

    /**
     * 通过Id查找购入途径详情
     */
    @Select("select * from buy_router where id=#{id} and status != " + BuyRouter.STATUS_DELETE)
    BuyRouter findDetailById(@Param("id") int id);

    /**
     * 批量删除
     */
    @Update("<script>" +
            " update buy_router set status=" + BuyRouter.STATUS_DELETE +
            " where id in " +
            " <foreach collection = 'ids' item = 'id' open='('  separator = ','  close=')' >" +
            " #{id} " +
            " </foreach>" +
            " </script>")
    void deleteBuyRouters(@Param("ids") List<Integer> ids);

    /**
     * 批量删除对应设备的购买途径
     */
    @Update("<script>" +
            " update fixed_asset set buy_router_id= " + BuyRouter.STATUS_NORMAL +
            " where buy_router_id in " +
            " <foreach collection = 'ids' item = 'id' open='('  separator = ','  close=')' >" +
            " #{id} " +
            " </foreach>" +
            " </script>")
    void deleteBuyRoutersBind(@Param("ids") List<Integer> ids);

    /**
     * 通过buyRouterId查询整个buyRouter (map列表)
     */
    @Select(" <script>" +
            " SELECT * FROM buy_router WHERE status != " + BuyRouter.STATUS_DELETE + " AND id IN" +
            " <foreach item='item' collection='list' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    List<BuyRouter> findBuyRouterListById(@Param("list") Set<Integer> list);

    /**
     * 模糊搜索
     */
    @Select("select * from buy_router where concat (id,name,description) like '%' #{dto.name} '%' " +
            "and status!=" + BuyRouter.STATUS_DELETE + " order by id desc limit #{pagination.offset},#{pagination.limit} ")
    List<BuyRouter> search(@Param("dto") BuyRouterSearchDto dto, @Param("pagination") Pagination pagination);

    /**
     * 模糊搜索条数
     */
    @Select("select count(*) from buy_router where concat (id,name,description) like '%' #{dto.name} '%' " +
            "and status!=" + BuyRouter.STATUS_DELETE + " order by id desc  ")
    long searchCount(@Param("dto") BuyRouterSearchDto dto);

}
