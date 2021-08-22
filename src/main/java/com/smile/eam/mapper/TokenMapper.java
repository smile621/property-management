package com.smile.eam.mapper;

import com.smile.eam.entity.AdminUserToken;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TokenMapper {

    /**
     * 拦截token
     * */
    @Select(" SELECT * FROM `admin_user_token` WHERE token = #{token} LIMIT 1")
    AdminUserToken findOneByUserId(@Param("token") String token);

    /**
     *  随机产生一个token值，并添加进数据库
     * */
    @Insert("insert into `admin_user_token` (`user_id`,`token`) values (#{user_id},#{token})")
    void addToken(@Param("user_id")int userId,@Param("token")String token);

    /**
     * 根据token获取userId
     * */
    @Select("select * from `admin_user_token` where `token`=#{token}")
    AdminUserToken findUserIdByToken(@Param("token")String token);

    /**
     * 删除对应的token数据库条目
     * */
    @Delete("delete from `admin_user_token` where token=#{token}")
    int deleteToken(@Param("token")String token);

    /**
     *  删除对应用户的所有token
     */
    @Delete("delete from admin_user_token  where user_id=#{id}")
    int deleteAllToken(@Param("id") int id);

    /**
     *  批量删除用户的所有token
     */
    @Delete("<script>" +
            " delete from admin_user_token where user_id in" +
            " <foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            " #{id}" +
            " </foreach>" +
            "</script>")
    int deleteAllTokens(@Param("ids") List<Integer> ids);
}
