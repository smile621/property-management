package com.smile.eam.controller.property;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.AssetAffiliate;
import com.smile.eam.entity.AssetCategory;
import com.smile.eam.entity.FixedAsset;
import com.smile.eam.service.SoftwareService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Api(tags = "软件相关接口")
@RestController
@CrossOrigin
public class SoftwareController {
    @Resource
    SoftwareService softwareService;

    /**
     * 添加软件分类列表
     */
    @ApiOperation("添加软件分类列表API")
    @PostMapping("/api/property/software/category/create")
    public JsonResult<String> createSoftware(@Valid SoftwareCreateRequest softwareCreateRequest){
        //是否已存在于列表
        Boolean flag = softwareService.existSoftware(softwareCreateRequest);
        if (flag) {
            return new JsonResult<>("ERROR", "已存在该软件分类");
        }
        softwareService.addSoftware(softwareCreateRequest);
        return new JsonResult<>("已成功添加该软件分类");
    }

    /**
     * 编辑软件分类
     */
    @ApiOperation("编辑软件分类列表API")
    @PostMapping("/api/property/software/category/edit")
    public JsonResult<String> editSoftware(@Valid UpdateSoftwareByIdRequest updateSoftwareByIdRequest) {
        //传入的id父类是否为3
        Boolean e = softwareService.isExist(updateSoftwareByIdRequest.getId());
        if(!e){
            return new JsonResult<>("ERROR", "没有该软件分类");
        }
        //根据id查找asset_category 对应的数据，进行修改
        int s = softwareService.updateSoftware(updateSoftwareByIdRequest);

        if (s == 1) {
            return new JsonResult<>("修改成功");
        }
        return new JsonResult<>("ERROR", "修改失败");
    }

    /**
     * 软件分类删除
     */
    @ApiOperation("删除软件分类列表API")
    @PostMapping("/api/property/software/category/delete")
    public JsonResult<String> deleteSoftware(@Valid DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest) {
        Boolean flag = softwareService.findSoftware(deleteSoftwareCategoryRequest);
        if (!flag) {
            return new JsonResult<>("ERROR", "没有该分类");
        }
        int s = softwareService.deleteSoftware(deleteSoftwareCategoryRequest);
        //资产表中对应的分类id设置为0
        softwareService.edit(deleteSoftwareCategoryRequest.getId());
        if (s == 1) {
            return new JsonResult<>("删除成功");
        }
        return new JsonResult<>("ERROR", "删除失败");
    }

    /**
     * 软件分类模糊查找
     * */
    @ApiOperation("软件分类模糊查找API")
    @GetMapping("/api/property/software/category/lookFor")
    public JsonResult<SoftwareCategoryDto> lookFor(String search, Pagination pagination){
        //根据name,模糊查找软件分类
        List<AssetCategory> assetCategoryList = softwareService.lookFor(search, pagination);
        //获取总条数
        pagination.setTotal(softwareService.lookForCount(search));
        SoftwareCategoryDto data = SoftwareCategoryDto.builder()
                .pagination(pagination)
                .assetCategoryList(assetCategoryList)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 软件分类列表params
     */
    @ApiOperation("获取软件分类列表API")
    @GetMapping("/api/property/software/category/list")
    public JsonResult<SoftwareCategoryListDto> softwareCategoryList(@Valid GetSoftwareCategoryById getSoftwareCategoryById, Pagination pagination) {
        int total = softwareService.softwareCategoryCount();
        pagination.setTotal(total);
        List<SoftwareCategoryResponse> softwareCategory = softwareService.findSoftwareCategory(getSoftwareCategoryById, pagination);
        SoftwareCategoryListDto data = SoftwareCategoryListDto.builder()
                .softwareCategory(softwareCategory)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 批量删除软件分类列表
     * */
    @ApiOperation("批量删除软件分类列表API")
    @PostMapping("/api/property/software/category/deleteList")
    public JsonResult<String> deleteCategoryList(@Valid SoftwareListDeleteRequest softwareListDeleteRequest){
        softwareService.deleteSoftwareCategoryListByIds(softwareListDeleteRequest);
        return new JsonResult<>("批量删除成功");
    }

    /**
     * 软件资产新增
     */
    @ApiOperation("新增软件资产API")
    @PostMapping("/api/property/software/create")
    public JsonResult<String> addSoftwareProperty(@Valid SoftwarePropertyCreateRequest softwarePropertyCreateRequest) {
        softwareService.addSoftwareProperty(softwarePropertyCreateRequest);
        return new JsonResult<>("新增成功");
    }

    /**
     * 获取软件资产详情
     * */
    @ApiOperation("获取软件资产详情API")
    @GetMapping("/api/property/software/detail")
    public JsonResult<FixedAsset> getSoftwareDetail(@Valid DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest){
        //获取软件资产详情
        FixedAsset softwareDetail = softwareService.getSoftwareDetail(deleteSoftwareCategoryRequest);
        if(softwareDetail==null){
            return new JsonResult<>("ERROR","未找到该资产");
        }
        return new JsonResult<>(softwareDetail);
    }

    /**
     * 软件详情
     * */
    @ApiOperation("软件详情API")
    @GetMapping("/api/property/software/Detail")
    public JsonResult<List<SoftwarePropertyResponse>> softwareDetail(@Valid DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest){
        //获取软件资产详情
        List<SoftwarePropertyResponse> softwareDetail = softwareService.softwareDetail(deleteSoftwareCategoryRequest);
        if(softwareDetail==null){
            return new JsonResult<>("ERROR","未找到该资产");
        }
        return new JsonResult<>(softwareDetail);
    }
    /**
     * 软件资产编辑
     */
    @ApiOperation("编辑软件资产API")
    @PostMapping("/api/property/software/edit")
    public JsonResult<String> editSoftwareProperty(@Valid SoftwarePropertyCreateRequest softwarePropertyCreateRequest,
                                           @Valid UpdateSoftwarePropertyById updateSoftwarePropertyById) {
        //是否有该软件分类
        if(softwareService.isCategory(softwarePropertyCreateRequest.getAssetCategoryId())==null){
            throw  new RuntimeException("没有该软件分类");
        }
        //是否有该购买途径
        if(softwareService.isBuyRouter(softwarePropertyCreateRequest.getBuyRouterId())==null){
            throw  new RuntimeException("没有该购买途径");
        }
        //是否有厂商
        if(softwareService.isVendor(softwarePropertyCreateRequest.getVendorId())==null){
            throw  new RuntimeException("没有该厂商");
        }
        //是否有版本发行方式
        if(softwareService.isIssue(softwarePropertyCreateRequest.getIssueId())==null){
            throw  new RuntimeException("没有该发行方式");
        }
        FixedAsset fixed = softwareService.findSoftwarePropertyById(updateSoftwarePropertyById);
        if (fixed == null) {
            return new JsonResult<>("ERROR", "没有该软件资产");
        }
        int s = softwareService.editSoftwareProperty(softwarePropertyCreateRequest, updateSoftwarePropertyById);
        return new JsonResult<>("修改成功");
    }

    /**
     * 软件资产绑定
     */
    @ApiOperation("软件资产绑定API")
    @PostMapping("/api/property/software/binding")
    public JsonResult<String> bindSoftware(@Valid BindSoftwareRequest bindSoftwareRequest) {
        if (!softwareService.existBindSoftwarePropertyAssetId(bindSoftwareRequest)) {
            return new JsonResult<>("ERROR", "没有该软件");
        }
        if (!softwareService.existBindSoftwarePropertyAssetAffiliate(bindSoftwareRequest)) {
            return new JsonResult<>("ERROR", "没有该设备");
        }
        Boolean flag = softwareService.isSoftwareBindDevice(bindSoftwareRequest);
        if (flag) {
            return new JsonResult<>("ERROR", "该软件已绑定该设备");
        }
        //修改软件资产状态为：2：归属
        int a = softwareService.updateAssetStatusById(bindSoftwareRequest);
        if (a < 1) {
            return new JsonResult<>("ERROR", "软件资产状态修改失败");
        }
        //添加软件资产绑定记录
        softwareService.bindSoftware(bindSoftwareRequest);
        //添加绑定记录到 履历表
        softwareService.addSoftwareBind(bindSoftwareRequest.getAssetId(),bindSoftwareRequest.getAssetAffiliate());
        return new JsonResult<>("绑定成功");
    }

    /**
     * 软件资产删除
     */
    @ApiOperation("删除软件资产API")
    @PostMapping("/api/property/software/delete")
    public JsonResult<String> deleteSoftwareProperty(@Valid UpdateSoftwarePropertyById updateSoftwarePropertyById) {
        FixedAsset fixed = softwareService.findSoftwarePropertyById(updateSoftwarePropertyById);
        if (fixed == null) {
            return new JsonResult<>("ERROR", "没有该软件资产");
        }
        //删除资产表中软件
        softwareService.deleteSoftwareProperty(updateSoftwarePropertyById);
        //删除资产绑定表中软件
        softwareService.deleteByAssetId(updateSoftwarePropertyById);
        return new JsonResult<>("软件资产删除成功");
    }

    /**
     * 取消软件资产绑定..
     */
    @ApiOperation("取消软件资产绑定API")
    @PostMapping("/api/property/software/binding/off")
    public JsonResult<String> cancelBindSoftware(Integer id) {
        AssetAffiliate assetIdById = softwareService.findAssetIdById(id);
        int c = softwareService.cancelBindSoftware(id);
        //获取已解除绑定的id对应的asset_id
        int assetId = assetIdById.getAssetId();
        //判断该软件资产是否还有别的已绑定设备
        int e = softwareService.isExistAssetId(assetId);
        if (e < 1) {
            //修改资产表中软件状态为1：闲置
            softwareService.updateAssetStatus(assetId);
        }
        //添加取消绑定到 归属表
        softwareService.addSoftwareCancelBind(assetIdById.getAssetId(),assetIdById.getAssetAffiliate());
        if (c < 1) {
            return new JsonResult<>("ERROR", "取消绑定失败,请检查该软件是否已绑定该资产");
        }
        return new JsonResult<>("取消绑定成功");
    }

    /**
     * 软件资产归属列表
     */
    @ApiOperation("软件资产归属列表API")
    @GetMapping("/api/property/software/binding/list")
    public JsonResult<BindSoftwareListDto> bindSoftwareList(Pagination pagination) {
        //获取总条数
        int total = softwareService.assetIdCount();
        pagination.setTotal(total);
        List<AssetAffiliateDto>  SoftwareAssetAffiliateResponseList = softwareService.bindSoftwareList(pagination);
        BindSoftwareListDto data = BindSoftwareListDto.builder()
                .SoftwareAssetAffiliateResponseList(SoftwareAssetAffiliateResponseList)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }
    @ApiOperation("软件绑定模糊查找API")
    @PostMapping("/api/property/software/binding/lookFor")
    public JsonResult<FindBindSoftwareDto> findBindSoftware(String name,Pagination pagination){
        //传入name,模糊查找对应的绑定表
        FindBindSoftwareDto data = softwareService.findByName(name, pagination);
        return new JsonResult<>(data);
    }

    /**
     * 批量删除软件归属列表
     * */
    @ApiOperation("批量删除软件归属列表API")
    @PostMapping("/api/property/software/binding/deleteList")
    public JsonResult<String> deleteBindList(@Valid SoftwareListDeleteRequest softwareListDeleteRequest){
        softwareService.deleteSoftwareBindListByIds(softwareListDeleteRequest);
        return new JsonResult<>("批量删除成功");
    }

    /**
     * 软件主数据列表模糊查找
     */
    @ApiOperation("软件主数据列表模糊查找API")
    @GetMapping("/api/property/software/lookFor")
    public JsonResult<LookForSoftwareDto> lookForSoftware(@Valid FindSoftwareFixedAssetRequest findSoftwareFixedAssetRequest, Pagination pagination) {
        List<SoftwarePropertyResponse> fixedAsset = softwareService.findFixedAsset(findSoftwareFixedAssetRequest, pagination);
        int total = softwareService.findFixedAssetCount(findSoftwareFixedAssetRequest, pagination);
        pagination.setTotal(total);
        LookForSoftwareDto data = LookForSoftwareDto.builder()
                .fixedAsset(fixedAsset)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 软件资产列表
     */
    @ApiOperation("获取软件资产列表API")
    @GetMapping("/api/property/software/list")
    public JsonResult<SoftwarePropertyListDto> SoftwarePropertyList( Pagination pagination) {
        //更新过保时间
        softwareService.updateExpiredDate();
        //获取资产列表
        List<SoftwarePropertyResponse> softwarePropertyResponses = softwareService.softwarePropertyList( pagination);
        //获取软件列表总数量
        int s = softwareService.softwarePropertyCount();
        pagination.setTotal(s);
        SoftwarePropertyListDto data = SoftwarePropertyListDto.builder()
                .softwarePropertyResponses(softwarePropertyResponses)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 批量删除软件资产列表
     * */
    @ApiOperation("(批量)删除软件资产列表API")
    @PostMapping("/api/property/software/deleteList")
    public JsonResult<String> delete(@Valid SoftwareListDeleteRequest softwareListDeleteRequest){
        softwareService.deleteSoftwareListByIds(softwareListDeleteRequest);
        return new JsonResult<>("批量删除成功");
    }

    /**
     * 软件统计页api
     * */
    @ApiOperation("软件统计页api")
    @GetMapping("/api/property/software/count")
    public JsonResult<CountDto> count(@Valid SoftwareStatisticRequest softwareStatisticRequest){
        //软件总数
        int totalSoftware = softwareService.softwarePropertyCount();
        //即将过保软件总数
        int willPastCount = softwareService.willPast();
        //已过保软件数
        int past = softwareService.past();
        //全年价值趋势
        Map<String, BigDecimal> decimalMap = softwareService.allYearValue(softwareStatisticRequest.getYear());
        CountDto data = CountDto.builder()
                .totalSoftware(totalSoftware)
                .willPastCount(willPastCount)
                .past(past)
                .decimalMap(decimalMap)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 软件归属记录
     * */
    @ApiOperation("软件归属记录api")
    @GetMapping("/api/property/software/track")
    public JsonResult<TrackDto> track(Integer id,Pagination pagination){
        //获取软件取消绑定记录
        List<SoftwareTrackResponse> trackResponseList = softwareService.getTrack(id, pagination);
        //获取 归属列表总条数
        pagination.setTotal(softwareService.getTrackCount(id));
        TrackDto data = TrackDto.builder()
                .trackResponseList(trackResponseList)
                .pagination(pagination)
                .build();
        return new JsonResult<>(data);
    }

    @ApiOperation("软件分类递归api")
    @GetMapping("/api/property/software/tree")
    public JsonResult<List<TreeNode>> treeNode(){
        List<TreeNode> list = softwareService.test();
        return new JsonResult<>(list);
    }
}