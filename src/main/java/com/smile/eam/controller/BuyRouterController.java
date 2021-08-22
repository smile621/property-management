package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.BuyRouter;
import com.smile.eam.service.BuyRouterService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "购入途径相关接口")
@RestController
@CrossOrigin
public class BuyRouterController {

    @Resource
    BuyRouterService buyRouterService;

    /**
     * 获取购入途径列表
     */
    @ApiOperation("获取购入途径列表API")
    @GetMapping("/api/buyRouter/getList")
    public JsonResult<BuyRouterResponseDto> getBuyRouterList(BuyRouterSearchDto buyRouterDto, Pagination pagination) {

        List<BuyRouter> buyRouterList = buyRouterService.getBuyRouterList(buyRouterDto, pagination);

        BuyRouterResponseDto dto = BuyRouterResponseDto.builder()
                .pagination(pagination)
                .buyRouterResponseDtoList(buyRouterList)
                .build();

        return new JsonResult<>(dto);
    }

    /**
     * 新增购入途径
     */
    @ApiOperation("新增购入途径API")
    @PostMapping("/api/buyRouter/create")
    public JsonResult<String> createBuyRouter(@Valid BuyRouterCreateDto buyRouterCreateDto) {

        int i = buyRouterService.createBuyRouter(buyRouterCreateDto);

        if (i == -1) {
            return new JsonResult<>(false, "购买途径名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 删除购入途径
     */
    @ApiOperation("删除购入途径API")
    @PostMapping("/api/buyRouter/delete")
    public JsonResult<String> deleteBuyRouter(int id) {

        buyRouterService.deleteBuyRouter(id);

        return new JsonResult<>();
    }

    /**
     * 编辑购入途径
     */
    @ApiOperation("编辑购入途径API")
    @PostMapping("/api/buyRouter/update")
    public JsonResult<String> updateBuyRouter(@Valid BuyRouterUpdateDto buyRouterUpdateDto) {

        int row = buyRouterService.updateBuyRouter(buyRouterUpdateDto);

        if (row == -1) {
            return new JsonResult<>(false, "购入途径名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 购入途径详情
     */
    @ApiOperation("购入途径详情显示API")
    @GetMapping("/api/buyRouter/detail")
    public JsonResult<BuyRouter> buyRouterDetail(int id) {

        BuyRouter buyRouter = buyRouterService.buyRouterDetail(id);

        return new JsonResult<>(buyRouter);
    }

    /**
     * 购入途径批量删除
     */
    @ApiOperation("购入途径批量删除API")
    @PostMapping("/api/buyRouter/deleteQuery")
    public JsonResult<String> deleteBuyRouters(@RequestBody @Valid BuyRouterDeleteQueryDto buyRouterDeleteQueryDto) {

        buyRouterService.deleteBuyRouters(buyRouterDeleteQueryDto);

        return new JsonResult<>();
    }
}
