package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.SoftwareTrackRequest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import static com.smile.eam.entity.FixedAssetTrack.STATUS_DELETE;

public interface FixedAssetTrackMapper {
    /**
     * 添加软件绑定记录
     */
    @Insert("insert into fixed_asset_track ( fixed_asset_id, device_id,lend_description) values (#{fixed_asset_id},#{device_id},'绑定') ")
    void addSoftwareBind(@Param("fixed_asset_id")Integer FixedAssetId,@Param("device_id")Integer deviceId);

    /**
     * 添加软件取消绑定到  归属表
     * */
    @Insert("insert into fixed_asset_track ( fixed_asset_id, device_id,lend_description) values (#{fixed_asset_id},#{device_id},'取消绑定') ")
    void addSoftwareCancelBind(@Param("fixed_asset_id")Integer FixedAssetId,@Param("device_id")Integer deviceId);

    /**
     * 传id 获取软件绑定记录表
     * */
    @Select("select * from fixed_asset_track where fixed_asset_id = #{id} and status != "+STATUS_DELETE +
            " order by id desc limit #{pagination.offset},#{pagination.limit} ")
    List<SoftwareTrackRequest>  getTrackList(@Param("id") Integer id, @Param("pagination") Pagination pagination);

    /**
     * 获取归属列表总条数
     * */
    @Select("select count(*) from fixed_asset_track where fixed_asset_id = #{id} and status != "+STATUS_DELETE)
    int getTrackCount(@Param("id") Integer id);
}
