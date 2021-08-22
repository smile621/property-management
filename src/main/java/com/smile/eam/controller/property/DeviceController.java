package com.smile.eam.controller.property;
import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Category;
import com.smile.eam.service.DeviceService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.smile.eam.until.ExcelUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "设备相关接口")
@CrossOrigin
@RestController
public class DeviceController {

    @Resource
    DeviceService deviceService;

    /**
     * 设备记录清单列表
     */
    @ApiOperation("设备记录清单列表")
    @GetMapping("/api/property/device/list")
    public JsonResult<DeviceResponseDto> list(String name, Pagination pagination) {

        JsonResult<DeviceResponseDto> jsonResult = new JsonResult<>();

        //传入空字符串时为记录清单列表api,不为空时是模糊搜索api
        if (!name.equals("")) {

            List<DeviceListDto> deviceAssetDtoList = deviceService.searchDevice(name, pagination);

            DeviceResponseDto dto = DeviceResponseDto.builder()
                    .pagination(pagination).dtoList(deviceAssetDtoList)
                    .build();
            jsonResult.setData(dto);

            return jsonResult;
        }

        List<DeviceListDto> dtoList = deviceService.switchId(pagination);
        DeviceResponseDto dto = DeviceResponseDto.builder()
                .pagination(pagination).dtoList(dtoList)
                .build();

        jsonResult.setData(dto);

        return jsonResult;
    }

    /**
     * 记录清单导出
     */
    @GetMapping("/api/property/device/list/export")
    public void exportDeviceList(String name, Pagination pagination, HttpServletResponse response) {
        if (!name.equals("")) {

            List<DeviceListDto> deviceAssetDtoList = deviceService.searchDevice(name, pagination);

        }
        List<DeviceListDto> deviceAssetDtoList = deviceService.switchId(pagination);


        String fileName = URLEncoder.encode("Device");
        String sheetName = URLEncoder.encode("Device");

        try {
//            response.setContentType("text/html;charset=utf-8");
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            ExcelUtil.writeExcel(response, deviceAssetDtoList, fileName, sheetName, new DeviceListDto());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * 设备详情
     */
    @GetMapping("/api/property/device/{deviceId}")
    public JsonResult<DeviceListDto> deviceDetail(@PathVariable(name = "deviceId") int deviceId) {
        DeviceListDto dto = deviceService.getDeviceDetail(deviceId);
        if (dto == null) {
            return new JsonResult<>("ERROR", "获取详情失败");
        }
        JsonResult jsonResult = new JsonResult();
        Map<String, Object> map = new HashMap<>();
        map.put("deviceDto", dto);
        jsonResult.setData(map);
        return jsonResult;
    }

    /**
     * 新增设备
     */
    @ApiOperation("新增设备")
    @PostMapping("/api/property/device/add")
    public JsonResult<String> addDevice(@Valid DeviceCreateDto request) {
        boolean bool = deviceService.createDevice(request);
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!bool) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("新增失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("新增成功");
        return jsonResult;
    }

    /**
     * 删除设备
     */
    @ApiOperation("删除设备")
    @PostMapping("/api/property/device/delete")
    public JsonResult<String> deleteDevice(int id) {
        JsonResult<String> jsonResult = new JsonResult<>();
        boolean bool = deviceService.deleteDevice(id);
        if (!bool) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("删除失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("删除成功");
        return jsonResult;
    }

    /**
     * 编辑设备
     */
    @ApiOperation("编辑设备")
    @PostMapping("/api/property/device/edit")
    public JsonResult<String> editDevice(@Valid DeviceUpdateDto deviceUpdateDto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        boolean bool = deviceService.editDevice(deviceUpdateDto);
        if (!bool) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("编辑失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("编辑成功");
        return jsonResult;
    }


    /**
     * 设备归属用户
     */
    @ApiOperation("设备用户绑定")
    @PostMapping("/api/property/device/bindUser")
    public JsonResult<String> bindUser(DeviceBindUserDto dto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        Boolean bool = deviceService.deviceBindUser(dto);
        if (!bool) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("绑定失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("绑定成功");
        return jsonResult;
    }

    /**
     * 设备被借用
     */
    @ApiOperation("设备被借用")
    @PostMapping("/api/property/device/lend")
    public JsonResult<String> lendDevice(DeviceLendDto dto) {
        Boolean bool = deviceService.lendDevice(dto);
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!bool) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("借用失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("借用成功");
        return jsonResult;
    }

    /**
     * 设备分类
     */
    @ApiOperation("设备分类")
    @GetMapping("/api/property/device/category")
    public JsonResult<List<CategoryTreeDto>> category() {
        JsonResult<List<CategoryTreeDto>> jsonResult = new JsonResult<>();
        List<CategoryTreeDto> categoryList = deviceService.showCategory();
        jsonResult.setData(categoryList);
        if (categoryList == null) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("设备类别为空");
            return jsonResult;
        }
        return jsonResult;
    }


    /**
     * 设备类别模糊查找
     */
    @ApiOperation("设备分类模糊查找name")
    @GetMapping("/api/property/device/category/search")
    public JsonResult<DeviceCategoryResponseDto> searchCategories(String name, Pagination pagination) {

        JsonResult<DeviceCategoryResponseDto> jsonResult = new JsonResult<>();
        List<Category> categoryList = deviceService.searchCategories(name, pagination);

        DeviceCategoryResponseDto dto = DeviceCategoryResponseDto.builder()
                .pagination(pagination).categoryList(categoryList)
                .build();
        jsonResult.setData(dto);

        return jsonResult;
    }

    /**
     * 设备分类详情
     */
    @ApiOperation("设备分类详情")
    @GetMapping("/api/property/device/category/{id}")
    public JsonResult<DeviceCategoryDetailDto> categoryDetail(@PathVariable(name = "id") int id) {
        JsonResult jsonResult = new JsonResult<>();
        DeviceCategoryDetailDto detail = deviceService.getCategoryDetail(id);
        Map<String, Object> map = new HashMap<>();
        map.put("categoryDto", detail);
        jsonResult.setData(map);
        if (detail == null) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("设备类别为空");
            return jsonResult;
        }
        return jsonResult;
    }


    /**
     * 设备不嵌套的分类
     */
    @ApiOperation("设备分类")
    @GetMapping("/api/property/device/categories")
    public JsonResult<DeviceCategoryResponseDto> categories(Pagination pagination) {

        JsonResult<DeviceCategoryResponseDto> jsonResult = new JsonResult<>();
        List<Category> categoryList = deviceService.showCategories(pagination);

        DeviceCategoryResponseDto dto = DeviceCategoryResponseDto.builder()
                .pagination(pagination).categoryList(categoryList)
                .build();
        jsonResult.setData(dto);

        if (categoryList.size() == 0) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("设备类别为空");
            return jsonResult;
        }

        return jsonResult;
    }


    /**
     * 设备类别删除
     */
    @ApiOperation("设备类别删除")
    @PostMapping("/api/property/device/category/delete")
    public JsonResult<String> categoryDelete(int categoryId) {
        Boolean bool = deviceService.deleteDeviceCategory(categoryId);
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!bool) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("删除失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("删除成功");
        return jsonResult;
    }

    /**
     * 设备类别编辑
     */
    @ApiOperation("设备类别编辑")
    @PostMapping("/api/property/device/category/edit")
    public JsonResult<String> categoryEdit(@Valid DeviceCategoryUpdateDto dto) {
        Boolean aBoolean = deviceService.editDeviceCategory(dto);
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!aBoolean) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("编辑失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("编辑成功");
        return jsonResult;
    }

    /**
     * 设备类别新增
     */
    @ApiOperation("设备类别新增")
    @PostMapping("/api/property/device/category/add")
    public JsonResult<String> categoryAdd(@Valid DeviceCategoryUpdateDto dto) {
        Boolean aBoolean = deviceService.addDeviceCategory(dto);
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!aBoolean) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("类别新增失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("类别新增成功");
        return jsonResult;
    }

    /**
     * 设备解除归属
     */
    @ApiOperation("设备解除归属")
    @PostMapping("/api/property/device/attribute/remove")
    public JsonResult<String> removeAttribute(int id) {
        Boolean aBoolean = deviceService.dismissDevice(id);
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!aBoolean) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("解除失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("解除成功");
        return jsonResult;
    }

    /**
     * 设备归还
     */
    @ApiOperation("设备归还")
    @PostMapping("/api/property/device/return")
    public JsonResult<String> returnDevice(int id, String description) {
        Boolean aBoolean = deviceService.returnDevice(id, description);
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!aBoolean) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("归还失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("归还成功");
        return jsonResult;
    }

    /**
     * 设备归属列表
     */
    @ApiOperation("设备归属列表")
    @GetMapping("/api/property/device/attributeList")
    public JsonResult<DeviceTrackResponseDto> attributeList(Pagination pagination) {
        List<DeviceListDto> dtoList = deviceService.switchId(pagination);

        //所有设备的资产id
        Set<Integer> fixedAssetIds = dtoList.stream().map(DeviceListDto::getId).collect(Collectors.toSet());
        JsonResult<DeviceTrackResponseDto> jsonResult = new JsonResult<>();

        List<AssetTrackDto> assetTrackDtoList = deviceService.getAttributeList();
        List<AssetTrackDto> list = assetTrackDtoList.stream().filter((item) -> {
            int status = 0;
            for (Integer id : fixedAssetIds) {
                if (item.getFixedAssetId() == id) {
                    status = 1;
                }
            }
            //状态为1 的item返回
            return status == 1;
        }).collect(Collectors.toList());

        pagination.setTotal(list.size());
        DeviceTrackResponseDto dto = DeviceTrackResponseDto.builder()
                .pagination(pagination).trackDtoList(list)
                .build();

        jsonResult.setData(dto);
        return jsonResult;
    }

    /**
     * 设备归属详情
     */
    @ApiOperation("设备归属详情")
    @GetMapping("/api/property/device/attribute/{id}")
    public JsonResult<AssetTrackDto> attributeDetail(@PathVariable(name = "id") int id) {

        JsonResult<AssetTrackDto> jsonResult = new JsonResult<>();
        List<AssetTrackDto> assetTrackDtoList = deviceService.getAttributeList();

        List<AssetTrackDto> trackDtoList = assetTrackDtoList.stream().filter((item) -> {
            return item.getId() == id;
        }).collect(Collectors.toList());

        AssetTrackDto dto = trackDtoList.get(0);
        jsonResult.setData(dto);
        return jsonResult;
    }

    /**
     * 统计
     */
    @ApiOperation("设备统计")
    @PostMapping("/api/property/device/attribute/statistic")
    public JsonResult<DeviceStatisticResponseDto> statistic(int year, Pagination pagination) {
        List<DeviceStatisticDto> totalPrice = deviceService.getTotalPrice(year, pagination);

        DeviceStatisticResponseDto dto = DeviceStatisticResponseDto.builder()
                .pagination(pagination).dtoList(totalPrice)
                .build();
        JsonResult<DeviceStatisticResponseDto> jsonResult = new JsonResult<>();
        jsonResult.setData(dto);

        return new JsonResult<>();
    }

    /**
     * 配件归属设备
     */
    @GetMapping("/api/property/device/attributePart")
    public JsonResult<List<DeviceAttributePartDto>> getAttributePart(int id) {
        List<DeviceAttributePartDto> dtoList = deviceService.getAttributePart(id);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(dtoList);
        return jsonResult;
    }

    /**
     * 软件归属设备
     */
    @GetMapping("/api/property/device/attributeSoftWare")
    public JsonResult<List<DeviceAttributeSoftWareDto>> getAttributeSoftWare(int id) {
        List<DeviceAttributeSoftWareDto> dtoList = deviceService.getAttributeSoftWare(id);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(dtoList);
        return jsonResult;
    }

    /**
     * 服务归属设备
     */
    @GetMapping("/api/property/device/attributeService")
    public JsonResult<List<DeviceAttributeServiceDto>> getAttributeService(int id) {
        List<DeviceAttributeServiceDto> dtoList = deviceService.getAttributeService(id);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(dtoList);
        return jsonResult;
    }

    /**
     * 设备故障报修
     */
    @PostMapping("/api/property/device/error")
    public JsonResult<String> reportError(DeviceMalfunctionRequest request) {

        String res = deviceService.sendMalfunction(request);
        if (res.equals("REPEAT")) {
            return new JsonResult<>("ERROR", "请勿重复报修");
        }
        if (res.equals("ERROR")) {
            return new JsonResult<>("ERROR", "报修失败");
        }

        return new JsonResult<>("SUCCESS", "报修成功");
    }

}
