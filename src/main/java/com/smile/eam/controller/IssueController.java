package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Issue;
import com.smile.eam.service.IssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "发行方式相关接口")
@RestController
@CrossOrigin
public class IssueController {
    @Resource
    IssueService issueService;
    /**
     * 获取发行方式
     * */
    @ApiOperation("发行方式列表API")
    @GetMapping("/api/issue/list")
    public JsonResult getIssueIdList(Pagination pagination){
        //获取发行方式list
        List<Issue> issueIdList = issueService.getIssueIdList(pagination);
        pagination.setTotal(issueService.getIssueIdCount());
        Map<String,Object> result = new HashMap<>();
        result.put("pagination",pagination);
        result.put("issueIdList",issueIdList);
        return new JsonResult(result);
    }
}
