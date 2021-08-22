package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Issue;
import com.smile.eam.mapper.IssueMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IssueService {
    @Resource
    IssueMapper issueMapper;
    /**
     * 获取发行方式
     * */
    public List<Issue> getIssueIdList(Pagination pagination){
        //获取发行方式list
        return issueMapper.getIssueIdList(pagination);
    }

    /**
     * 获取总发行条数
     * */
    public int getIssueIdCount(){
      return   issueMapper.getIssueIdCount();
    }
}
