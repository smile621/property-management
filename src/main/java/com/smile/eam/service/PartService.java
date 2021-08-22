package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.common.UserContext;
import com.smile.eam.dto.*;
import com.smile.eam.entity.*;
import com.smile.eam.mapper.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartService {

    @Resource
    PartMapper partMapper;

    @Resource
    VendorMapper vendorMapper;

    @Resource
    BuyRouterMapper buyRouterMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    VerifyMapper verifyMapper;

    @Resource
    DepreciationMapper depreciationMapper;

    @Resource
    permissionsService permissionsService;

    /**
     *创建配件Part
     */
    public String createPart( PartCreateDto partCreateDto) throws ParseException {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //验证厂商id是否存在
        if (verifyMapper.findVendorById(partCreateDto.getVendorId()) == null) {
            throw new RuntimeException("无此厂商");
        }

        //验证购买路径是否存在
        if (verifyMapper.findBuyRouterById(partCreateDto.getBuyRouterId()) == null) {
            throw new RuntimeException("无此购买路径");
        }

        //验证分类id是否存在
        if (verifyMapper.findCategoryById(partCreateDto.getAssetCategoryId()) == null) {
            throw new RuntimeException("无此分类");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date buyAt = simpleDateFormat.parse(partCreateDto.getBuyAt());
        Date expiredAt = simpleDateFormat.parse(partCreateDto.getExpiredAt());
        Date date = new Date();

        //验证购入时间是否小于当前时间
        if (buyAt.getTime() - date.getTime() > 0){
            throw new RuntimeException("购入时间大于当前时间");
        }

        //验证购入时间是否小于过保时间
        if (buyAt.getTime() - expiredAt.getTime() > 0){
            throw new RuntimeException("购入时间大于过保时间");
        }

        return String.valueOf(partMapper.create(partCreateDto, Asset.CATEGORY_ID_PART));
    }

    /**
     *查询配件Part列表
     */
    public List<PartReturnDto> findPartList(PartSearchDto partSearchDto, Pagination pagination)  {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //查看当前用户查找匹配到的设备
        Set<Part> searchPartList = new HashSet<>();

        if (partSearchDto.getTitle() != null && !partSearchDto.getTitle().equals("")) {

            //查看厂商表中有无类似数据
            List<Part> vendorPartList = new ArrayList<>();
            List<Integer> vendorIds = vendorMapper.findVendorIdVague(partSearchDto.getTitle());
            //通过厂商id列表找到对应的List<Part>
            if (vendorIds.size() != 0) {
                vendorPartList = vendorMapper.findPartsByVendorIds(vendorIds);
                if (vendorPartList.size()!=0){
                    for (Part part : vendorPartList) {
                        searchPartList.add(part);
                    }
                }
            }

            //查看购入途径中有无类似数据
            List<Part> buyRouterPartList = new ArrayList<>();
            List<Integer> buyRouterIds = buyRouterMapper.findBuyRouterIdVague(partSearchDto.getTitle());
            //通过购入途径id列表找到对应的List<Part>
            if (buyRouterIds.size() != 0) {
                buyRouterPartList = buyRouterMapper.findPartsByBuyRouterIds(buyRouterIds);
                if (buyRouterPartList.size()!=0){
                    for (Part part : buyRouterPartList) {
                        searchPartList.add(part);
                    }
                }
            }

            //查看分类中有无类似数据
            List<Part> categoryPartList = new ArrayList<>();
            List<Integer> categoryIds = partMapper.findPartCategoryVague(partSearchDto.getTitle());
            if (categoryIds.size() !=0 ){
                categoryPartList = partMapper.findCategoryList(categoryIds);
                if (categoryPartList.size() !=0 ){
                    for (Part part : categoryPartList) {
                        searchPartList.add(part);
                    }
                }
            }
        }

        List<Part> partLists = partMapper.findParts(partSearchDto, pagination);
        for (Part partList : partLists) {
            searchPartList.add(partList);
        }
        if (searchPartList.size() == 0){
            return null;
        }

        //组装状态Map
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(Asset.STATUS_NORMAL, "正常");
        statusMap.put(Asset.STATUS_LEISURE, "闲置");
        statusMap.put(Asset.STATUS_MALFUNCTION, "故障");
        statusMap.put(Asset.STATUS_BEING_PROCESSED, "处理中");
        statusMap.put(Asset.STATUS_AFFILIATION, "归属");

        //组装故障状态Map
        Map<Integer, String> malfunctionStatus = new HashMap<>();
        malfunctionStatus.put(Asset.STATUS_NORMAL, "正常");
        malfunctionStatus.put(Asset.STATUS_MALFUNCTION, "故障");
        malfunctionStatus.put(Asset.STATUS_BEING_PROCESSED, "处理中");

        //组装折旧模板Map
        Map<Integer, String> depreciationMap = new HashMap<>();
        List<DepreciationDetailItem> depreciationDetailItems = depreciationMapper.findTemplateAll(Asset.STATUS_DELETE);
        for (DepreciationDetailItem depreciationDetailItem : depreciationDetailItems) {
            depreciationMap.put(depreciationDetailItem.getId(), depreciationDetailItem.getTemplateName());
        }

        //组装归属设备Map
        Set<Integer> deviceIds = new HashSet<>();
        for (Part part : searchPartList) {
            deviceIds.add(part.getDeviceId());
        }

        List<Part> deviceList = partMapper.findPartById(deviceIds, Asset.STATUS_DELETE);
        Map<Integer, String> deviceMap = new HashMap<>();
        for (Part part : deviceList) {
            deviceMap.put(part.getId(), part.getName());
        }

        //通过厂商ID组装厂商Map
        Set<Integer> vendorIdList = searchPartList.stream().map((item) -> {
            return item.getVendorId();
        }).collect(Collectors.toSet());
        List<Vendor> vendorList = vendorMapper.findVendorById(vendorIdList);
        Map<Integer, String> vendorMap = vendorList.stream().collect(Collectors.toMap(Vendor::getId, Vendor::getName));

        //通过购入途径ID组装购入途径Map
        Set<Integer> buyRouter = searchPartList.stream().map((item) -> {
            return item.getBuyRouterId();
        }).collect(Collectors.toSet());
        List<BuyRouter> buyRouterList = buyRouterMapper.findBuyRouterById(buyRouter);
        Map<Integer, String> buyRouterMap = buyRouterList.stream().collect(Collectors.toMap(BuyRouter::getId, BuyRouter::getName));

        //通过分类ID组装分类Map
        Set<Integer> categoryIdSet = searchPartList.stream().map((item) -> {
            return item.getAssetCategoryId();
        }).collect(Collectors.toSet());
        List<Category> categoryList = categoryMapper.findCategoriesByIds(categoryIdSet);
        Map<Integer, String> categoryMap = categoryList.stream().collect(Collectors.toMap(Category::getId, Category::getName));
        Map<Integer, Integer> categoryIdParentMap = new HashMap<>();
        List<categoryParentDto> categoryParents = categoryMapper.findIdParentId();
        for (categoryParentDto categoryParent : categoryParents) {
            categoryIdParentMap.put(categoryParent.getId(), categoryParent.getParentId());
        }

        Map<Integer, List<Integer>> categoryListMap = new HashMap<>();
        for (Part part : searchPartList) {
            List<Integer> categoryIdListItem = new ArrayList<>();
            //通过分类id查询父类id
            categoryIdListItem.add(part.getAssetCategoryId());
            finCategoryParentMap(categoryIdListItem, part.getAssetCategoryId());
            Collections.reverse(categoryIdListItem);
            categoryIdListItem.remove(0);
            categoryListMap.put(part.getId(), categoryIdListItem);
        }

        //组装dto
        List<PartReturnDto> partReturnDtoList = new ArrayList<>();

        //排序
        List<Part> partItem = searchPartList.stream().sorted((a,b) -> {
            return a.getId() < b.getId() ? 1 : -1 ;
        }).collect(Collectors.toList());

        for (Part part : partItem) {
            PartReturnDto partReturnDto = new PartReturnDto();
            partReturnDto.setVendor(vendorMap.get(part.getVendorId()));
            partReturnDto.setBuyRouter(buyRouterMap.get(part.getBuyRouterId()));
            partReturnDto.setAssetCategory(categoryMap.get(part.getAssetCategoryId()));
            partReturnDto.setAssetId(part.getId());
            partReturnDto.setAssetCategoryId(part.getAssetCategoryId());
            partReturnDto.setVendorId(part.getVendorId());
            partReturnDto.setBuyRouterId(part.getBuyRouterId());
            partReturnDto.setAvatar(part.getAvatar());
            partReturnDto.setDescription(part.getDescription());
            partReturnDto.setName(part.getName());
            if (part.getCreatedAt()!=null){
                partReturnDto.setCreateAt(DateFormat.getDateInstance().format(part.getCreatedAt()));
            }
            if (part.getEndAt()!=null){
                partReturnDto.setEndAt(DateFormat.getDateInstance().format(part.getEndAt()));
            }
           if (part.getBuyAt()!=null){
               partReturnDto.setBuyAt(DateFormat.getDateInstance().format(part.getBuyAt()));
           }
           if (part.getStartAt()!=null){
               partReturnDto.setStartAt(DateFormat.getDateInstance().format(part.getStartAt()));
           }
           if (part.getEndAt()!=null){
               partReturnDto.setEndAt(DateFormat.getDateInstance().format(part.getEndAt()));
           }
           if (part.getUpdatedAt()!=null){
               partReturnDto.setUpdateAt(DateFormat.getDateInstance().format(part.getUpdatedAt()));
           }
            partReturnDto.setPrice(part.getPrice());
            partReturnDto.setOutPrice(part.getOutPrice());
            partReturnDto.setDepreciation(depreciationMap.get(part.getDepreciateId()));
            partReturnDto.setMalfunctionStatus(malfunctionStatus.get(part.getMalfunctionStatus()));
            partReturnDto.setAffiliationDevice(deviceMap.get(part.getDeviceId()));
            partReturnDto.setStatus(statusMap.get(part.getStatus()));
            partReturnDto.setExpiredAt(DateFormat.getDateInstance().format(part.getExpiredAt()));
            partReturnDto.setCateList(categoryListMap.get(part.getId()));
            partReturnDtoList.add(partReturnDto);
        }
        return partReturnDtoList;


    }

    /**
     *删除配件Part
     */
    public int deletePart(int fixedAssetId) {

        //验证是否有此配件资产
        if (partMapper.findDeletePartExist(fixedAssetId) == 0){
            throw new RuntimeException("无此配件id");
        }

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //删除归属表中记录
        partMapper.updateAssetAffiliate(Asset.STATUS_DELETE, fixedAssetId);

        //将归属记录表中最新的记录改为删除
        partMapper.updateTrackStatus(fixedAssetId, Asset.STATUS_DELETE, new Date());

        //删除故障表中的记录
        partMapper.updateMalfunction(Asset.STATUS_DELETE, fixedAssetId);

        List<Integer> fixedAssetIdList = new ArrayList<>();
        fixedAssetIdList.add(fixedAssetId);

        return partMapper.deletePart(fixedAssetIdList, Asset.STATUS_DELETE);
    }

    /**
     * 配件Part更新
     */
    public int updatePart(PartUpdateDto partUpdateDto) throws ParseException {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //验证时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date buyAt = simpleDateFormat.parse(partUpdateDto.getBuyAt());
        Date expiredAt = simpleDateFormat.parse(partUpdateDto.getExpiredAt());
        Date date = new Date();

        //验证购入时间是否小于当前时间
        if (buyAt.getTime() - date.getTime() > 0){
            throw new RuntimeException("购入时间大于当前时间");
        }

        //验证购入时间是否小于过保时间
        if (buyAt.getTime() - expiredAt.getTime() > 0){
            throw new RuntimeException("购入时间大于过保时间");
        }

        //验证厂商id是否存在
        if (verifyMapper.findVendorById(partUpdateDto.getVendorId()) == null) {
            throw new RuntimeException("无此厂商编号");
        }
        //验证购买路径是否存在
        if (verifyMapper.findBuyRouterById(partUpdateDto.getBuyRouterId()) == null) {
            throw new RuntimeException("无此购买路径编号");
        }
        //验证分类id是否存在
        if (verifyMapper.findCategoryById(partUpdateDto.getAssetCategoryId()) == null) {
            throw new RuntimeException("无此分类编号");
        }
        return partMapper.updatePart(partUpdateDto);
    }

    /**
     * 获取PartCategoryList配件分类列表
     */
    public Map<Integer, List<Category>> findCategoryList() {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //新建一个List，把递归之后的每一个category存到里面
        List<Category> categoryList = new ArrayList<>();

        //查配件分类下的第一级
        List<Category> categoryListFirstSon = categoryMapper.findCategorySonList(Part.CATEGORY_ID_PART);

        //调用递归
        findCategorySonMap(categoryList, categoryListFirstSon);

        //stream流进行map分类
        Map<Integer, List<Category>> listMap = categoryList.stream().collect(Collectors.groupingBy(Category::getParentId));

        return listMap;
    }

    /**
     * 获取配件新建分类
     */
    public List<Object> findCategoryListHome() {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //查出配件下面所有的分类
        //新建一个List，把递归之后的每一个category存到里面
        List<Category> categoryList = new ArrayList<>();

        //查配件分类下的第一级
        List<Category> categoryListFirstSon = categoryMapper.findCategorySonList(Asset.CATEGORY_ID_PART);

        //调用递归
        findCategorySonMap(categoryList, categoryListFirstSon);

        List<Map> queryList = new ArrayList<>();
        for (Category category : categoryList) {
            Map<String, String> item = getQueryMap(String.valueOf(category.getId()), category.getName(), String.valueOf(category.getParentId()));
            queryList.add(item);
        }

        return getResult(queryList);

    }

    /**
     * 获取配件分类列表
     */
    public List<Object> findCategoryListCategory() {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //查出配件下面所有的分类
        //新建一个List，把递归之后的每一个category存到里面
        List<Category> categoryList = new ArrayList<>();

        //查配件分类下的第一级
        List<Category> categoryListFirstSon = categoryMapper.findCategorySonList(Asset.CATEGORY_ID_PART);

        //调用递归
        findCategorySonMap(categoryList, categoryListFirstSon);

        List<Map> queryList = new ArrayList<>();
        for (Category category : categoryList) {
            Map<String, String> item = getCategoryExampleMap(String.valueOf(category.getId()), category.getName(), String.valueOf(category.getParentId()));
            queryList.add(item);
        }
        return getCategoryResult(queryList);
    }

    public static List<Object> getCategoryResult(List<Map> mapList) {

        List<Map> queryList = mapList;
        List<Object> resultList = new ArrayList<>();

        //为每一个节点添加孩子字段
        for (Map map : queryList) {
            List<Map> childList = new ArrayList<>(); //[]
            map.put("children", childList);
        }
        List<Map> itemList = new ArrayList<>();
        for (Map map : queryList) {
            for (Map childMap : queryList) {
                if (childMap.get("id").toString().equals(map.get("key").toString())) {
                    List<Map> arrList = (ArrayList<Map>) map.get("children"); // [] 引用类型
                    arrList.add(childMap); // [{1},{2}]
                    itemList.add(childMap);
                }
            }
            if (!itemList.contains(map)) {
                resultList.add(map); //[ [{1},{2}, [{3},{4}] ] ]
            }
        }
        return resultList;
    }

    private static Map<String, String> getCategoryExampleMap(String id, String name, String parentId) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("key", id);
        queryMap.put("name", name);
        queryMap.put("id", parentId);
        return queryMap;
    }

    public static List<Object> getResult(List<Map> mapList) {

        List<Map> queryList = mapList;
        List<Object> resultList = new ArrayList<>();

        //为每一个节点添加孩子字段
        for (Map map : queryList) {
            List<Map> childList = new ArrayList<>();
            map.put("children", childList);
        }

        List<Map> itemList = new ArrayList<>();
        for (Map map : queryList) {
            for (Map childMap : queryList) {
                if (childMap.get("fid").toString().equals(map.get("value").toString())) {
                    List<Map> arrList = (ArrayList<Map>) map.get("children");
                    arrList.add(childMap);
                    itemList.add(childMap);
                }
            }
            if (!itemList.contains(map)) {
                resultList.add(map);
            }
        }
        return resultList;
    }

    private static Map<String, String> getQueryMap(String id, String name, String fid) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("value", id);
        queryMap.put("label", name);
        queryMap.put("fid", fid);
        return queryMap;
    }

    /**
     * 分类函数查子
     */
    public void findCategorySonMap(List<Category> categoryList, List<Category> categoryItem) {

        for (Category category : categoryItem) {
            categoryList.add(category);
            List<Category> categories = categoryMapper.findCategorySonList(category.getId());
            findCategorySonMap(categoryList, categories);
        }

    }

    /**
     * 分类函数查父
     */
    public void finCategoryParentMap(List<Integer> categoryIdList, int id) {
        if (id == Part.CATEGORY_ID_PART) {
            return;
        }
        int item = categoryMapper.findCategoryParentId(id);
        categoryIdList.add(item);
        finCategoryParentMap(categoryIdList, item);
    }

    /**
     * 新加配件分类
     */
    public int createCategory(PartCategoryCreateDto partCategoryCreateDto)  {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //验证分类名称是否为空，是否存在
        if (partMapper.findPartCategoryNameExist(partCategoryCreateDto.getName()) != 0 ){
            throw new RuntimeException("此分类名称已存在");
        }

        if (partCategoryCreateDto.getParentId() != 0) {
            //判断传入的parent_id是否存在
            if (partMapper.findPartCategoryExist(partCategoryCreateDto.getParentId()) == null) {
                throw new RuntimeException("父级分类不存在");
            }
        }
        if (partCategoryCreateDto.getDepreciationId() != 0) {
            //判断传入的depreciation_id是否存在
            if (depreciationMapper.findDepreciationExist(partCategoryCreateDto.getDepreciationId()) == null) {
                throw new RuntimeException("购入路径不存在");
            }
        }
        return partMapper.createPartCategory(partCategoryCreateDto, Part.CATEGORY_ID_PART);
    }

    /**
     * 删除配件分类
     */
    public int deleteCategory(int categoryId) {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //验证改分类Id是否为配件分类id
        if (categoryMapper.findCategoryIdExist(categoryId,Part.CATEGORY_ID_PART) == 0){
            throw new RuntimeException("无此配件Id");
        }

        //先判断当前分类下是否存在绑定的商品

        //查看该分类id下是否存在子分类id
        //查出配件下面所有的分类
        //新建一个List，把递归之后的每一个category存到里面
        List<Category> categoryList = new ArrayList<>();
        //查配件分类下的第一级
        List<Category> categoryList1 = categoryMapper.findCategorySonList(categoryId);
        //调用递归
        findCategorySonMap(categoryList, categoryList1);

        List<Integer> categoryIdList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryIdList.add(category.getId());
        }
        categoryIdList.add(categoryId);

        //将资产表中属于该分类的设备分类设置为0
        if(partMapper.selectPartBinding(categoryIdList) > 0){
            throw new RuntimeException("存在设备绑定无法删除");
        }

        return partMapper.deletePartCategory(categoryIdList, Asset.STATUS_DELETE);
    }

    /**
     * 编辑配件分类
     */
    public int updateCategory(PartCategoryUpdateDto partCategoryUpdateDto) {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        if (partCategoryUpdateDto.getParentId() != 0) {
            //判断传入的parent_id是否存在
            if (partMapper.findPartCategoryExist(partCategoryUpdateDto.getParentId()) == null) {
                throw new RuntimeException("父级分类不存在");
            }
        }

        if (partCategoryUpdateDto.getDepreciationId() != 0) {
            //判断传入的depreciation_id是否存在
            if (depreciationMapper.findDepreciationExist(partCategoryUpdateDto.getDepreciationId()) == null) {
                throw new RuntimeException("购入路径不存在");
            }
        }

        return partMapper.updatePartCategory(partCategoryUpdateDto);
    }

    /**
     * 配件绑定归属设备
     */
    public int bindDevice(int partAssetId, int deviceAssetId) {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //验证设备id
        if(partMapper.findDeviceExist(Asset.STATUS_DELETE,deviceAssetId) == 0){
            throw new RuntimeException("无此设备");
        }

        //将该配件之前的归属删除
        partMapper.updatePartAffiliate(Asset.STATUS_DELETE,partAssetId);

        //验证记录当中当前配件最新一条是否状态是否为归属状态
        //如果状态为yes，将状态改为删除状态
        String partStatus = partMapper.findTrackStatus(partAssetId);

        if (partStatus != null && Integer.parseInt(partStatus) == Asset.STATUS_AFFILIATION) {
            //将最新的绑定记录状态改为删除状态
            partMapper.updateTrackStatus(partAssetId, Asset.STATUS_DELETE, new Date());
        }

        //给资产绑定设备id
        partMapper.updatePartBind(partAssetId, deviceAssetId, Asset.STATUS_AFFILIATION, new Date());
        //资产绑定表当中添加数据
        partMapper.createAffiliate(partAssetId, deviceAssetId);
        //资产记录添加
        return partMapper.createBindTrack(partAssetId, UserContext.getUser().getId(), deviceAssetId, new Date(), Asset.STATUS_AFFILIATION);

    }

    /**
     * 获取配件归属列表
     */
    public List<PartTrackReturnDto> findPartBindList(Pagination pagination) {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //查出配件分类下所有的分类id
        //新建一个List，把递归之后的每一个category存到里面
        List<Category> categoryList = new ArrayList<>();

        //查配件分类下的第一级
        List<Category> categoryList1 = categoryMapper.findCategorySonList(Asset.CATEGORY_ID_PART);

        //调用递归
        findCategorySonMap(categoryList, categoryList1);
        List<Integer> partIdList = new ArrayList<>();

        //组装一个配件类id列表
        for (Category category : categoryList) {
            partIdList.add(category.getId());
        }

        //通过配件类Id列表查找所有的配件资产编号
        List<Part> parts = partMapper.findPartsByCategoryIds(partIdList, Asset.STATUS_DELETE);
        List<Integer> partIds = new ArrayList<>();
        for (Part part : parts) {
            partIds.add(part.getId());
        }
        //通过配件id列表去归属列表里面查记录
        List<Track> tracks = partMapper.findTrackByAssetId(partIds,pagination);


        //组装归属设备Map
        Set<Integer> deviceIds = new HashSet<>();
        for (Part part : parts) {
            deviceIds.add(part.getDeviceId());
        }
        List<Part> deviceList = partMapper.findPartById(deviceIds, Asset.STATUS_DELETE);
        Map<Integer, String> deviceMap = new HashMap<>();
        for (Part part : deviceList) {
            deviceMap.put(part.getId(), part.getName());
        }
        //组装配件名
        Map<Integer, String> partName = new HashMap<>();
        for (Part part : parts) {
            partName.put(part.getId(), part.getName());
        }

        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(Asset.STATUS_AFFILIATION, "归属");

        List<PartTrackReturnDto> partTrackReturnDtoList = new ArrayList<>();
        for (Track track : tracks) {
            PartTrackReturnDto partTrackReturnDto = new PartTrackReturnDto();
            partTrackReturnDto.setAssetId(track.getFixedAssetId());
            partTrackReturnDto.setName(partName.get(track.getFixedAssetId()));
            partTrackReturnDto.setTrackDeviceId(track.getDeviceId());
            partTrackReturnDto.setTrackDeviceName(deviceMap.get(track.getDeviceId()));
            partTrackReturnDto.setCreateAt(track.getCreatedAt());
            partTrackReturnDto.setUpdateAt(track.getUpdatedAt());
            partTrackReturnDto.setBindingStartAt(track.getBindingStartAt());
            partTrackReturnDto.setBindingEndAt(track.getBindingEndAt());
            partTrackReturnDto.setLendDescription(track.getLendDescription());
            partTrackReturnDto.setLendStartAt(track.getLendStartAt());
            partTrackReturnDto.setLendReturnAt(track.getLendReturnAt());
            partTrackReturnDto.setLendPlanReturnAt(track.getLendPlanReturnAt());
            partTrackReturnDto.setStatus(statusMap.get(track.getStatus()));
            partTrackReturnDtoList.add(partTrackReturnDto);
        }

        return partTrackReturnDtoList;
    }

    /**
     * 解除归属绑定
     */
    public Boolean cancelBind(int assetId) {

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //将资产表状态改为空闲，资产记录表最新一条更新，将归属表中的记录删除
        //将归属表中的数据改为删除状态
        partMapper.updateAssetAffiliate(Asset.STATUS_DELETE, assetId);
        return (partMapper.updateAffiliateStatus(assetId, 0, Asset.STATUS_LEISURE) == 1 && partMapper.updateTrackStatus(assetId, Asset.STATUS_DELETE, new Date()) == 1);
    }

    /**
     * 故障报修
     */
    public int malfunction(int assetId, String description){

        //验证有无配件权限
        permissionsService.verifyPermissions("part","part");

        //验证资产id是否存在
        if (partMapper.findAssetExist(assetId, Asset.STATUS_DELETE) == null) {
            throw new RuntimeException("不存在的资产id");
        }
        //验证该资产状态是否为报修状态
        if (partMapper.findAssetStatus(assetId) == Asset.STATUS_MALFUNCTION) {
            throw new RuntimeException("重复报修");
        }
        //更新资产表中状态为故障
        partMapper.updateAssetStatus(assetId, Asset.STATUS_MALFUNCTION);
        return partMapper.insertMalfunction(assetId, description, Asset.STATUS_MALFUNCTION);
    }

}
