package com.smile.eam.mapper;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Issue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface IssueMapper {
    /**
     * 根据issueId 查找对应的发行实体类..
     * */
    @Select("<script>" +
            "select * from `issue` where status != 100 and id in " +
            "<foreach item='issue_id' collection='issueIdList' separator=',' open='(' close=')'>" +
            "#{issue_id}" +
            "</foreach>" +
            "</script>")
    List<Issue> findIssueNameById(@Param("issueIdList") Set<Integer> issueIdS);

    /**
     * 验证查询方式是否存在
     * */
    @Select("SELECT * FROM issue WHERE id = #{id} LIMIT 1")
    String findIssueById(@Param("id")int IssueById);

    /**
     * 获取发行方式list
     * */
    @Select("select * from `issue` where status != 100 order by id desc limit #{pagination.offset},#{pagination.limit}")
     List<Issue> getIssueIdList(@Param("pagination") Pagination pagination);

    /**
     * 获取总发行条数
     * */
    @Select("select count(*) from `issue` where status != "+ Issue.STATUS_DELETE)
     int getIssueIdCount();

    /**
     * 是否有版本发行方式
     * */
    @Select("select * from issue where id=#{id} and status != "+ Issue.STATUS_DELETE)
    Issue isIssue(@Param("id")Integer id);
}
