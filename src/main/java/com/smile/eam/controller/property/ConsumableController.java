package com.smile.eam.controller.property;
import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Category;
import com.smile.eam.service.ConsumableService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "耗材相关接口")
@RestController
@CrossOrigin
public class ConsumableController {

    @Resource
    ConsumableService consumableService;

    /**
     * 耗材记录清单列表
     */
    @ApiOperation("耗材记录清单列表")
    @GetMapping("/api/property/consumable/list")
    public JsonResult<ConsumableResponseDto> consumableList(Pagination pagination, String name) {

        JsonResult<ConsumableResponseDto> jsonResult = new JsonResult();

        //耗材记录清单搜索
        if (!name.equals("")) {

            List<ConsumableDto> consumableDtoList = consumableService.searchConsumable(pagination, name);
            ConsumableResponseDto dto = ConsumableResponseDto.builder()
                    .pagination(pagination).consumableDtoList(consumableDtoList)
                    .build();
            jsonResult.setData(dto);

            return jsonResult;
        }

        List<ConsumableDto> list = consumableService.getList(pagination);
        ConsumableResponseDto consumableResponseDto = ConsumableResponseDto.builder()
                .pagination(pagination).consumableDtoList(list)
                .build();
        jsonResult.setData(consumableResponseDto);

        return jsonResult;
    }

    /**
     * 耗材记录清单列表
     */
    @ApiOperation("耗材详情")
    @GetMapping("/api/property/consumable/detail")
    public JsonResult<ConsumableDto> consumableDetail(int id) {

        JsonResult<ConsumableDto> jsonResult = new JsonResult<>();
        ConsumableDto dto = consumableService.getConsumableDetail(id);
        jsonResult.setData(dto);

        return jsonResult;
    }

    /**
     * 耗材新增
     */
    @ApiOperation("耗材新增")
    @PostMapping("/api/property/consumable/add")
    public JsonResult<String> add(@Valid ConsumableCreateDto dto) {

        JsonResult<String> jsonResult = new JsonResult<>();
        boolean bool = consumableService.create(dto);

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
     * 耗材删除
     */
    @ApiOperation("耗材删除")
    @PostMapping("/api/property/consumable/delete")
    public JsonResult<String> delete(int id) {

        JsonResult<String> jsonResult = new JsonResult<>();
        String res = consumableService.delete(id);

        if (res.equals("INVALID_ID")) {
            jsonResult.setCode("INVALID_ID");
            jsonResult.setMessage("删除失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("删除成功");
        return jsonResult;
    }

    /**
     * 耗材编辑
     */
    @ApiOperation("耗材编辑")
    @PostMapping("/api/property/consumable/edit")
    public JsonResult<String> edit(ConsumableEditDto dto) {

        JsonResult<String> jsonResult = new JsonResult<>();
        boolean bool = consumableService.edit(dto);

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
     * 耗材搜索
     */
    @ApiOperation("耗材搜索")
    @GetMapping("/api/property/consumable/search")
    public JsonResult<ConsumableListResponseDto> search(Pagination pagination, String name) {

        List<ConsumableDto> consumableDtoList = consumableService.searchConsumable(pagination, name);

        ConsumableListResponseDto dto = ConsumableListResponseDto.builder()
                .pagination(pagination).consumableDtoList(consumableDtoList)
                .build();

        return new JsonResult<>(dto);
    }

    /**
     * 耗材入库
     */
    @ApiOperation("耗材入库")
    @PostMapping("/api/property/consumable/int")
    public JsonResult<String> intWareHouse(ConsumableIntWareHouseDto dto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        boolean bool = consumableService.intWareHouse(dto);
        if (!bool) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("入库失败");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("入库成功");
        return jsonResult;
    }

    /**
     * 耗材领用
     */
    @ApiOperation("耗材领用")
    @PostMapping("/api/property/consumable/out")
    public JsonResult<String> outWareHouse(@Valid ConsumableOutWareHouseDto dto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        String res = consumableService.outWareHouse(dto);

        if (res.equals("ERROR")) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("领用失败");
            return jsonResult;
        }

        if(res.equals("NOT_ENOUGH")){
            jsonResult.setCode("ERROR");
            jsonResult.setMessage(("库存不足"));
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("领用成功");
        return jsonResult;
    }

    /**
     * 耗材分类列表
     */
    @ApiOperation("耗材分类列表")
    @GetMapping("/api/property/consumable/category")
    public JsonResult<List<CategoryTreeDto>> categoryList() {

        JsonResult<List<CategoryTreeDto>> jsonResult = new JsonResult<>();
        List<CategoryTreeDto> categories = consumableService.getConsumableCategories();
        jsonResult.setData(categories);

        return jsonResult;
    }

    /**
     * 耗材分类列表 不嵌套
     */
    @ApiOperation("耗材分类列表 不嵌套")
    @GetMapping("/api/property/consumable/categories")
    public JsonResult<ConsumableCategoryResponseDto> categoryLists(Pagination pagination) {

        JsonResult<ConsumableCategoryResponseDto> jsonResult = new JsonResult<>();
        List<Category> categories = consumableService.getConsumableCategoryList(pagination);

        ConsumableCategoryResponseDto dto = ConsumableCategoryResponseDto.builder()
                .pagination(pagination).categoryList(categories)
                .build();
        jsonResult.setData(dto);

        return jsonResult;
    }

    /**
     * 耗材分类列表 详情
     */
    @ApiOperation("耗材分类列表")
    @GetMapping("/api/property/consumable/category/detail")
    public JsonResult<ConsumableCategoryDto> categoryDetail(int id) {
        JsonResult<ConsumableCategoryDto> jsonResult = new JsonResult<>();
        ConsumableCategoryDto categoryDto = consumableService.getDetail(id);
        jsonResult.setData(categoryDto);
        return jsonResult;
    }

    /**
     * 耗材分类新增
     */
    @ApiOperation("耗材分类新增")
    @PostMapping("/api/property/consumable/category/add")
    public JsonResult<String> categoryAdd(CategoryCreateDto dto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        boolean bool = consumableService.addConsumableCategory(dto);
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
     * 耗材分类删除
     */
    @ApiOperation("耗材分类删除")
    @PostMapping("/api/property/consumable/category/delete")
    public JsonResult<String> categoryDelete(int id) {
        JsonResult<String> jsonResult = new JsonResult<>();
        int res = consumableService.deleteConsumableCategory(id);

        if (res == -1) {
            jsonResult.setCode("INVALID_ID");
            jsonResult.setMessage("耗材顶级分类不能删除");
            return jsonResult;
        }
        jsonResult.setCode("SUCCESS");
        jsonResult.setMessage("删除成功");

        return jsonResult;
    }

    /**
     * 耗材分类编辑
     */
    @ApiOperation("耗材分类编辑")
    @PostMapping("/api/property/consumable/category/edit")
    public JsonResult<String> categoryEdit(CategoryCreateDto dto, int id) {
        JsonResult<String> jsonResult = new JsonResult<>();
        boolean bool = consumableService.editConsumableCategory(dto, id);
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
     * 耗材领用记录履历
     */
    @ApiOperation("耗材领用记录履历")
    @GetMapping("/api/property/consumable/receive")
    public JsonResult<ConsumableTrackResponseDto> receive(Pagination pagination) {
        JsonResult<ConsumableTrackResponseDto> jsonResult = new JsonResult();

        List<ConsumableInOutDto> dtoMap = consumableService.getConsumableTrack(pagination);
        ConsumableTrackResponseDto consumableTrackResponseDto = ConsumableTrackResponseDto.builder()
                .pagination(pagination).consumableInOutDtoList(dtoMap)
                .build();

        jsonResult.setData(consumableTrackResponseDto);
        return jsonResult;
    }

    /**
     * 耗材领用记录详情
     */
    @ApiOperation("耗材领用记录详情")
    @GetMapping("/api/property/consumable/receive/detail")
    public JsonResult<ConsumableInOutDto> trackDetail(int id) {

        JsonResult<ConsumableInOutDto> jsonResult = new JsonResult<>();

        List<ConsumableInOutDto> dtoMap = consumableService.getTrackDetail(id);
        ConsumableInOutDto dto = dtoMap.get(0);
        jsonResult.setData(dto);

        return jsonResult;
    }


    /**
     * 耗材履历搜索  根据 耗材名称 筛选
     */
    @ApiOperation("耗材履历搜索")
    @GetMapping("/api/property/consumable/searchReceive")
    public JsonResult<ConsumableTrackResponseDto> searchReceive(String name, Pagination pagination) {

        JsonResult<ConsumableTrackResponseDto> jsonResult = new JsonResult<>();
        List<ConsumableInOutDto> dtoMap = consumableService.searchConsumableTrack(name, pagination);

        ConsumableTrackResponseDto dto = ConsumableTrackResponseDto.builder()
                .pagination(pagination).consumableInOutDtoList(dtoMap)
                .build();

        jsonResult.setData(dto);
        return jsonResult;
    }

}
