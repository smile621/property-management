package com.smile.eam.controller.property;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.service.ServiceService;
import com.smile.eam.dto.*;
import com.smile.eam.dto.ServiceBindDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "服务相关接口")
@CrossOrigin
@RestController
public class ServiceController {

    @Resource
    ServiceService serviceService;


    /**
     * 服务记录清单列表获取API
     */
    @ApiOperation("服务列表获取API")
    @GetMapping("/api/property/service/getList")
    public JsonResult<ServiceListsDto> getList(Pagination pagination) {
        List<ServiceListDto> serviceListDtos = serviceService.getList(pagination);
        ServiceListsDto dto = ServiceListsDto.builder()
                .serviceListDto(serviceListDtos)
                .pagination(pagination)
                .build();
        return new JsonResult<>(dto);
    }

    /**
     * 新增服务记录
     */
    @ApiOperation("新增服务API")
    @PostMapping("/api/property/service/create")
    public JsonResult<String> create(@Valid ServiceCreateDto serviceCreateDto) {

        int row = serviceService.create(serviceCreateDto);

        if (row == -1) {
            return new JsonResult<>(false, "服务名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 服务记录清单编辑API
     */
    @ApiOperation("编辑服务API")
    @PostMapping("/api/property/service/update")
    public JsonResult<String> update(@Valid ServiceUpdateDto serviceUpdateDto) {

        int row = serviceService.update(serviceUpdateDto);

        if (row == -1) {
            return new JsonResult<>(false, "服务名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 服务查看API
     */
    @ApiOperation("查看服务API")
    @GetMapping("/api/property/service/detail")
    public JsonResult<ServiceDetailDto> detail(int id) {
        ServiceDetailDto detail = serviceService.detail(id);
        return new JsonResult<>(detail);
    }

    /**
     * 模糊搜索API
     */
    @ApiOperation("服务模糊搜索API")
    @GetMapping("/api/property/service/search")
    public JsonResult<ServiceListsDto> search(Pagination pagination, ServiceSearchDto serviceSearchDto) {
        List<ServiceListDto> list = serviceService.getSearchList(pagination, serviceSearchDto);
        ServiceListsDto dto = ServiceListsDto.builder()
                .serviceListDto(list)
                .pagination(pagination)
                .build();
        return new JsonResult<>(dto);
    }

    /**
     * 删除API
     */
    @ApiOperation("删除服务API")
    @PostMapping("/api/property/service/delete")
    public JsonResult<String> delete(int id) {
        serviceService.delete(id);
        return new JsonResult<>();
    }

    /**
     * 批量删除
     */
    @ApiOperation("批量删除服务API")
    @PostMapping("/api/property/service/deleteQuery")
    public JsonResult<String> deleteQuery(@RequestBody ServiceDeleteQueryDto serviceDeleteQueryDto) {
        serviceService.deleteQuery(serviceDeleteQueryDto);
        return new JsonResult<>();
    }

    /**
     * 状态变化API
     */
    @ApiOperation("状态变化API")
    @PostMapping("/api/property/service/status")
    public JsonResult<String> changeStatus(int code, int id) {
        serviceService.status(code, id);
        return new JsonResult<>();
    }

    /**
     * 报告异常
     */
    @ApiOperation("异常报告API")
    @PostMapping("/api/property/service/error")
    public JsonResult<String> createError(@Valid ServiceErrorDto serviceErrorDto) {
        serviceService.createError(serviceErrorDto);
        return new JsonResult<>();
    }

    /**
     * 设备绑定
     */
    @ApiOperation("设备绑定API")
    @PostMapping("/api/property/service/bind")
    public JsonResult<String> bind(@Valid ServiceBindDto serviceBindDto) {
        serviceService.bind(serviceBindDto);
        return new JsonResult<>();
    }

    /**
     * 按归属有无获取服务列表
     */
    @ApiOperation("服务归属列表API")
    @GetMapping("/api/property/service/affiliateList")
    public JsonResult<ServiceBindListsDto> getAffiliateList(Pagination pagination) {
        List<ServiceBindListDto> list = serviceService.getAffiliateList(pagination);
        ServiceBindListsDto dto = ServiceBindListsDto.builder()
                .pagination(pagination)
                .serviceBindListDto(list)
                .build();
        return new JsonResult<>(dto);
    }

    /**
     * 归属服务列表模糊搜索
     */
    @ApiOperation("有归属的服务列表模糊搜索API")
    @GetMapping("/api/property/service/searchAffiliateList")
    public JsonResult<ServiceBindListsDto> searchAffiliateList(@Valid ServiceAffiliateSearchDto serviceAffiliateSearchDto, Pagination pagination) {
        List<ServiceBindListDto> serviceBindListDtos = serviceService.searchAffiliateList(serviceAffiliateSearchDto, pagination);
        ServiceBindListsDto dto = ServiceBindListsDto.builder()
                .serviceBindListDto(serviceBindListDtos)
                .pagination(pagination)
                .build();
        return new JsonResult<>(dto);
    }

    /**
     * 归属解除
     */
    @ApiOperation("解除归属API")
    @PostMapping("/api/property/service/deleteAffiliate")
    public JsonResult<String> deleteAffiliate(int id) {
        serviceService.deleteAffiliate(id);
        return new JsonResult<>();
    }

    /**
     * 按故障拉取服务列表
     */
    @ApiOperation("服务异常列表API")
    @GetMapping("/api/property/service/errorList")
    public JsonResult<ServiceErrorListsDto> getErrorList(Pagination pagination) {
        List<ServiceErrorListDto> list = serviceService.getErrorList(pagination);
        ServiceErrorListsDto dto = ServiceErrorListsDto.builder()
                .list(list)
                .pagination(pagination)
                .build();
        return new JsonResult<>(dto);
    }

    /**
     * 模糊搜索按故障拉取服务列表
     */
    @ApiOperation("服务故障列表模糊搜索API")
    @GetMapping("/api/property/service/errorSearch")
    public JsonResult<ServiceErrorListsDto> searchErrorList(ServiceAffiliateSearchDto serviceAffiliateSearchDto, Pagination pagination) {
        List<ServiceErrorListDto> serviceErrorListDtos = serviceService.searchErrorList(serviceAffiliateSearchDto, pagination);
        ServiceErrorListsDto dto = ServiceErrorListsDto.builder()
                .list(serviceErrorListDtos)
                .pagination(pagination)
                .build();
        return new JsonResult<>(dto);
    }

    /**
     * 处理故障
     */
    @ApiOperation("故障处理API")
    @PostMapping("/api/property/service/deleteError")
    public JsonResult<String> deleteError(@Valid ServiceErrorResolveDto serviceErrorResolveDto) {

        serviceService.deleteError(serviceErrorResolveDto);
        return new JsonResult<>();
    }

}
