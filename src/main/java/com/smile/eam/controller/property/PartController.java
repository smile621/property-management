package com.smile.eam.controller.property;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Category;
import com.smile.eam.mapper.VendorMapper;
import com.smile.eam.service.PartService;
import com.smile.eam.dto.*;
import lombok.SneakyThrows;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(tags = "配件相关接口")
@CrossOrigin
@RestController
public class PartController {

    @Autowired
    PartService partService;

    @Autowired
    VendorMapper vendorMapper;

    /**
     *获取配件列表
     */
    @ApiOperation("获取配件列表API")
    @GetMapping("/api/property/part/partPropertyList")
    public JsonResult<List<PartReturnDto>> getPartPropertyList(Pagination pagination, @Valid PartSearchDto partSearchDto)  {
        List<PartReturnDto> partReturnDtoList = partService.findPartList(partSearchDto,pagination);
        JsonResult<List<PartReturnDto>> jsonResult = new JsonResult<>();
        jsonResult.setData(partReturnDtoList);
        return jsonResult;
    }

    /**
     *添加配件
     */
    @ApiOperation("添加配件API")
    @PostMapping("/api/property/part/create")
    public JsonResult<String> createPart(@Valid PartCreateDto partCreateDto) throws ParseException {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.createPart(partCreateDto) != null) {
            jsonResult.setCode("SUCCESS");
            jsonResult.setMessage("配件添加成功");
        }
        return jsonResult;
    }

    /**
     *删除配件
     */
    @ApiOperation("删除配件API")
    @PostMapping("/api/property/part/delete")
    public JsonResult<String> deletePart(@Valid PartDeleteDto partDeleteDto)  {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.deletePart(partDeleteDto.getFixedAssetId()) != 1) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("删除配件失败");
        }
        return jsonResult;
    }

    /**
     *配件编辑
     */
    @SneakyThrows
    @ApiOperation("编辑配件API")
    @PostMapping("/api/property/part/category/edit")
    public JsonResult<String> updatePart(@Valid PartUpdateDto partUpdateDto)  {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.updatePart(partUpdateDto) != 1) {
            jsonResult.setCode("ERROR");
            jsonResult.setMessage("修改失败");
        }
        return jsonResult;
    }

    /**
     *获取分类列表(递归)
     */
    @ApiOperation("获取分类列表")
    @GetMapping("/api/property/part/categoryList")
    public JsonResult<Map<Integer,List<Category>>> getPartCategoryList(){
        JsonResult<Map<Integer,List<Category>>> jsonResult = new JsonResult<>();
        jsonResult.setData(partService.findCategoryList());
        return jsonResult;
    }

    /**
     *配件主页添加配件分类选择
     */
    @GetMapping("/api/property/part/categoryListHome")
    public JsonResult<List<Object>> getPartCategoryListHome(){
        JsonResult<List<Object>> jsonResult = new JsonResult<>();
        jsonResult.setData(partService.findCategoryListHome());
        return jsonResult;
    }

    /**
     * 配件主页添加配件分类选择
     */
    @GetMapping("/api/property/part/categoryListCategory")
    public JsonResult<List<Object>> getPartCategoryListCategory(){
        JsonResult<List<Object>> jsonResult = new JsonResult<>();
        jsonResult.setData(partService.findCategoryListCategory());
        return jsonResult;
    }

    /**
     * 新增配件分类
     */
    @ApiOperation("新增配件分类")
    @PostMapping("/api/property/part/createCategory")
    public JsonResult<String> createCategory(@Valid PartCategoryCreateDto partCategoryCreateDto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.createCategory(partCategoryCreateDto)!=1){
            jsonResult.setMessage("添加失败");
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     *删除配件分类
     */
    @ApiOperation("删除配件分类")
    @PostMapping("/api/property/part/deleteCategory")
    public JsonResult<String> deletePartCategory(int CategoryId){
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.deleteCategory(CategoryId) <= 0)
        {
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     * 编辑配件分类
     */
    @ApiOperation("编辑配件分类")
    @PostMapping("/api/property/part/updateCategory")
    public JsonResult<String> updatePartCategory(@Valid PartCategoryUpdateDto partCategoryUpdateDto) {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.updateCategory(partCategoryUpdateDto)!=1){
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     *配件归属绑定
     */
    @ApiOperation("配件归属绑定")
    @PostMapping("/api/property/part/bindDevice")
    public JsonResult<String> partBindDevice(int partAssetId,int deviceAssetId){
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.bindDevice(partAssetId,deviceAssetId) != 1){
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     *获取配件归属列表
     */
    @ApiOperation("获取配件归属列表")
    @GetMapping("/api/property/part/partBindPropertyList")
    public JsonResult<List<PartTrackReturnDto>> getPartBindPropertyList(Pagination pagination){
        JsonResult<List<PartTrackReturnDto>> jsonResult = new JsonResult<>();
        jsonResult.setData(partService.findPartBindList(pagination));
        return jsonResult;
    }

    /**
     *解除归属绑定
     */
    @ApiOperation("解除归属绑定")
    @PostMapping("/api/property/part/cancelBind")
    public JsonResult<String> cancelBind(int assetId){
        JsonResult<String> jsonResult = new JsonResult<>();
        if (!partService.cancelBind(assetId)){
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

    /**
     *解除归属绑定
     */
    @ApiOperation("配件故障报修")
    @PostMapping("/api/property/part/malfunction")
    public JsonResult<String> malfunction(int assetId,String description) {
        JsonResult<String> jsonResult = new JsonResult<>();
        if (partService.malfunction(assetId,description)!=1){
            jsonResult.setCode("ERROR");
        }
        return jsonResult;
    }

}

