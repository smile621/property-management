package com.smile.eam.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StatusMapper {
    /**
     * 根据status 标识 获取具体的内容..
     * */
    @Select("select content from `status` where status =#{status} ")
    String getStatus(@Param("status") Integer status);

}
