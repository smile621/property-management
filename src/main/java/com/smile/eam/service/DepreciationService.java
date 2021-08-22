package com.smile.eam.service;


import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.mapper.CategoryMapper;
import com.smile.eam.mapper.DepreciationMapper;
import com.smile.eam.dto.*;
import com.smile.eam.entity.*;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepreciationService {

    @Resource
    DepreciationMapper depreciationMapper;

    @Resource
    CategoryMapper categoryMapper;

    /**
     * 获取折旧规则列表
     */
    public List<Depreciation> getList() {
        return depreciationMapper.getList();
    }

    /**
     * 查看折旧详情
     */
    public DepreciationDetailDto getDepreciationDetail(int depreciationId) {

        //通过折旧id查看折旧表
        DepreciationItem depreciation = depreciationMapper.findDepreciation(depreciationId);
        //通过折旧id去详情表中查详情
        List<DepreciationDetailItem> depreciationDetail = depreciationMapper.findDepreciationDetail(depreciationId);

        List<DepreciationDetailListDto> depreciationDetailLists = new ArrayList<>();

        //组装周期Map
        Map<Integer, String> measureMap = new HashMap<>();
        measureMap.put(1, "天");
        measureMap.put(2, "月");
        measureMap.put(3, "年");

        if (depreciationDetail.size() != 0) {
            for (DepreciationDetailItem depreciationDetailItem : depreciationDetail) {
                DepreciationDetailListDto depreciationDetailList = new DepreciationDetailListDto();
                depreciationDetailList.setId(depreciationDetailItem.getId());
                depreciationDetailList.setName(depreciationDetailItem.getTemplateName());
                depreciationDetailList.setPeriod(depreciationDetailItem.getPeriod());
                depreciationDetailList.setMeasure(measureMap.get(depreciationDetailItem.getMeasure()));
                depreciationDetailList.setRate(depreciationDetailItem.getRate());
                depreciationDetailLists.add(depreciationDetailList);
            }
        }

        //组装分类Map
        Map<Integer, String> CategoryMap = new HashMap<>();
        List<CategoryItemDto> categoryList = categoryMapper.findCategoryAll();
        for (CategoryItemDto category : categoryList) {
            CategoryMap.put(category.getId(), category.getName());
        }

        DepreciationDetailDto depreciationDetails = new DepreciationDetailDto();
        depreciationDetails.setId(depreciation.getId());
        depreciationDetails.setName(depreciation.getName());
        depreciationDetails.setDescription(depreciation.getDescription());
        depreciationDetails.setCategory(CategoryMap.get(depreciation.getAssetCategoryId()));
        depreciationDetails.setCreatedAt(depreciation.getCreatedAt());
        depreciationDetails.setUpdatedAt(depreciation.getUpdatedAt());
        depreciationDetails.setDepreciationDetailLists(depreciationDetailLists);

        return depreciationDetails;

    }

    /**
     * 查看所有分类
     */
    public List<CategoryItemDto> getCategoryAll() {
        return categoryMapper.findCategoryAll();
    }

    /**
     * 创建折旧规则
     */
    public int createDepreciation(DepreciationCreateDto depreciationCreateDto) {
        if (depreciationCreateDto.getDescription() == null) {
            depreciationCreateDto.setDescription("");
        }
        //验证传入值是否正确
        //新建
        return depreciationMapper.createDepreciation(depreciationCreateDto.getName(), depreciationCreateDto.getDescription(), depreciationCreateDto.getCategoryId());
    }

    /**
     * 创建折旧模板折旧详情
     */
    public int createDepreciationDetail(DepreciationDetailCreateDto depreciationDetailCreateDto) {
        return depreciationMapper.createDepreciationDetail(depreciationDetailCreateDto.getName(), depreciationDetailCreateDto.getDepreciationId(), depreciationDetailCreateDto.getPeriod(), depreciationDetailCreateDto.getMeasure(), depreciationDetailCreateDto.getRate());
    }

    /**
     * 删除折旧模板折旧详情
     */
    public int deleteDetail(int depreciationTemplateId) {
        return depreciationMapper.updateDepreciationTemplate(Depreciation.STATUS_DELETE, depreciationTemplateId);
    }

    /**
     * 查看当前分类绑定的折旧规则及其折旧模板
     */
    public List<DepreciationDetailItem> getDepreciation(int categoryId) throws Exception {

        //验证分类id是否存在绑定
        if (depreciationMapper.findDepreciationCategoryIdExist(categoryId)==0){
            return null;
        }
        //通过分类Id查出折旧规则
        DepreciationItem DepreciationItemList = depreciationMapper.findDepreciationListByCategoryId(categoryId);

        //通过折旧规则Id找出所有的折旧模板
        List<DepreciationDetailItem> depreciationDetailItems =depreciationMapper.findDepreciationDetail(DepreciationItemList.getId());

        return depreciationDetailItems;

    }

    /**
     * 分类和折旧规则绑定
     */
    public int bindingCategory(int categoryId, int depreciationId) throws Exception {
        //验证此分类是否绑定
        if (depreciationMapper.findDepreciationCategoryIdExist(categoryId) != 0) {
            throw new Exception("此分类已存在折旧绑定");
        }
        return depreciationMapper.updateDepreciationCategory(categoryId, depreciationId);
    }

    /**
     * 资产和折旧模板绑定
     */
    public int bindingAsset(int assetId, int depreciationTemplateId) {

        //验证资产id

        //绑定
        //通过折旧模板算折旧后的价格
        DepreciationDetailItem depreciationDetailItem = depreciationMapper.findTemplateById(depreciationTemplateId);
        AssetCapital assetCapital = depreciationMapper.findAssetCapital(assetId);

        BigDecimal num1 = new BigDecimal(depreciationDetailItem.getRate());
        BigDecimal num2 = new BigDecimal(assetCapital.getPrice());
        BigDecimal result = num1.multiply(num2);

        return depreciationMapper.updateDepreciationAsset(assetId, depreciationTemplateId, result);
    }

    /**
     * 模糊搜索折旧规则列表
     */
    public List<DepreciationResponseDto> searchList(DepreciationSearchDto depreciationSearchDto, Pagination pagination) {
        pagination.setTotal(depreciationMapper.searchDepreciationCount(depreciationSearchDto));
        List<DepreciationResponseDto> searchList = depreciationMapper.getSearchList(depreciationSearchDto, pagination);
        return searchList;
    }

    /**
     * 删除折旧规则  depreciation
     */
    public int delete(int id) {
        return depreciationMapper.delete(id, Asset.STATUS_DELETE);
    }

    /**
     * 批量删除折旧规则
     */
    public JsonResult deleteQuery(DepreciationDeleteQueryDto depreciationDeleteQueryDto) {
        depreciationMapper.deleteQuery(depreciationDeleteQueryDto.getIds());
        return new JsonResult();
    }

    /**
     * 折旧规则详情
     */
    public JsonResult<DepreciationResponseDto> detail(int id) {
        return new JsonResult(depreciationMapper.detail(id));
    }

    /**
     * 折旧规则编辑
     */
    public void update(DepreciationUpdateDto depreciationUpdateDto) {
        if (depreciationMapper.findIdByName(depreciationUpdateDto.getName()) != null &&
                Integer.parseInt(depreciationMapper.findIdByName(depreciationUpdateDto.getName())) != depreciationUpdateDto.getId()) {
            return;
        }
        depreciationMapper.update(depreciationUpdateDto);
        depreciationMapper.deleteTemplate(depreciationUpdateDto.getId());
        depreciationMapper.createTemplate(depreciationUpdateDto.getList(), depreciationUpdateDto.getId());
    }

}
