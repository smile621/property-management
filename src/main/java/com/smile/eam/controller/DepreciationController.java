package com.smile.eam.controller;


import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Depreciation;
import com.smile.eam.entity.DepreciationDetailItem;
import com.smile.eam.service.DepreciationService;
import com.smile.eam.dto.*;
import com.smile.eam.dto.CategoryItemDto;
import com.smile.eam.dto.DepreciationDetailDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "折旧规则相关接口")
@RestController
@CrossOrigin
public class DepreciationController {


    @Resource
    DepreciationService depreciationService;

    /**
     * 获取折旧规则列表
     */
    @ApiOperation("获取折旧规则列表API")
    @GetMapping("/api/depreciation/getList")
    public JsonResult<List<Depreciation>> getList() {
        return new JsonResult<>(depreciationService.getList());
    }

    /**
     * 获取折旧模板详情列表
     */
    @ApiOperation("获取折旧模板详情列表")
    @GetMapping("/api/getDepreciation/Detail")
    public JsonResult<DepreciationDetailDto> getDepreciationDetail(int depreciationId) {
        JsonResult<DepreciationDetailDto> jsonResult = new JsonResult<>();
        DepreciationDetailDto depreciationDetail = depreciationService.getDepreciationDetail(depreciationId);
        jsonResult.setData(depreciationDetail);
        return jsonResult;
    }

    /**
     * 获取添加折旧时获取所有的分类列表
     */
    @ApiOperation("获取添加折扣时获取所有的分类列表")
    @GetMapping("/api/getCategoryAll")
    public JsonResult<List<CategoryItemDto>> getCategoryAll() {
        JsonResult<List<CategoryItemDto>> jsonResult = new JsonResult<>();
        jsonResult.setData(depreciationService.getCategoryAll());
        return jsonResult;
    }

    /**
     * 新增折旧规则
     */
    @ApiOperation("新增折旧规则")
    @PostMapping("/api/createDepreciation")
    public JsonResult createDepreciation(@Valid DepreciationCreateDto depreciationCreateDto) {
        JsonResult jsonResult = new JsonResult();
        if (depreciationService.createDepreciation(depreciationCreateDto) != 1) {
            jsonResult.setCode("ERROR");
            jsonResult.setData("新建错误");
        }
        return jsonResult;
    }

    /**
     * 新增折旧规则详情折旧模板
     */
    @ApiOperation("新增折旧规则详情折旧模板")
    @PostMapping("/api/createDepreciationDetail")
    public JsonResult createDepreciationDetail(DepreciationDetailCreateDto depreciationDetailCreateDto) {
        JsonResult jsonResult = new JsonResult();
        if (depreciationService.createDepreciationDetail(depreciationDetailCreateDto) != 1) {
            jsonResult.setCode("ERROR");
            jsonResult.setData("新增失败");
        }
        return jsonResult;
    }

    /**
     * 删除折旧模板
     */
    @ApiOperation("删除折旧模板")
    @PostMapping("/api/depreciationTemplate/deleteDetail")
    public JsonResult deleteDetail(int depreciationTemplateId) {
        JsonResult jsonResult = new JsonResult();
        if (depreciationService.deleteDetail(depreciationTemplateId) != 1) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("删除失败");
        }
        return jsonResult;
    }

    /**
     * 折旧规则删除
     */
    @ApiOperation("删除折旧规则API")
    @PostMapping("/api/depreciation/delete")
    public JsonResult delete(int id) {
        JsonResult jsonResult = new JsonResult();
        if (depreciationService.delete(id) != 1) {
            jsonResult.setCode("ERROR");
            jsonResult.setData("删除失败");
        }
        return jsonResult;
    }

    /**
     * 查看当前分类绑定折旧规则的折旧模板
     */
    @ApiOperation("查看当前分类绑定折旧规则的折旧模板")
    @GetMapping("/api/depreciation/getDepreciation")
    public JsonResult<List<DepreciationDetailItem>> getDepreciation(int categoryId) throws Exception {
        JsonResult<List<DepreciationDetailItem>> jsonResult = new JsonResult<>();
        List<DepreciationDetailItem> depreciation = depreciationService.getDepreciation(categoryId);
        jsonResult.setData(depreciation);
        return jsonResult;
    }

    /**
     * 折旧规则和分类绑定
     */
    @ApiOperation("折旧规则和分类绑定")
    @PostMapping("/api/depreciation/bindingCategory")
    public JsonResult bindingCategory(int categoryId, int depreciationId) throws Exception {
        JsonResult jsonResult = new JsonResult();
        if (depreciationService.bindingCategory(categoryId, depreciationId) != 1) {
            jsonResult.setCode("ERROR");
            jsonResult.setData("绑定失败");
        }
        return jsonResult;
    }

    /**
     * 资产id和折旧模板绑定
     */
    @ApiOperation("资产id和折旧模板绑定")
    @PostMapping("/api/depreciation/bindingAsset")
    public JsonResult bindingAsset(int assetId, int depreciationTemplateId) {
        JsonResult jsonResult = new JsonResult();
        if (depreciationService.bindingAsset(assetId, depreciationTemplateId) != 1) {
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     * 折旧规则批量删除
     */
    @ApiOperation("折旧规则批量删除")
    @PostMapping("/api/depreciation/deleteQuery")
    public JsonResult<String> deleteQuery(@RequestBody DepreciationDeleteQueryDto depreciationDeleteQueryDto) {
        depreciationService.deleteQuery(depreciationDeleteQueryDto);
        return new JsonResult<>();
    }

    /**
     * 折旧规则详情查看
     */
    @ApiOperation("折旧规则详情")
    @GetMapping("/api/depreciation/detail")
    public JsonResult<DepreciationResponseDto> detail(int id) {
        return depreciationService.detail(id);
    }

    /**
     * 折旧规则编辑
     */
    @ApiOperation("折旧规则编辑")
    @PostMapping("/api/depreciation/update")
    public JsonResult<String> update(@Valid @RequestBody DepreciationUpdateDto depreciationUpdateDto) {
        depreciationService.update(depreciationUpdateDto);
        return new JsonResult<>();
    }

    /**
     * 模糊搜索折旧规则列表
     */
    @ApiOperation("折旧规则模糊搜索API")
    @GetMapping("/api/depreciation/search")
    public JsonResult<List<DepreciationResponseDto>> getSearchList(DepreciationSearchDto depreciationSearchDto, Pagination pagination) {
        return new JsonResult<>(depreciationService.searchList(depreciationSearchDto, pagination));
    }

}
