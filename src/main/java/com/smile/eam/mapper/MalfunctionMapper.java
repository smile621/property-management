package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.DeviceMalfunctionRequest;
import com.smile.eam.dto.DisposeMalfunctionRequest;
import com.smile.eam.entity.Asset;
import com.smile.eam.entity.Malfunction;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;


public interface MalfunctionMapper {
    /**
     * 获取故障列表总条数
     */
    @Select("select count(*) from `malfunction` where status !=" + Malfunction.STATUS_DELETE)
    int MalfunctionCount();

    /**
     * 获取故障列表
     */
    @Select("select * from `malfunction` where status !=" + Malfunction.STATUS_DELETE + " order by id desc limit #{pagination.offset}, #{pagination.limit}")
    List<Malfunction> malfunctionList(@Param("pagination") Pagination pagination);

    /**
     * 查看该故障是否已完成
     */
    @Select("select * from `malfunction` where id=#{id} and status =" + Malfunction.STATUS_FIGURE_OUT)
    Malfunction isFinished(DisposeMalfunctionRequest disposeMalfunctionRequest);

    /**
     * 判断传入id是否存在
     */
    @Select("select * from `malfunction` where id=#{id} and status !=" + Malfunction.STATUS_DELETE)
    Malfunction isExistMalfunction(@Param("id") Integer id);

    /**
     * 根据资产Id,获取故障列表
     */
    @Select("select * from `malfunction` where fixed_asset_id=#{id} and status !=" + Malfunction.STATUS_DELETE)
    List<Malfunction> malfunctionByAssetId(@Param("id") Integer id);

    /**
     * 处理故障
     */
    @Update("update  `malfunction` set `solve_id`=#{solve_id},`repair_description`=#{malfunction.repairDescription}," +
            "`status`=#{malfunction.status} where id=#{malfunction.id} and status != " + Malfunction.STATUS_DELETE)
    int disposeMalfunction(@Param("malfunction") DisposeMalfunctionRequest disposeMalfunctionRequest, @Param("solve_id") Integer solveId);

    /**
     * 删除故障
     */
    @Delete("update  `malfunction` set status=" + Malfunction.STATUS_DELETE + " where id=#{id} ")
    int deleteMalfunction(@Param("id") Integer id);

    /**
     * 对故障列表多个字段进行模糊查找
     */
    @Select("select * from `malfunction` where concat (malfunction_description,id,fixed_asset_id,created_at " +
            " ) like '%' #{name} '%' and status != " + Malfunction.STATUS_DELETE +
            " order by id desc limit #{pagination.offset}, #{pagination.limit}" )
    List<Malfunction> findByName(@Param("name") String name, @Param("pagination") Pagination pagination);

    /**
     * 删除设备时删除归属在该设备下的资产状态
     */
    @Update(" <script>" +
            " UPDATE `malfunction` SET status=" + Malfunction.STATUS_DELETE + " WHERE status=" + Malfunction.STATUS_MALFUNCTION + " AND fixed_asset_id IN " +
            " <foreach item ='item' collection='ids' index='index' separator=',' open='(' close=')' >" +
            " #{item}" +
            " </foreach>" +
            " </script>")
    void deleteAttributeMalfunction(@Param("ids") Set<Integer> ids);

    /**
     * 设备报告故障
     */
    @Insert(" INSERT INTO `malfunction` (fixed_asset_id,malfunction_description,status) VALUES (#{request.fixedAssetId},#{request.malfunctionDescription}," + Malfunction.STATUS_MALFUNCTION + ")")
    int sendDeviceMalfunction(@Param("request") DeviceMalfunctionRequest request);


    /**
     * 更新设备故障状态
     * */
    @Update(" UPDATE `fixed_asset` SET malfunction_status = " + Asset.STATUS_MALFUNCTION +
            " WHERE id = #{id} AND status != " + Malfunction.STATUS_DELETE)
    void updateMalStatus(@Param("id") int id);
}
