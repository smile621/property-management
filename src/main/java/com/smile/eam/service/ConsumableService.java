package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.*;
import com.smile.eam.mapper.CategoryMapper;
import com.smile.eam.mapper.ConsumableMapper;
import com.smile.eam.mapper.UserMapper;
import com.smile.eam.mapper.VendorMapper;
import com.smile.eam.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsumableService {

    @Resource
    ConsumableMapper consumableMapper;
    @Resource
    VendorMapper vendorMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    UserMapper userMapper;

    @Resource
    DeviceService deviceService;

    /**
     * 耗材记录清单列表
     */
    public List<ConsumableDto> getList(Pagination pagination) {

        List<Consumable> consumables = consumableMapper.findAll(pagination);
        if (consumables.size() == 0) {
            List<ConsumableDto> dto = new ArrayList<>();
            return dto;
        }

        Set<Integer> ids = consumables.stream().map(Consumable::getVendorId).collect(Collectors.toSet());
        List<Vendor> vendorList = vendorMapper.findAllByIds(ids);

        //厂商Map
        Map<Integer, String> vendorMap = vendorList.stream().collect(Collectors.toMap(Vendor::getId, Vendor::getName));

        List<ConsumableDto> consumableDtoList = switchDto(consumables, vendorMap);
        pagination.setTotal(consumableDtoList.size());

        return consumableDtoList;
    }

    /**
     * 转换数据格式 ConsumableDto
     */
    public List<ConsumableDto> switchDto(List<Consumable> consumables, Map<Integer, String> vendorMap) {

        //第一层思维  即使分类被删除了，但是这个分类下的耗材不应该被删除
        //第二层思维  分类被删除的时候，(必然要改变和其有关资产的分类   存在疑惑)
        //分类被删除时  之前绑定在该分类下的资产不变  后续不能绑定在该分类下  因此该处应该是搜索全部的  包括已删除的目录
        Set<Integer> categoryIds = consumables.stream().map(Consumable::getCategoryId).collect(Collectors.toSet());

        //不能在foreach里面使用 and status != 100的形式
        List<Category> categoryList = categoryMapper.findCategoriesByIds(categoryIds);

        //组成目录Map
        Map<Integer, String> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        List<ConsumableDto> consumableList = new ArrayList<>();
        for (Consumable consumable : consumables) {
            ConsumableDto consumableDto = new ConsumableDto();
            BeanUtils.copyProperties(consumable, consumableDto);
            consumableDto.setVendorName(vendorMap.get(consumable.getVendorId()));
            consumableDto.setCategoryName(categoryMap.get(consumable.getCategoryId()));
            consumableList.add(consumableDto);
        }

        return consumableList;
    }

    /**
     * 耗材详情
     */
    public ConsumableDto getConsumableDetail(int id) {

        Consumable consumable = consumableMapper.findConsumableById(id);

        ConsumableDto dto = new ConsumableDto();
        BeanUtils.copyProperties(consumable, dto);

        // 厂商名称
        VendorDto vendorDto = vendorMapper.findOneById(consumable.getVendorId());
        dto.setVendorName(vendorDto.getName());

        // 分类名称
        Category category = categoryMapper.findOneById(consumable.getCategoryId());
        dto.setCategoryName(category.getName());

        return dto;
    }

    /**
     * 耗材新增
     */
    public boolean create(ConsumableCreateDto dto) {

        //如果为null则转为空字符串
        String description = dto.getDescription() == null ? "" : dto.getDescription();
        dto.setDescription(description);
        String specification = dto.getSpecification() == null ? "" : dto.getSpecification();
        dto.setSpecification(specification);

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return 1 == consumableMapper.insertConsumable(dto, time);
    }

    /**
     * 耗材删除
     */
    public String delete(int id) {


        String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int consumableId = consumableMapper.deleteConsumable(id, updatedAt);

        return "SUCCESS";
    }

    /**
     * 耗材编辑
     */
    public boolean edit(ConsumableEditDto dto) {

        String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return 1 == consumableMapper.editConsumable(dto, updatedAt);
    }

    /**
     * 耗材搜索
     */
    public List<ConsumableDto> searchConsumable(Pagination pagination, String name) {

        //所有的厂商,包括已删除的厂商
        List<Vendor> list = vendorMapper.findVendorsByName(name);

        //厂商id列表   Map   耗材
        Set<Integer> vendorIds = list.stream().map(Vendor::getId).collect(Collectors.toSet());
        Map<Integer, String> vendorMap = list.stream().collect(Collectors.toMap(Vendor::getId, Vendor::getName));

        List<Consumable> consumables;

        if (vendorIds.size() == 0) {
            consumables = consumableMapper.searchConsumablesByName(name, pagination);
        } else {
            consumables = consumableMapper.searchByCategoryOrVendorId(pagination, name, vendorIds);
        }

        //搜索为空直接rerun空列表
        if (consumables.size() == 0) {
            List<ConsumableDto> dto = new ArrayList<>();
            return dto;
        }

        List<ConsumableDto> consumableDtoList = switchDto(consumables, vendorMap);
        pagination.setTotal(consumableDtoList.size());

        return consumableDtoList;
    }

    /**
     * 耗材入库
     */
    public boolean intWareHouse(ConsumableIntWareHouseDto dto) {

        Consumable consumable = consumableMapper.findConsumableById(dto.getConsumableId());
        if (consumable == null) {
            return false;
        }

        //必须有新增的样本才能入库
        consumableMapper.insertConsumableTrack(dto);
        consumableMapper.updateNumber(dto.getInNumber() + consumable.getTotal(), dto.getConsumableId());

        return true;
    }

    /**
     * 耗材出库
     */
    public String outWareHouse(ConsumableOutWareHouseDto dto) {

        Consumable consumable = consumableMapper.findConsumableById(dto.getConsumableId());

        //当领用数量大于库存数量，提示库存不足，无法领用
        if (consumable.getTotal() < dto.getOutNumber()) {
            return "NOT_ENOUGH";
        }

        int track = consumableMapper.insertOutTrack(dto);
        consumableMapper.updateNumber(consumable.getTotal() - dto.getOutNumber(), dto.getConsumableId());

        if (track == 0) {
            return "ERROR";
        }
        return "SUCCESS";
    }

    /**
     * 耗材分类列表
     **/
    public List<CategoryTreeDto> getConsumableCategories() {

        //寻找耗材顶级分类
        Category categoryTop = categoryMapper.findTopConsumable();
        List<Category> categories = new ArrayList<>();
        categories.add(categoryTop);

        List<CategoryTreeDto> categoryTreeDtoList = new ArrayList<>();

        //转换格式
        for (Category category : categories) {
            CategoryTreeDto dto = new CategoryTreeDto();
            BeanUtils.copyProperties(category, dto);
            categoryTreeDtoList.add(dto);
        }

        return deviceService.findChildren(categoryTreeDtoList);
    }

    public void findCategory(List<Category> categoryList, List<Category> categories, Pagination pagination) {

        for (Category category : categories) {
            categoryList.add(category);
            List<Category> categories1 = categoryMapper.findAllByCategoryId(category.getId(), pagination);
            findCategory(categoryList, categories1, pagination);
        }
    }

    /**
     * 耗材分类列表  不嵌套
     */
    public List<Category> getConsumableCategoryList(Pagination pagination) {

        List<Category> categoryList = new ArrayList<>();
        List<Category> categories = categoryMapper.findAllByCategoryId(Consumable.CATEGORY_ID_CONSUMABLE, pagination);
        findCategory(categoryList, categories, pagination);

        Category category = categoryMapper.findOneById(Consumable.CATEGORY_ID_CONSUMABLE);
        categoryList.add(0, category);
        pagination.setTotal(categoryList.size());

        return categoryList;
    }

    /**
     * 耗材分类列表 详情显示
     */
    public ConsumableCategoryDto getDetail(int id) {

        Category category = categoryMapper.findOneById(id);
        ConsumableCategoryDto dto = new ConsumableCategoryDto();
        BeanUtils.copyProperties(category, dto);

        //设置父级名称
        dto.setCategoryName(categoryMapper.findOneById(category.getParentId()).getName());
        return dto;
    }

    /**
     * 耗材分类新增
     */
    public boolean addConsumableCategory(CategoryCreateDto dto) {

        return 1 == categoryMapper.insertConsumableCategory(dto);
    }

    /**
     * 耗材分类删除
     */
    public int deleteConsumableCategory(int id) {

        if (id == Consumable.CATEGORY_ID_CONSUMABLE) {
            //最顶级的耗材分类不能删除
            return -1;
        }

        return categoryMapper.deleteCategory(id);
    }

    /**
     * 耗材分类编辑
     */
    public boolean editConsumableCategory(CategoryCreateDto dto, int id) {

        return 1 == categoryMapper.updateCategory(dto, id);
    }

    /**
     * 将用户id和姓名转换成Map格式
     */
    public Map<Integer, String> switchMap(List<ConsumableTrack> tracks) {

        //将领用人入库人出库人Id 转化成用户名
        Set<Integer> ids = tracks.stream().map(ConsumableTrack::getRecipientId).collect(Collectors.toSet());
        Set<Integer> intIds = tracks.stream().map(ConsumableTrack::getInWareHouseUserId).collect(Collectors.toSet());
        Set<Integer> outIds = tracks.stream().map(ConsumableTrack::getOutWareHouseUserId).collect(Collectors.toSet());
        ids.addAll(intIds);
        ids.addAll(outIds);

        List<AdminUser> users = userMapper.findUserListById(ids);
        Map<Integer, String> userMap = users.stream().collect(Collectors.toMap(AdminUser::getId, AdminUser::getUsername));

        return userMap;
    }

    /**
     * 处理格式，返回对应的耗材列表
     */
    public void handleData(List<ConsumableInOutDto> list, List<ConsumableTrack> tracks, Map<Integer, String> userMap) {

        List<Consumable> allConsumable = consumableMapper.findAllConsumable();
        Map<Integer, String> map = allConsumable.stream().collect(Collectors.toMap(Consumable::getId, Consumable::getName));

        for (ConsumableTrack consumableTrack : tracks) {
            ConsumableInOutDto dto = new ConsumableInOutDto();
            BeanUtils.copyProperties(consumableTrack, dto);
            dto.setConsumableName(map.get(consumableTrack.getConsumableId()));
            dto.setRecipientName(userMap.get(consumableTrack.getRecipientId()));
            dto.setInWareHouseUserName(userMap.get(consumableTrack.getInWareHouseUserId()));
            dto.setOutWareHouseUserName(userMap.get(consumableTrack.getOutWareHouseUserId()));
            list.add(dto);
        }
    }

    /**
     * 耗材入库领用记录履历
     */
    public List<ConsumableInOutDto> getConsumableTrack(Pagination pagination) {

        //所有的耗材领用与入库记录
        List<ConsumableTrack> tracks = consumableMapper.findAllTrack(pagination);

        List<ConsumableInOutDto> mapList = new ArrayList<>();
        Map<Integer, String> userMap = switchMap(tracks);

        handleData(mapList, tracks, userMap);
        pagination.setTotal(mapList.size());

        return mapList;
    }

    /**
     * 耗材分类列表 详情显示
     */
    public List<ConsumableInOutDto> getTrackDetail(int id) {

        List<ConsumableTrack> track = consumableMapper.findOneTrack(id);
        List<ConsumableInOutDto> mapList = new ArrayList<>();

        Map<Integer, String> userMap = switchMap(track);
        handleData(mapList, track, userMap);

        return mapList;

    }

    /**
     * 耗材入库领用搜索 根据耗材名称模糊搜索
     */
    public List<ConsumableInOutDto> searchConsumableTrack(String name, Pagination pagination) {

        List<ConsumableInOutDto> mapList = new ArrayList<>();
        List<Consumable> list = consumableMapper.findConsumablesByName(name, pagination);

        if (list.size() == 0) {
            return mapList;
        }

        Set<Integer> ids = list.stream().map(Consumable::getId).collect(Collectors.toSet());
        List<ConsumableTrack> tracks = consumableMapper.searchConsumableTrack(ids);

        Map<Integer, String> userMap = switchMap(tracks);
        handleData(mapList, tracks, userMap);
        pagination.setTotal(mapList.size());

        return mapList;
    }


}
