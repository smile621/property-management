package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.ConsumableCreateDto;
import com.smile.eam.dto.ConsumableEditDto;
import com.smile.eam.dto.ConsumableIntWareHouseDto;
import com.smile.eam.dto.ConsumableOutWareHouseDto;
import com.smile.eam.entity.Consumable;
import com.smile.eam.entity.ConsumableTrack;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

public interface ConsumableMapper {

    /**
     * 耗材记录清单列表查询
     */
    @Select(" <script> " +
            " SELECT * FROM `consumable` WHERE status !=#{status} AND category_id in" +
            " <foreach item='item' collection='ids' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " ORDER BY id DESC" +
            " </script>")
    List<Consumable> findAllByIds(@Param("status") int status, @Param("ids") Set<Integer> ids);

    @Select(" SELECT * FROM `consumable` WHERE status !=" + Consumable.STATUS_DELETE + " ORDER BY id DESC LIMIT #{pagination.offset},#{pagination.limit}")
    List<Consumable> findAll(@Param("pagination") Pagination pagination);

    /**
     * 耗材新增  将主键返回后插入consumable_track表
     */
    @Insert(" INSERT INTO `consumable` (name,description,unit_price,vendor_id,specification,category_id,created_at,updated_at) " +
            " VALUES (#{consumable.name},#{consumable.description},#{consumable.unitPrice},#{consumable.vendorId}" +
            " ,#{consumable.specification},#{consumable.categoryId},#{time},#{time})")
    int insertConsumable(@Param("consumable") ConsumableCreateDto consumableCreateDto, @Param("time") String time);

    @Insert(" INSERT INTO `consumable_track` (consumable_id,created_at,updated_at) VALUES (#{id},#{time},#{time} )")
    int insertTrack(@Param("id") int id, @Param("time") String time);

    /**
     * 耗材删除 100
     */
    @Update(" UPDATE `consumable` SET status = " + Consumable.STATUS_DELETE + ",updated_at = #{date} WHERE id = #{id}")
    int deleteConsumable(@Param("id") int id, @Param("date") String date);

    @Update(" UPDATE `consumable_track` SET status = " + Consumable.STATUS_DELETE + ",updated_at = #{date} WHERE consumable_id = #{id} and status !=" + Consumable.STATUS_DELETE)
    int deleteConsumableTrack(@Param("id") int id, @Param("date") String date);

    /**
     * 耗材编辑
     */
    @Update(" UPDATE `consumable` SET name = #{consumable.name},specification = #{consumable.specification}," +
            " description=#{consumable.description},category_id=#{consumable.categoryId},vendor_id=#{consumable.vendorId}," +
            " unit_price = #{consumable.unitPrice},updated_at=#{updatedAt} WHERE id = #{consumable.id} AND status !=" + Consumable.STATUS_DELETE)
    int editConsumable(@Param("consumable") ConsumableEditDto consumableEditDto, @Param("updatedAt") String updatedAt);

    /**
     * 耗材模糊搜索   根据分类和厂商名称  " <if test = 'category_id !=null'> AND category_id = #{categoryId} </if>" +
     */
    @Select(" <script> " +
            " SELECT * FROM `consumable` WHERE" +
            " status !=" + Consumable.STATUS_DELETE + " AND name LIKE CONCAT ('%',#{name},'%') " +
            " OR vendor_id IN " +
            " <foreach item='item' collection='vendorIds' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " ORDER BY id DESC" +
            " LIMIT #{pagination.offset},#{pagination.limit}" +
            " </script> ")
    List<Consumable> searchByCategoryOrVendorId(@Param("pagination") Pagination pagination,
                                                @Param("name") String name, @Param("vendorIds") Set vendorId);

    @Select(" SELECT * FROM `consumable` WHERE status !=" + Consumable.STATUS_DELETE + " AND name LIKE CONCAT ('%',#{name},'%') " +
            " ORDER BY id DESC LIMIT #{pagination.offset},#{pagination.limit}")
    List<Consumable> searchConsumablesByName(@Param("name") String name, @Param("pagination") Pagination pagination);

    /**
     * 耗材入库  没有就插入，有则先将有的那条的状态值修改再插入新的数据
     */
    @Insert(" INSERT INTO `consumable_track` (consumable_id,in_warehouse_user_id,in_number,in_warehouse_at," +
            " int_description,buy_at,status) VALUES (#{consumable.consumableId},#{consumable.inWareHouseUserId}" +
            " ,#{consumable.inNumber},#{consumable.inWareHouseAt},#{consumable.intDescription}," +
            " #{consumable.buyAt}," + Consumable.STATUS_INT + ") ")
    void insertConsumableTrack(@Param("consumable") ConsumableIntWareHouseDto dto);

    /**
     * 耗材入库/领用记录后将耗材表对应的数量增加或减少
     */
    @Select(" SELECT * FROM `consumable` WHERE id = #{id} AND status != " + Consumable.STATUS_DELETE)
    Consumable findConsumableById(@Param("id") int id);

    @Update(" UPDATE `consumable` SET total = #{total} WHERE id = #{id} AND status!=" + Consumable.STATUS_DELETE)
    void updateNumber(@Param("total") int total, @Param("id") int id);

    /**
     * 耗材领用
     */
    @Insert(" INSERT INTO `consumable_track` (consumable_id,recipient_id,out_warehouse_user_id," +
            " out_number,out_description,out_warehouse_at,status) VALUES (#{consumable.consumableId}," +
            " #{consumable.recipientId},#{consumable.outWareHouseUserId},#{consumable.outNumber},#{consumable.outDescription}," +
            " #{consumable.outWareHouseAt}," + ConsumableTrack.STATUS_OUT + ") ")
    int insertOutTrack(@Param("consumable") ConsumableOutWareHouseDto dto);

    /**
     * 耗材分类列表
     */
    @Select(" SELECT * FROM `consumable` WHERE status != " + Consumable.STATUS_DELETE)
    List<Consumable> findAllConsumable();

    /**
     * 耗材领用记录履历
     */
    @Select(" SELECT * FROM `consumable_track` WHERE status !=" + ConsumableTrack.STATUS_DELETE + " ORDER BY id DESC LIMIT #{pagination.offset},#{pagination.limit}")
    List<ConsumableTrack> findAllTrack(@Param("pagination") Pagination pagination);

    @Select(" SELECT * FROM `consumable_track` WHERE id=#{id} AND status !=" + ConsumableTrack.STATUS_DELETE)
    List<ConsumableTrack> findOneTrack(@Param("id") int id);

    /**
     * 耗材领用入库记录模糊搜索     根据耗材名称
     */
    @Select(" <script> " +
            " SELECT * FROM `consumable_track` WHERE status !=" + ConsumableTrack.STATUS_DELETE +
            " AND consumable_id in " +
            " <foreach item ='id' collection='ids' separator=',' open='(' close=')' >" +
            " #{id} " +
            " </foreach>" +
            " </script>")
    List<ConsumableTrack> searchConsumableTrack(@Param("ids") Set<Integer> ids);

    /**
     * 根据耗材名称模糊查找id
     */
    @Select(" SELECT * FROM `consumable` WHERE status !=" + Consumable.STATUS_DELETE + " AND name LIKE CONCAT ('%',#{name},'%') " +
            " ORDER BY id DESC LIMIT #{pagination.offset},#{pagination.limit}")
    List<Consumable> findConsumablesByName(@Param("name") String name, @Param("pagination") Pagination pagination);


}

