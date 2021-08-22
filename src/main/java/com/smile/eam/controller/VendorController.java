package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Vendor;
import com.smile.eam.entity.VendorDetail;
import com.smile.eam.service.VendorService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "厂商相关接口")
@RestController
@CrossOrigin
public class VendorController {

    @Resource
    VendorService vendorService;

    /**
     * 获取单个厂商信息
     * */
    @ApiOperation("获取厂商信息")
    @GetMapping("/api/vendor/detail")
    public JsonResult<VendorDetailDto> detail(int id){

        VendorDto vendor = vendorService.getVendor(id);
        if(vendor==null){
            return new JsonResult<>("ERROR","该厂商不存在");
        }

        List<VendorUserDetailDto> userDetail = vendorService.getVendorUserDetail(id);
        VendorDetailDto vendorDetailDto = new VendorDetailDto();
        BeanUtils.copyProperties(vendor,vendorDetailDto);
        vendorDetailDto.setVendorUser(userDetail);

        JsonResult<VendorDetailDto> jsonResult = new JsonResult<>();
        jsonResult.setData(vendorDetailDto);

        return jsonResult;
    }

    /**
     * 获取厂商列表
     * */
    @ApiOperation("获取厂商列表")
    @GetMapping("/api/vendor/list")
    public JsonResult<VendorResponseDto> list(Pagination pagination){

        List<Vendor> vendorList = vendorService.vendorList();
        List<VendorDto> vendorDtoList = vendorList.stream()
                .map(vendor -> {
                    VendorDto vendorDto = new VendorDto();
                    BeanUtils.copyProperties(vendor,vendorDto);
                    return vendorDto;
                }).collect(Collectors.toList());
        pagination.setTotal(vendorDtoList.size());

        VendorResponseDto dto = VendorResponseDto.builder()
                .pagination(pagination).vendorDtoList(vendorDtoList)
                .build();

        return new JsonResult<>(dto);
    }

    /**
     * 插入厂商信息，并添加厂商联系人信息 也可以不添加
     * */
    @ApiOperation("插入厂商信息，并添加厂商联系人信息")
    @PostMapping("/api/vendor/create")
    public JsonResult<String> create(@Valid @RequestBody VendorCreateRequest request) throws Exception{

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int vendorId = vendorService.createVendor(request,time);
        if(request.getVendorUserDto()!=null){
            boolean exist = vendorService.create(request.getVendorUserDto(),vendorId);
            if(!exist){
                return new JsonResult<>("ERROR","厂商联系人信息创建失败，请重试");
            }
            return new JsonResult<>("SUCCESS","创建成功");
        }
        return new JsonResult<>("SUCCESS","创建成功");
    }

    /**
     * 删除厂商联系人信息
     * */
    @ApiOperation("删除厂商联系人信息")
    @PostMapping("/api/vendor/user/delete")
    public JsonResult<String> addVendorUser(int id){
        boolean bool = vendorService.deleteUser(id);
        if(!bool){
            return new JsonResult<>("ERROR","删除失败,没有该联系人");
        }
        return new JsonResult<>("SUCCESS","删除成功");
    }

    /**
     * 删除厂商时需要一并删除其下的厂商联系人
     * */
    @ApiOperation("删除厂商")
    @PostMapping("/api/vendor/delete")
    public JsonResult<String> deleteVendor(int id){

        boolean bool = vendorService.deleteVendor(id);
        boolean userBool = vendorService.deleteUser(id);
        if(!bool){
            return new JsonResult<>("ERROR","删除失败");
        }
        return new JsonResult<>("SUCCESS","删除成功");
    }

    /**
     * 编辑厂商信息   编辑厂商联系人信息
     * */
    @ApiOperation("编辑厂商信息")
    @PostMapping("/api/vendor/edit")
    public JsonResult<String> editVendor(@Valid VendorDto vendorDto, VendorUserDto vendorUserDto){
        Boolean notAndYes = vendorService.editVendor(vendorDto,vendorUserDto);
        if(!notAndYes){
            return new JsonResult<>("ERROR","编辑失败");
        }
        return new JsonResult<>("SUCCESS","编辑成功");
    }

    /**
     * 模糊搜索厂商信息  厂商名称
     * */
    @ApiOperation("厂商名称搜索厂商信息")
    @GetMapping("/api/vendor/search")
    public JsonResult<VendorSearchResponseDto> list(String name,Pagination pagination){

        JsonResult<VendorSearchResponseDto> jsonResult = new JsonResult<>();
        List<VendorDetail> vendorDtoList = vendorService.searchVendorDetail(name,pagination);

        VendorSearchResponseDto dto = VendorSearchResponseDto.builder()
                .pagination(pagination).vendorList(vendorDtoList)
                .build();

        jsonResult.setData(dto);

        return jsonResult;
    }

}
