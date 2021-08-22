package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.dto.*;
import com.smile.eam.entity.Malfunction;
import com.smile.eam.service.MalfunctionService;
import com.smile.eam.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "故障相关接口")
@RestController
@CrossOrigin
public class MalfunctionController {
    @Resource
    MalfunctionService malfunctionService;
    @Resource
    TodoService todoService;
    @ApiOperation("获取故障列表")
    @GetMapping("/api/malfunction/list")
    public JsonResult<GetMalfunctionListDto> getMalfunctionList(Pagination pagination){
        //获取总数据条数
        int total = malfunctionService.MalfunctionCount();
        pagination.setTotal(total);
        //获取故障列表
        List<MalfunctionResponse> malfunctionList = malfunctionService.malfunctionList(pagination);
        GetMalfunctionListDto data = GetMalfunctionListDto.builder()
                .malfunctionList(malfunctionList)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    @ApiOperation("处理故障api")
    @PostMapping("/api/malfunction/dispose")
    public JsonResult<String> dispose(@Valid DisposeMalfunctionRequest disposeMalfunctionRequest, String token){
        //判断传入id是否存在
        Malfunction existMalfunction = malfunctionService.isExistMalfunction(disposeMalfunctionRequest.getId());
        if(existMalfunction==null){
            return new JsonResult<>("ERROR","该故障不存在");
        }
        //查看该故障是否为已完成状态
        Boolean m = malfunctionService.isFinished(disposeMalfunctionRequest);
        if(m){
            return new JsonResult<>("ERROR","该故障为已完成状态，不可修改");
        }
        //获取处理人id
        int solveId = todoService.findUserIdByToken(token);
        //处理故障
        int malfunction = malfunctionService.disposeMalfunction(disposeMalfunctionRequest, solveId);
        if(malfunction<1){
            return new JsonResult<>("ERROR","处理失败");
        }
        if(disposeMalfunctionRequest.getStatus()==6){
            //把报修的故障id  状态更改为 处理中 6
            malfunctionService.updateAssetStatus6(existMalfunction.getFixedAssetId());
        }
        if(disposeMalfunctionRequest.getStatus()==7){
            //把报修的故障id  状态更改为 正常 0
            malfunctionService.updateAssetStatus0(existMalfunction.getFixedAssetId());
        }
        return new JsonResult<>("处理成功");
    }

    @ApiOperation("删除故障列表")
    @PostMapping("/api/malfunction/delete")
    public JsonResult<String> deleteMalfunction(@Valid DeleteMalfunctionRequest deleteMalfunctionRequest){
        //判断传入id是否存在
        Malfunction existMalfunction = malfunctionService.isExistMalfunction(deleteMalfunctionRequest.getId());
        if(existMalfunction==null){
            return new JsonResult<>("ERROR","该故障不存在");
        }
        //删除故障列表
        Boolean m = malfunctionService.deleteMalfunction(deleteMalfunctionRequest.getId());
       if (!m){
           return new JsonResult<>("ERROR","删除失败");
       }
        return new JsonResult<>("删除成功");
    }

    /**
     * 故障模糊搜索
     * */
    @ApiOperation("故障模糊搜索")
    @PostMapping("/api/malfunction/findByName")
    public JsonResult<FindByNameDto> findByName(String name, Pagination pagination){
        FindByNameDto data = malfunctionService.findByName(name, pagination);
        return new JsonResult<>(data);
    }
}
