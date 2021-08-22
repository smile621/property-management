package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.*;
import com.smile.eam.entity.*;
import com.smile.eam.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Resource
    DeviceMapper deviceMapper;
    @Resource
    BuyRouterMapper buyRouterMapper;
    @Resource
    VendorMapper vendorMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    DepreciationMapper depreciationMapper;
    @Resource
    AttributeMapper attributeMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    DeviceAffiliateMapper deviceAffiliateMapper;
    @Resource
    PartMapper partMapper;
    @Resource
    FixedAssetMapper fixedAssetMapper;
    @Resource
    IssueMapper issueMapper;
    @Resource
    ServiceMapper serviceMapper;
    @Resource
    MalfunctionMapper malfunctionMapper;


    /**
     * 将设备厂商、发行方式ID转换成名称并组装dto返回
     */
    public List<DeviceListDto> switchId(Pagination pagination) {

        List<Category> categoryList = new ArrayList<>();

        //将categoryId为1的添加进列表
        Category category = new Category();
        category.setId(1);
        categoryList.add(0, category);
        List<Category> categories = categoryMapper.findAllByCategoryId(Category.CATEGORY_ID_DEVICE, pagination);

        //查询出所有的设备的categoryId
        findCategory(categoryList, categories);

        Set<Integer> ids = categoryList.stream().map(Category::getId).collect(Collectors.toSet());

        //查询所有设备
        List<Device> devices = deviceMapper.findDeviceByCategoryId(ids);

        if (devices.size() == 0) {
            //需要实例一个空列表对象  否则当没有设备时会传给前端null值。
            List<DeviceListDto> dtoList = new ArrayList<>();

            return dtoList;
        }

        //将id转换成名称列表 并且处理dto返回map
        List<DeviceListDto> map = handleId(devices);
        pagination.setTotal(map.size());

        return map;
    }

    public List<DeviceListDto> handleId(List<Device> devices) {

        //将设备列表的ID存放至集合
        Set<Integer> vendorIds = devices.stream().map(device -> device.getVendorId()).collect(Collectors.toSet());

        //根据厂商ID获取厂商信息   并组成MAP<vendorId,vendor>
        //没有厂商存在时返回null
        if (vendorIds.size() == 0) {
            return null;
        }
        List<Vendor> vendors = vendorMapper.findAllByIds(vendorIds);
        Map<Integer, String> vendorMap = vendors.stream().collect(Collectors.toMap(Vendor::getId, Vendor::getName));

        //根据发行方式ID获取发行方式名 并组成Map
        Set<Integer> buyIds = devices.stream().map(device -> device.getBuyRouterId()).collect(Collectors.toSet());
        List<BuyRouter> buyRouters = buyRouterMapper.findBuyRouterListById(buyIds);
        Map<Integer, String> buyRouterMap = buyRouters.stream().collect(Collectors.toMap(BuyRouter::getId, BuyRouter::getName));

        //根据折旧名称ID获取折旧规则名称
        Set<Integer> depreciateIds = devices.stream().map(device -> device.getAssetCategoryId()).collect(Collectors.toSet());
        List<Depreciation> depreciationList = depreciationMapper.findDepreciateByIds(depreciateIds);
        Map<Integer, String> depreciationMap = depreciationList.stream().collect(Collectors.toMap(Depreciation::getId, Depreciation::getName));

        //根据category_id获取分类名称
        Set<Integer> categoryIds = devices.stream().map(Device::getAssetCategoryId).collect(Collectors.toSet());
        List<Category> categoryList = categoryMapper.findAllByIds(categoryIds);
        Map<Integer, String> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        //根据用户id获取用户名   将借用人 归属人都放进去
        Set<Integer> userIds = devices.stream().map(device -> device.getUserId()).collect(Collectors.toSet());
        Set<Integer> lendUserIds = devices.stream().map(Device::getLendUserId).collect(Collectors.toSet());
        userIds.addAll(lendUserIds);
        List<AdminUser> userList = userMapper.findUserListById(userIds);

        Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(AdminUser::getId, AdminUser::getUsername));

        List<Map<Integer, String>> maps = new ArrayList<>();
        maps.add(userMap);
        maps.add(vendorMap);
        maps.add(buyRouterMap);
        maps.add(depreciationMap);
        maps.add(categoryMap);

        List<DeviceListDto> assetDtoList = new ArrayList<>();
        for (Device device : devices) {
            DeviceListDto dto = new DeviceListDto();
            BeanUtils.copyProperties(device, dto);
            dto.setUserName(maps.get(0).get(device.getUserId()));
            dto.setLendUserName(maps.get(0).get(device.getLendUserId()));
            dto.setVendorName(maps.get(1).get(device.getVendorId()));
            dto.setBuyRouterName(maps.get(2).get(device.getBuyRouterId()));
            dto.setDepreciateName(maps.get(3).get(device.getDepreciateId()));
            dto.setAssetCategoryName(maps.get(4).get(device.getAssetCategoryId()));
            assetDtoList.add(dto);
        }

        return assetDtoList;
    }

    /**
     * 设备详情
     */
    public DeviceListDto getDeviceDetail(int id) {

        //之前封装转换id的方法是一个列表
        List<Device> devices = new ArrayList<>();
        Device device = deviceMapper.findDeviceById(id);
        devices.add(device);

        List<DeviceListDto> dtoList = handleId(devices);
        DeviceListDto dto = dtoList.get(0);

        return dto;
    }


    /**
     * 增加设备
     */
    public boolean createDevice(DeviceCreateDto deviceCreateDto) {

        deviceCreateDto.setAssetCategoryId(FixedAsset.CATEGORY_ID_DEVICE);

        return 1 == deviceMapper.insertDevice(deviceCreateDto);
    }

    /**
     * 删除设备
     */
    public boolean deleteDevice(int id) {

        int rows = deviceMapper.deleteDevice(id);
        List<DeviceAttributePartDto> attributePart = getAttributePart(id);
        Set<Integer> partIds = attributePart.stream().map(DeviceAttributePartDto::getId).collect(Collectors.toSet());

        //归属在该设备下的配件软件服务的故障以及记录asset_affiliate都要删掉
        if (partIds.size() != 0) {
            malfunctionMapper.deleteAttributeMalfunction(partIds);
            deviceAffiliateMapper.deleteAttributeAffiliate(partIds);
        }
        List<DeviceAttributeSoftWareDto> attributeSoftWare = getAttributeSoftWare(id);
        Set<Integer> softWareIds = attributeSoftWare.stream().map(DeviceAttributeSoftWareDto::getId).collect(Collectors.toSet());

        if (softWareIds.size() != 0) {
            malfunctionMapper.deleteAttributeMalfunction(softWareIds);
            deviceAffiliateMapper.deleteAttributeAffiliate(softWareIds);
        }
        List<DeviceAttributeServiceDto> attributeService = getAttributeService(id);
        Set<Integer> serviceIds = attributeService.stream().map(DeviceAttributeServiceDto::getId).collect(Collectors.toSet());

        if (serviceIds.size() != 0) {
            malfunctionMapper.deleteAttributeMalfunction(serviceIds);
            deviceAffiliateMapper.deleteAttributeAffiliate(serviceIds);
        }
        if (rows == 0) {
            return false;
        }
        return true;
    }

    /**
     * 编辑设备
     */
    public boolean editDevice(DeviceUpdateDto deviceUpdateDto) {

        String updatedAAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return 1 == deviceMapper.editDevice(deviceUpdateDto, updatedAAt);
    }

    /**
     * 模糊搜索设备
     */
    public List<DeviceListDto> searchDevice(String name, Pagination pagination) {

        List<DeviceListDto> dtoList;

        //传进来为空字符串
        //str==""  和  str.equals("") 都是true
        if (name == "") {
            dtoList = switchId(pagination);
            pagination.setTotal(dtoList.size());
            return dtoList;
        }

        //查询出所搜的设备的目录
        List<Category> categories = showCategories(pagination);
        Map<Integer, String> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        //名称和描述搜索
        DeviceSearchDto dto = DeviceSearchDto.builder().name(name).description(name).build();
        //使用名称和描述去模糊查询设备
        List<Device> devices = deviceMapper.searchDevice(dto);

        //组成integer,device 的Map
        Map<Integer, Device> dtoMap = devices.stream().collect(Collectors.toMap(Device::getId, item -> item));

        //搜索厂商id  并去重
        List<Vendor> vendors = deviceMapper.searchVendorIdsByName(name);
        Set<Integer> vendorIds = vendors.stream().map(Vendor::getId).collect(Collectors.toSet());

        //符合条件的厂商
        if (vendorIds.size() == 0) {
            //过滤不是设备的资产  厂商为空时
            List<Device> list = devices.stream().filter((item) -> {
                return categoryMap.get(item.getAssetCategoryId()) != null;
            }).collect(Collectors.toList());
            //没有符合条件的设备
            if(list.size()==0){
                pagination.setTotal(list.size());
                return new ArrayList<>();
            }

            dtoList = handleId(list);
            return dtoList;
        }

        List<Device> deviceList = deviceMapper.searchLikeVendor(vendorIds);

        //将通过厂商查出来的设备添加到设备列表里面
        for (Device device : deviceList) {
            //该处不是deviceMap而是dtoMap
            if (dtoMap.get(device.getId()) == null) {
                devices.add(device);
            }
        }

        //过滤不是设备的资产
        List<Device> list = devices.stream().filter((item) -> {
            return categoryMap.get(item.getAssetCategoryId()) != null;
        }).collect(Collectors.toList());
        dtoList = handleId(list);
        pagination.setTotal(dtoList.size());

        return dtoList;
    }

    /**
     * 设备归属给用户
     */
    public boolean deviceBindUser(DeviceBindUserDto dto) {

        String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        AssetTrack assetTrack = attributeMapper.findFrontAttributeById(dto.getId());

        //当该设备之前已归属时，归属新设备时解除之前的归属关系
        if (assetTrack != null) {
            //直接删除  之前的归属记录
            attributeMapper.removeAttribute(updatedAt, dto.getId());
        }

        int rows = attributeMapper.insertBindUser(dto, updatedAt);
        if (rows == 0) {
            return false;
        }

        //在资产记录表里插入后再更新资产表里设备的状态和归属信息。
        attributeMapper.updateDeviceBindUser(dto.getId(), dto.getUserId(), updatedAt);
        return true;
    }

    /**
     * 设备被借用
     */
    public boolean lendDevice(DeviceLendDto dto) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        AssetTrack assetTrack = attributeMapper.findFrontAttributeById(dto.getId());
        //当借用之前已经被归属或者借用时   直接解除之前的归属或借用
        if (assetTrack != null) {
            attributeMapper.removeAttribute(time, dto.getId());
        }
        int rows = attributeMapper.insertLendUser(dto, time);
        if (rows == 0) {
            return false;
        }
        int res = attributeMapper.updateDeviceLendUser(dto.getId(), dto.getLendUserId(), time);
        if (res == 0) {
            return false;
        }
        return true;
    }

    /**
     * 设备分类列表   树形结构
     */
    public List<CategoryTreeDto> showCategory() {

        //耗材顶级分类
        List<Category> categories = deviceMapper.findAllCategories(Category.CATEGORY_ID_DEVICE);

        List<CategoryTreeDto> categoryTreeDtoList = new ArrayList<>();
        for (Category category : categories) {
            CategoryTreeDto categoryTreeDto = new CategoryTreeDto();
            BeanUtils.copyProperties(category, categoryTreeDto);
            categoryTreeDtoList.add(categoryTreeDto);
        }
        List<CategoryTreeDto> list = findChildren(categoryTreeDtoList);

        return list;
    }

    public List<CategoryTreeDto> findChildren(List<CategoryTreeDto> categories) {

        List<CategoryTreeDto> categoryList = new ArrayList<>();

        for (CategoryTreeDto category : categories) {
            category.setTitle(category.getName());
            category.setValue(category.getId());
            categoryList.add(category);
            List<Category> list = categoryMapper.findAllByCategoryIds(category.getId());
            List<CategoryTreeDto> dtoList = new ArrayList<>();
            for (Category dto : list) {
                CategoryTreeDto categoryTreeDto = new CategoryTreeDto();
                BeanUtils.copyProperties(dto, categoryTreeDto);
                categoryTreeDto.setTitle(dto.getName());
                categoryTreeDto.setValue(dto.getId());
                dtoList.add(categoryTreeDto);
            }
            category.getChildren().addAll(dtoList);
            findChildren(dtoList);
        }

        return categoryList;
    }

    public void findCategory(List<Category> categoryList, List<Category> categories) {

        for (Category category : categories) {
            categoryList.add(category);
            List<Category> categories1 = categoryMapper.findAllByCategoryIds(category.getId());
            findCategory(categoryList, categories1);
        }
    }

    /**
     * 设备分类列表  不嵌套
     */
    public List<Category> showCategories(Pagination pagination) {

        List<Category> categoryList = new ArrayList<>();
        List<Category> categories = categoryMapper.findAllByCategoryId(Category.CATEGORY_ID_DEVICE, pagination);
        findCategory(categoryList, categories);

        Category category = categoryMapper.findOneById(Category.CATEGORY_ID_DEVICE);
        categoryList.add(0, category);
        pagination.setTotal(categoryList.size());

        return categoryList;
    }

    /**
     * 设备分类详情
     */
    public DeviceCategoryDetailDto getCategoryDetail(int id) {

        DeviceCategoryDetailDto dto = new DeviceCategoryDetailDto();
        Category category = categoryMapper.findOneById(id);
        if (category == null) {
            throw new RuntimeException("设备分类不存在");
        }
        Depreciation depreciate = depreciationMapper.findDepreciateByCategoryId(id);
        dto.setDepreciateName(depreciate == null ? null : depreciate.getName());
        BeanUtils.copyProperties(category, dto);

        String name = categoryMapper.findOneById(category.getParentId()).getName();
        dto.setAssetCategoryName(name);

        return dto;
    }

    /**
     * 设备分类搜索
     */
    public List<Category> searchCategories(String name, Pagination pagination) {

        List<Category> list = categoryMapper.searchCategories(name, pagination);
        pagination.setTotal(list.size());

        return list;
    }

    /**
     * 设备列表删除
     */
    public boolean deleteDeviceCategory(int categoryId) {
        int rows = categoryMapper.deleteDeviceCategory(categoryId);
        if (rows == 0) {
            return false;
        }
        return true;
    }

    /**
     * 设备目录编辑
     */
    public boolean editDeviceCategory(DeviceCategoryUpdateDto dto) {

        //dto.getDepreciateId()为折旧模板id(depreciate_template表)
        String updatedAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rows = categoryMapper.updateDeviceCategory(dto, updatedAt);
        if (rows == 0) {
            return false;
        }

        //获取折旧项目表depreciate中的asset_category_id   item.getAssetCategoryId()
        DepreciationItem item = depreciationMapper.findDepreciation(dto.getId());

        //查询获取category对象，通过id查询得到设备的折旧模板id(depreciate_id)  device.getDepreciateId()
        Device device = deviceMapper.findDeviceById(dto.getId());
        DepreciationDetailItem template = depreciationMapper.findDepreciateTemplateById(device.getDepreciateId());

        //template如果为null时则需要在depreciate_template插入数据
        if (template == null) {
            //没有折旧模板就插入默认的折旧模板  折旧比率1，周期1年一次
            depreciationMapper.insertDepreciationTemplate(dto.getDepreciateId());
        }

        //折旧不为null时则更新折旧模板的depreciate_id字段  dto.getDepreciateId()
        depreciationMapper.updateDepreciate(dto.getDepreciateId(), dto.getId());

        return true;
    }

    /**
     * 设备类别新增
     */
    public boolean addDeviceCategory(DeviceCategoryUpdateDto dto) {

        int rows = categoryMapper.insertCategory(dto);

        //depreciateId 为0  直接return
        if (dto.getDepreciateId() == 0) {
            return true;
        }
        DepreciationDetailItem depreciation = depreciationMapper.findDepreciateTemplateById(dto.getDepreciateId());
        depreciationMapper.insertDepreciationTemplate(depreciation.getDepreciationId());

        return true;
    }

    /**
     * 设备解除归属   修改fixed_asset_track和fixed_asset表的字段
     */
    public boolean dismissDevice(int id) {

        int rows = attributeMapper.returnAttribute(id);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        attributeMapper.removeAttribute(date, id);

        if (rows == 0) {
            return false;
        }
        return true;
    }

    /**
     * 设备归还
     */
    public boolean returnDevice(int id, String description) {

        int rows = attributeMapper.lendOverDevice(id);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        attributeMapper.lendBackDevice(time, description, id);

        if (rows == 0) {
            return false;
        }
        return true;
    }

    /**
     * 设备归属列表
     */
    public List<AssetTrackDto> getAttributeList() {

        List<AssetTrack> attributeList = attributeMapper.findListByStatus();
        List<AssetTrackDto> assetTrackDtoList = new ArrayList<>();

        //将设备归属人和借用人存至一个列表中
        Set<Integer> userIds = new HashSet<>();
        for (AssetTrack assetTrack : attributeList) {

            AssetTrackDto assetTrackDto = new AssetTrackDto();
            BeanUtils.copyProperties(assetTrack, assetTrackDto);
            assetTrackDtoList.add(assetTrackDto);

            if (assetTrack.getUserId() == 0) {
                userIds.add(assetTrack.getLendUserId());
            }
            if (assetTrack.getLendUserId() == 0) {
                userIds.add(assetTrack.getUserId());
            }
        }

        //设备ID
        Set<Integer> deviceIds = attributeList.stream().map(AssetTrack::getFixedAssetId).collect(Collectors.toSet());

        //所有的设备
        List<Device> devices = deviceMapper.findDevicesByIds(deviceIds);
        Map<Integer, String> nameMap = devices.stream().collect(Collectors.toMap(Device::getId, Device::getName));

        //查询出搜有的user并组成id和name的Map
        List<AdminUser> userList = userMapper.findUserListById(userIds);
        Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(AdminUser::getId, AdminUser::getUsername));

        List<AssetTrackDto> dtoList = new ArrayList<>();
        for (AssetTrack dto : attributeList) {

            AssetTrackDto assetTrackDto = new AssetTrackDto();
            BeanUtils.copyProperties(dto, assetTrackDto);
            assetTrackDto.setName(nameMap.get(dto.getFixedAssetId()));

            //归属人不存在  则借用人一定存在
            if (dto.getUserId() == 0) {
                assetTrackDto.setUserName(userMap.get(dto.getLendUserId()));
            }
            if (dto.getLendUserId() == 0) {
                assetTrackDto.setUserName(userMap.get(dto.getUserId()));
            }
            dtoList.add(assetTrackDto);
        }

        return dtoList;
    }

    /**
     * 设备统计
     */
    public List<DeviceStatisticDto> getTotalPrice(int year, Pagination pagination) {

        List<DeviceStatisticDto> dtoList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        List<Category> categories = categoryMapper.findAllByCategoryId(Category.CATEGORY_ID_DEVICE, pagination);
        findCategory(categoryList, categories);

        //categoryId为1的设备
        Category category = new Category();
        category.setId(1);
        categoryList.add(0, category);
        List<Integer> ids = categoryList.stream().map(Category::getId).collect(Collectors.toList());

        List<Device> list = deviceMapper.statisticDeviceByCategoryId(ids);

        //得到当年和下一年的时间毫秒数
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, 0, 1);
        long millis = calendar.getTimeInMillis();

        Calendar calendarLast = Calendar.getInstance();
        calendarLast.clear();
        calendarLast.set(year + 1, 0, 0);
        long millisLast = calendarLast.getTimeInMillis();

        List<Device> devices = list.stream().filter((item) -> {
            return item.getExpiredAt().getTime() - new Date().getTime() >= 0
                    && item.getCreatedAt().getTime() >= millis
                    && item.getCreatedAt().getTime() < millisLast;
        }).collect(Collectors.toList());

        for (Device device : devices) {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateNow = formatDate.format(device.getCreatedAt());
            String month = dateNow.substring(5, 7);

            DeviceStatisticDto dto = DeviceStatisticDto
                    .builder().totalPrice(device.getPrice())
                    .month(month)
                    .depreciateId(device.getDepreciateId()).build();
            dtoList.add(dto);
        }
        pagination.setTotal(dtoList.size());

        return dtoList;
    }

    /**
     * 配件归属设备
     */
    public List<DeviceAttributePartDto> getAttributePart(int deviceId) {

        List<DeviceAttributePartDto> dtoList = new ArrayList<>();
        List<DeviceAffiliate> affiliate = deviceAffiliateMapper.findDeviceAffiliateById(deviceId);

        //没有归属记录
        if (affiliate.size() == 0) {
            return dtoList;
        }
        Set<Integer> assetIds = affiliate.stream().map(DeviceAffiliate::getAssetId).collect(Collectors.toSet());
        List<Part> parts = partMapper.findPartById(assetIds, Asset.STATUS_DELETE);

        //厂商ids   组成vendorMap
        Set<Integer> vendorIds = parts.stream().map(Part::getVendorId).collect(Collectors.toSet());
        List<Vendor> vendors = vendorMapper.findVendorById(vendorIds);
        Map<Integer, String> vendorMap = vendors.stream().collect(Collectors.toMap(Vendor::getId, Vendor::getName));

        //分类ids  组成assetCategoryMap
        Set<Integer> categoryIds = parts.stream().map(Part::getAssetCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryMapper.findCategoryByIds(categoryIds, 2);
        Map<Integer, String> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        //过滤不是配件的归属
        List<Part> partList = parts.stream().filter((item) -> {
            return categoryMap.get(item.getAssetCategoryId()) != null;
        }).collect(Collectors.toList());

        //将数据转换成返回的数据格式
        for (Part dto : partList) {
            DeviceAttributePartDto partDto = new DeviceAttributePartDto();
            partDto.setId(dto.getId());
            partDto.setBuyAt(dto.getBuyAt());
            partDto.setPrice(dto.getPrice());
            partDto.setName(dto.getName());
            partDto.setVendorName(vendorMap.get(dto.getVendorId()));
            partDto.setAssetCategoryName(categoryMap.get(dto.getAssetCategoryId()));
            dtoList.add(partDto);
        }

        return dtoList;
    }

    /**
     * 软件归属设备
     */
    public List<DeviceAttributeSoftWareDto> getAttributeSoftWare(int deviceId) {
        List<DeviceAttributeSoftWareDto> dtoList = new ArrayList<>();
        //软件的flag为3
        List<DeviceAffiliate> affiliate = deviceAffiliateMapper.findDeviceAffiliateById(deviceId);
        Set<Integer> assetIds = affiliate.stream().map(DeviceAffiliate::getAssetId).collect(Collectors.toSet());
        if (affiliate.size() == 0) {
            return dtoList;
        }
        List<FixedAsset> assetList = fixedAssetMapper.findFixedAssetById(assetIds);

        //厂商IDS   组成vendorMap
        Set<Integer> issueIds = assetList.stream().map(FixedAsset::getIssueId).collect(Collectors.toSet());
        List<Issue> issues = issueMapper.findIssueNameById(issueIds);
        Map<Integer, String> issueMap = issues.stream().collect(Collectors.toMap(Issue::getId, Issue::getName));

        //分类ids  组成assetCategoryMap
        Set<Integer> categoryIds = assetList.stream().map(FixedAsset::getAssetCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryMapper.findCategoryByIds(categoryIds, 3);
        Map<Integer, String> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        //过滤不是软件的归属
        List<FixedAsset> softWareList = assetList.stream().filter((item) -> {
            return categoryMap.get(item.getAssetCategoryId()) != null;
        }).collect(Collectors.toList());

        //将数据转换成返回的数据格式
        for (FixedAsset dto : softWareList) {
            DeviceAttributeSoftWareDto softWareDto = new DeviceAttributeSoftWareDto();
            softWareDto.setId(dto.getId());
            softWareDto.setName(dto.getName());
            softWareDto.setEndAt(dto.getEndAt());
            softWareDto.setVersion(dto.getVersion());
            softWareDto.setIssueName(issueMap.get(dto.getIssueId()));
            softWareDto.setAssetCategoryName(categoryMap.get(dto.getAssetCategoryId()));
            dtoList.add(softWareDto);
        }
        return dtoList;
    }

    /**
     * 服务归属设备
     */
    public List<DeviceAttributeServiceDto> getAttributeService(int deviceId) {

        List<DeviceAttributeServiceDto> dtoList = new ArrayList<>();
        //服务的flag为4
        List<DeviceAffiliate> affiliate = serviceMapper.findServiceAffiliateById(deviceId);
        Set<Integer> assetIds = affiliate.stream().map(DeviceAffiliate::getAssetId).collect(Collectors.toSet());
        if (affiliate.size() == 0) {
            return dtoList;
        }
        List<FixedAsset> assetList = fixedAssetMapper.findFixedAssetById(assetIds);

        //分类ids  组成assetCategoryMap
        Set<Integer> categoryIds = assetList.stream().map(FixedAsset::getAssetCategoryId).collect(Collectors.toSet());
        List<Category> categories = categoryMapper.findCategoryByIds(categoryIds, 4);
        Map<Integer, String> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        //过滤不是软件的归属
        List<FixedAsset> serviceList = assetList.stream().filter((item) -> {
            return categoryMap.get(item.getAssetCategoryId()) != null;
        }).collect(Collectors.toList());

        //将数据转换成返回的数据格式
        for (FixedAsset dto : serviceList) {
            DeviceAttributeServiceDto serviceDto = new DeviceAttributeServiceDto();
            serviceDto.setId(dto.getId());
            serviceDto.setName(dto.getName());
            serviceDto.setDescription(dto.getDescription());
            dtoList.add(serviceDto);
        }
        return dtoList;
    }

    /**
     * 设备报告故障
     */
    public String sendMalfunction(DeviceMalfunctionRequest request) {

        //判断该设备之前是否发生故障
        Device device = deviceMapper.findMalfunctionStatus(request.getFixedAssetId());

        if (device != null) {
            return "REPEAT";
        }

        int row = malfunctionMapper.sendDeviceMalfunction(request);
        malfunctionMapper.updateMalStatus(request.getFixedAssetId());
        if (row == 0) {
            return "ERROR";
        }

        return "SUCCESS";
    }

}
