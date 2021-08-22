package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.service.CheckService;
import com.smile.eam.dto.CheckCreateDto;
import com.smile.eam.dto.CheckListReturnDto;
import com.smile.eam.dto.CheckUpdateDto;
import com.smile.eam.dto.checkDetailAllReturn;
import com.smile.eam.dto.CheckDetailReturnDto;
import com.smile.eam.entity.AdminUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@Api(tags = "盘点相关接口")
@RestController
@CrossOrigin
public class CheckController {

    @Resource
    CheckService checkService;

    /**
     * 新建盘点项目API
     */
    @ApiOperation("新建盘点项目API")
    @PostMapping("/api/check/createCheck")
    public JsonResult<String> createCheck(CheckCreateDto checkCreateDto) {
        checkService.createCheck(checkCreateDto);
        return new JsonResult<>();
    }

    /**
     * 获取盘点项目列表API
     */
    @ApiOperation("获取盘点项目列表API")
    @GetMapping("/api/check/getCheckList")
    public JsonResult<List<CheckListReturnDto>> getCheckList(Pagination pagination, String title) throws ParseException {
        JsonResult<List<CheckListReturnDto>> jsonResult = new JsonResult<>();
        List<CheckListReturnDto> checkListReturnDto = checkService.getCheckList(pagination, title);
        jsonResult.setData(checkListReturnDto);
        return jsonResult;
    }

    /**
     * 获取盘点项目详情列表API
     */
    @GetMapping("/api/check/getCheckDetail")
    public JsonResult<checkDetailAllReturn> getCheckDetail(int checkId, Pagination pagination) {
        JsonResult<checkDetailAllReturn> jsonResult = new JsonResult<>();
        jsonResult.setData(checkService.getCheckDetail(checkId, pagination));
        return jsonResult;
    }

    /**
     * 更新盘点状态API
     */
    @ApiOperation("更新盘点状态API")
    @PostMapping("/api/check/updateCheck")
    public JsonResult<String> updateCheck(CheckUpdateDto checkUpdateDto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (checkService.updateCheckStatus(checkUpdateDto.getAssetId(), checkUpdateDto.getCheckId(), checkUpdateDto.getDescription(), checkUpdateDto.getStatus()) != 1) {
            jsonResult.setData("ERROR");
        }
        return jsonResult;
    }

    /**
     * 拉取盘点记录列表API
     */
    @ApiOperation("拉取盘点记录列表API")
    @GetMapping("/api/check/getCheckTrack")
    public JsonResult<List<CheckDetailReturnDto>> getCheckTrack() {
        JsonResult<List<CheckDetailReturnDto>> jsonResult = new JsonResult<>();
        jsonResult.setData(checkService.getCheckTrack());
        return jsonResult;
    }

    /**
     * 拉取盘点记录列表API
     */
    @ApiOperation("盘点完成API")
    @PostMapping("/api/check/performCheck")
    public JsonResult<String> performCheck(int checkId) {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (checkService.performCheck(checkId) != 1) {
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     * 拉取盘点记录列表API
     */
    @ApiOperation("盘点中止API")
    @PostMapping("/api/check/discontinueCheck")
    public JsonResult<String> discontinueCheck(int checkId) {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (checkService.discontinueCheck(checkId) != 1) {
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     * 获取所有的用户列表
     */
    @GetMapping("/api/check/getUserList")
    public JsonResult<List<AdminUser>> getUserList() {
        JsonResult<List<AdminUser>> jsonResult = new JsonResult<>();
        jsonResult.setData(checkService.getUserList());
        return jsonResult;
    }

}
