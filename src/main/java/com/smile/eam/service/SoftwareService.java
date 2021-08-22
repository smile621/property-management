package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.*;
import com.smile.eam.entity.*;
import com.smile.eam.mapper.*;
import com.smile.eam.entity.AssetAffiliate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SoftwareService {
    @Resource
    BuyRouterMapper buyRouterMapper;
    @Resource
    IssueMapper issueMapper;
    @Resource
    CategoryMapper categoryMapper;
    @Resource
    VendorMapper vendorMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    AssetAffiliateMapper assetAffiliateMapper;
    @Resource
    FixedAssetMapper fixedAssetMapper;
    @Resource
    AssetCategoryMapper assetCategoryMapper;
    @Resource
    VerifyMapper verifyMapper;
    @Resource
    StatusMapper statusMapper;
    @Resource
    FixedAssetTrackMapper fixedAssetTrackMapper;
    /**
     * 判断传入的software是否已存在于列表
     */
    public Boolean existSoftware(SoftwareCreateRequest softwareCreateRequest) {
        SoftwareCreateRequest s = assetCategoryMapper.existSoftware(softwareCreateRequest);
        if (s == null) {
            return false;
        }
        return true;
    }

    /**
     * 添加软件
     */

    public void addSoftware(SoftwareCreateRequest softwareCreateRequest) {
        assetCategoryMapper.addSoftware(softwareCreateRequest);
    }

    /**
     * 修改软件列表..
     */
    public int updateSoftware(UpdateSoftwareByIdRequest updateSoftwareByIdRequest) {
        int s = assetCategoryMapper.updateSoftware(updateSoftwareByIdRequest);
        return s;
    }

    /**
     * 判断资产分类传入的父类id是否是3，以及是否存在
     * */
    public Boolean isExist(Integer id){
        AssetCategory assetCategory = assetCategoryMapper.isExist(id);
        if(assetCategory==null){
            return false;
        }
        return true;
    }

    /**
     * 软件资产分类查找
     * */
    public Boolean findSoftware(DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest){
        AssetCategory s = assetCategoryMapper.findSoftware(deleteSoftwareCategoryRequest);
        if(s!=null){
            return true;
        }
        return false;
    }

    /**
     * 软件资产分类删除
     */
    public int deleteSoftware(DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest) {
        return assetCategoryMapper.deleteSoftware( deleteSoftwareCategoryRequest);
    }

    /**
     * 软件资产分类列表
     */
    public List<SoftwareCategoryResponse> findSoftwareCategory(GetSoftwareCategoryById getSoftwareCategoryById, Pagination pagination) {
       return   assetCategoryMapper.findSoftwareCategory(getSoftwareCategoryById,pagination);
    }

    /**
     * 软件分类模糊查找
     * */
    public List<AssetCategory> lookFor(String name,Pagination pagination){
        return assetCategoryMapper.lookFor(name, pagination);
    }

    /**
     * 软件分类模糊查找总个数
     * */
    public int lookForCount(String name){
        return assetCategoryMapper.lookForCount(name);
    }

    /**
     * 获取软件资产分类列表总数
     * */
    public int softwareCategoryCount(){
      return   assetCategoryMapper.softwareCategoryCount();
    }

    /**
     * 软件资产新增
     */
    public void addSoftwareProperty(SoftwarePropertyCreateRequest softwarePropertyCreateRequest) {
        if(verifyMapper.findVendorById(softwarePropertyCreateRequest.getVendorId())==null){
            throw new RuntimeException("无此厂商编号");
        }if(verifyMapper.findBuyRouterById(softwarePropertyCreateRequest.getBuyRouterId())==null){
            throw new RuntimeException("无此购买路径");
        }if(verifyMapper.findCategoryById(softwarePropertyCreateRequest.getAssetCategoryId())==null){
            throw new RuntimeException("无此分类");
        }if(issueMapper.findIssueById(softwarePropertyCreateRequest.getIssueId())==null){
            throw new RuntimeException("无此发行方式");
        }
        fixedAssetMapper.addSoftwareProperty(softwarePropertyCreateRequest);
    }

    /**
     * 判断已传入的软件资产id是否存在
     */
    public FixedAsset findSoftwarePropertyById(UpdateSoftwarePropertyById updateSoftwarePropertyById) {
        FixedAsset fixedAsset = fixedAssetMapper.findSoftwarePropertyById(updateSoftwarePropertyById);
        return fixedAsset;
    }
    /**
     * 获取软件资产详情
     * */
    public FixedAsset getSoftwareDetail( DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest){
        String assetCategory ="";
        String buyRouter="";
        String vendor ="";
        String issue ="";
        //获取软件资产详情
        FixedAsset fix = fixedAssetMapper.getSoftwareDetail(deleteSoftwareCategoryRequest.getId());
        //根据软件分类id,查找对应软件分类name
        if(assetCategoryMapper.isCategory(fix.getAssetCategoryId())!=null){
            assetCategory = assetCategoryMapper.isCategory(fix.getAssetCategoryId()).getName();
        }
        //根据购买途径，查找对应购买途径name
        if(buyRouterMapper.findDetailById(fix.getBuyRouterId())!=null){
             buyRouter = buyRouterMapper.findDetailById(fix.getBuyRouterId()).getName();
        }
        //根据厂商id,查找对应厂商的name;
        if(vendorMapper.isVendor(fix.getVendorId())!=null){
             vendor = vendorMapper.isVendor(fix.getVendorId()).getName();
        }
        //根据版本发行方式id,查找对应的name
        if(issueMapper.isIssue(fix.getIssueId())!=null){
             issue = issueMapper.isIssue(fix.getIssueId()).getName();
        }
        return FixedAsset.builder()
                .id(fix.getId())
                .name(fix.getName())
                .assetCategoryId(fix.getAssetCategoryId())
                .assetCategory(assetCategory)
                .userId(fix.getUserId())
                .deviceId(fix.getDeviceId())
                .vendorId(fix.getVendorId())
                .vendor(vendor)
                .price(fix.getPrice())
                .buyRouterId(fix.getBuyRouterId())
                .buyRouter(buyRouter)
                .description(fix.getDescription())
                .specification(fix.getSpecification())
                .version(fix.getVersion())
                .issueId(fix.getIssueId())
                .issue(issue)
                .warrantyNumber(fix.getWarrantyNumber())
                .startAt(fix.getStartAt())
                .endAt(fix.getEndAt())
                .buyAt(fix.getBuyAt())
                .expiredAt(fix.getExpiredAt())
                .expiredDate(fix.getExpiredDate())
                .createdAt(fix.getCreatedAt())
                .updatedAt(fix.getUpdatedAt())
                .status(fix.getStatus())
                .flag(fix.getFlag())
                .build();
    }

    /**
     * 获取软件资产详情
     * */
    public List<SoftwarePropertyResponse> softwareDetail(DeleteSoftwareCategoryRequest deleteSoftwareCategoryRequest){
        //获取软件资产详情
        List<FixedAsset> fixedAssetList = fixedAssetMapper.softwareDetail(deleteSoftwareCategoryRequest.getId());
        return getSoftwarePropertyResponse(fixedAssetList);
    }

    /**
     * 软件资产编辑
     */
    public int editSoftwareProperty(SoftwarePropertyCreateRequest softwarePropertyCreateRequest,  UpdateSoftwarePropertyById updateSoftwarePropertyById) {
        int s = fixedAssetMapper.editSoftwareProperty(softwarePropertyCreateRequest, updateSoftwarePropertyById);
        return s;
    }

    /**
     * 软件资产删除
     */
    public void deleteSoftwareProperty(UpdateSoftwarePropertyById updateSoftwarePropertyById) {
         fixedAssetMapper.deleteSoftwareProperty(updateSoftwarePropertyById);
    }

    /**
     * 对应的软件绑定列表也要删除相应软件
     * */
    public void deleteByAssetId(UpdateSoftwarePropertyById updateSoftwarePropertyById){
        assetAffiliateMapper.deleteByAssetId(updateSoftwarePropertyById);
    }


    /**
     * 匹配过的软件列表
     * */
    public List<SoftwarePropertyResponse> getSoftwarePropertyResponse(List<FixedAsset> list){
        if(list.size()==0){
            return new ArrayList<>();
        }
        //把issueId和发行方式组装map
        Set<Integer> issueIdS = list.stream().map((item) -> {
            return item.getIssueId();
        }).collect(Collectors.toSet());
        List<Issue> issuesList = issueMapper.findIssueNameById(issueIdS);
        Map<Integer, String> issueMap = issuesList.stream().collect(Collectors.toMap(Issue::getId, Issue::getName));

        //把buyRouterId和购入途径进行map组装
        Set<Integer> buyRouterIds = list.stream().map((item) -> {
            return item.getBuyRouterId();
        }).collect(Collectors.toSet());
        List<BuyRouter> buyRouterList = buyRouterMapper.findBuyRouterById(buyRouterIds);
        Map<Integer, String> buyRouterMap = buyRouterList.stream().collect(Collectors.toMap(BuyRouter::getId, BuyRouter::getName));

        //把软件分类id和软件分类进行map组装
        Set<Integer> assetCategoryIds = list.stream().map((item) -> {
            return item.getAssetCategoryId();

        }).collect(Collectors.toSet());
        List<AssetCategory> categoryList = assetCategoryMapper.findCategoryByIds(assetCategoryIds);
        Map<Integer, String> categoryMap = categoryList.stream().collect(Collectors.toMap(AssetCategory::getId,AssetCategory::getName));

        //把厂商Id和厂商名字进行map组装
        Set<Integer> vendorIds = list.stream().map((item) -> {
            return item.getVendorId();
        }).collect(Collectors.toSet());
        List<Vendor> vendorList = vendorMapper.findVendorById(vendorIds);
        Map<Integer, String> vendorMap = vendorList.stream().collect(Collectors.toMap(Vendor::getId,Vendor::getName));

        //把借用人user_id与借用人username组装成map
        Set<Integer> userIds = list.stream().map((item) -> {
            return item.getUserId();
        }).collect(Collectors.toSet());
        List<AdminUser> userList = userMapper.findUserListById(userIds);
        Map<Integer, String> userMap = userList.stream().collect(Collectors.toMap(AdminUser::getId, AdminUser::getUsername));

        //把软件资产Id与软件资产名字组成map
        Set<Integer> softwareIds = list.stream().map((item) -> {
            return item.getId();
        }).collect(Collectors.toSet());
        Map<Integer,String> idMap = list.stream().collect(Collectors.toMap(FixedAsset::getId,FixedAsset::getName));

        //根据软件资产id,去软件设备绑定表查找，对应的设备id
        List<AssetAffiliate> assetAffiliateById1 = assetAffiliateMapper.findAssetAffiliateById(softwareIds);
        List<SoftwarePropertyResponse> softwarePropertyResponseList = new ArrayList<>();
        if(assetAffiliateById1.size()==0){
            for (FixedAsset fix : list) {
                SoftwarePropertyResponse softwarePropertyResponse = SoftwarePropertyResponse.builder()
                        .id(fix.getId())
                        .name(fix.getName())
                        .assetCategoryId(categoryMap.get(fix.getAssetCategoryId()))//软件分类
                        .price(fix.getPrice())
                        .buyRouterId(buyRouterMap.get(fix.getBuyRouterId()))//购买途径
                        .description(fix.getDescription())//资产描述
                        .vendorId(vendorMap.get(fix.getVendorId()))//厂商id
                        .status(AssetAffiliate.builder().status(fix.getStatus()).build().getStatusAlias())
                        .version(fix.getVersion())
                        .issueId(issueMap.get(fix.getIssueId()))//发行版本id
                        .warrantyNumber(fix.getWarrantyNumber())
                        .startAt(fix.getStartAt())
                        .buyAt(fix.getBuyAt())
                        .endAt(fix.getEndAt())
                        .expiredDate(fix.getExpiredDate())
                        .userId(userMap.get(fix.getUserId()))//借用人
                        .deviceId(new ArrayList<>())//归属设备
                        .deviceIds(new ArrayList<>())
                        .build();
                softwarePropertyResponseList.add(softwarePropertyResponse);
            }
            return softwarePropertyResponseList;
        }
        //组装设备id和设备名称，成map
        Set<Integer> deviceIds = assetAffiliateById1.stream().map((item)->{
            return item.getAssetAffiliate();
        }).collect(Collectors.toSet());
        List<FixedAsset> assetByIdList = fixedAssetMapper.findFixedAssetById(deviceIds);
        Map<Integer,String> deviceMap = assetByIdList.stream().collect(Collectors.toMap(FixedAsset::getId,FixedAsset::getName));

        //获取软件id与设备id对应列表
        List<AssetAffiliate> assetAffiliateById = assetAffiliateMapper.findAssetAffiliateById(softwareIds);//软件id与设备id对应的List
        //软件id与设备名称对应列表
        List<AssetAffiliateResponse> assetAffiliateResponseList = new ArrayList<>();
        for (AssetAffiliate asset : assetAffiliateById) {
            AssetAffiliateResponse assetAffiliateResponse= AssetAffiliateResponse.builder()
                    .assetId(asset.getAssetId())
                    .deviceId(asset.getAssetAffiliate())
                    .assetAffiliate(deviceMap.get(asset.getAssetAffiliate()))
                    .build();
            assetAffiliateResponseList.add(assetAffiliateResponse);
        }
        //组装软件id，对应设备id，按照软件id分组
        Map<Integer, List<AssetAffiliateResponse>> groupByAssetId = assetAffiliateResponseList.stream().collect(Collectors.groupingBy(AssetAffiliateResponse::getAssetId));
        //组装软件id，对应设备名称，按照软件id分组
        Map<Integer,List<String>>  assetIdMap = new HashMap<>();
        Map<Integer,List<Integer>>  deviceIdsMap = new HashMap<>();
        for (Integer softwareId : softwareIds) {
            if(groupByAssetId.get(softwareId)!=null){
                List<String> tempList = groupByAssetId.get(softwareId).stream().map(AssetAffiliateResponse::getAssetAffiliate).collect(Collectors.toList());
                List<Integer> tempList1 = groupByAssetId.get(softwareId).stream().map(AssetAffiliateResponse::getDeviceId).collect(Collectors.toList());
                assetIdMap.put(softwareId,tempList);
                deviceIdsMap.put(softwareId,tempList1);
            }
        }
       //返回数据
        for (FixedAsset fix : list) {
            SoftwarePropertyResponse softwarePropertyResponse = SoftwarePropertyResponse.builder()
                    .id(fix.getId())
                    .name(fix.getName())
                    .assetCategoryId(categoryMap.get(fix.getAssetCategoryId()))//软件分类
                    .price(fix.getPrice())
                    .buyRouterId(buyRouterMap.get(fix.getBuyRouterId()))//购买途径
                    .description(fix.getDescription())//资产描述
                    .vendorId(vendorMap.get(fix.getVendorId()))//厂商id
                    .status(AssetAffiliate.builder().status(fix.getStatus()).build().getStatusAlias())
                    .version(fix.getVersion())
                    .issueId(issueMap.get(fix.getIssueId()))//发行版本id
                    .warrantyNumber(fix.getWarrantyNumber())
                    .startAt(fix.getStartAt())
                    .endAt(fix.getEndAt())
                    .buyAt(fix.getBuyAt())
                    .expiredDate(fix.getExpiredDate())
                    .userId(userMap.get(fix.getUserId()))//借用人
                    .deviceId(assetIdMap.get(fix.getId()))//归属设备
                    .deviceIds(deviceIdsMap.get(fix.getId()))
                    .build();
            softwarePropertyResponseList.add(softwarePropertyResponse);
        }
        return softwarePropertyResponseList;
    }

    /**
     * 获取软件列表
     */
    public List<SoftwarePropertyResponse> softwarePropertyList( Pagination pagination){
        //获取返回的资产列表
        List<FixedAsset> fixedAssetList = fixedAssetMapper.softwarePropertyList(pagination);
        return getSoftwarePropertyResponse(fixedAssetList);
    }

    /**
     * 获取软件列表总数量
     */
    public int softwarePropertyCount() {
        return fixedAssetMapper.softwarePropertyCount();
    }

    /**
     * 软件资产绑定
     */
    public void bindSoftware(BindSoftwareRequest bindSoftwareRequest) {
        assetAffiliateMapper.bindSoftware(bindSoftwareRequest);
    }

    /**
     * 修改软件资产状态为1：闲置
     * */
    public void updateAssetStatus(Integer assetId){
          fixedAssetMapper.updateAssetStatus(assetId);
    }

    /**
     * 修改软件资产状态为2：归属
     * */
    public int updateAssetStatusById(BindSoftwareRequest bindSoftwareRequest){
        return  fixedAssetMapper.updateAssetStatusById(bindSoftwareRequest);
    }

    /**
     * 软件资产绑定时判断是否有对应的assetId
     * */
    public Boolean existBindSoftwarePropertyAssetId(BindSoftwareRequest bindSoftwareRequest){
        FixedAsset fixedAsset = fixedAssetMapper.existBindSoftwarePropertyAssetId(bindSoftwareRequest);
        if(fixedAsset==null){
            return false;
        }
        return true;
    }

    /**
     * 软件资产绑定时判断是否有对应的AssetAffiliate
     * */
    public Boolean existBindSoftwarePropertyAssetAffiliate(BindSoftwareRequest bindSoftwareRequest){
        FixedAsset fixedAsset = fixedAssetMapper.existBindSoftwarePropertyAssetAffiliate(bindSoftwareRequest);
        if(fixedAsset==null){
            return false;
        }
        return true;
    }

    /**
     * 判断软件有没有绑定过该设备
     * */
    public Boolean isSoftwareBindDevice(BindSoftwareRequest bindSoftwareRequest){
        AssetAffiliate softwareBindDevice = assetAffiliateMapper.isSoftwareBindDevice(bindSoftwareRequest);
        if(softwareBindDevice==null){
            return false;
        }
        return true;
    }
    /**
     * 传入id,查找软件绑定列表，对应的asset_id
     * */
    public AssetAffiliate findAssetIdById(Integer id){
        return assetAffiliateMapper.findAssetIdById(id);
    }

    /**
     * 根据软件资产Id,判断`asset_affiliate`表中，是否还有其他的绑定设备,返回数据条数
     * */
    public int isExistAssetId(Integer assetId){
        return assetAffiliateMapper.isExistAssetId(assetId);
    }

    /**
     * 取消软件资产绑定
     */
    public int cancelBindSoftware( Integer id) {
        return assetAffiliateMapper.cancelBindSoftware(id);
    }

    /**
     * 软件资产归属列表，共有多少条不重复软件id
     * */
    public int assetIdCount(){
        List<AssetAffiliate> assetAffiliateList = assetAffiliateMapper.allSoftwareList();
        Set<Integer> assetId = assetAffiliateList.stream().map((item) -> {
            return item.getAssetId();
        }).collect(Collectors.toSet());
        return assetId.size();
    }

    /**
     * 软件资产归属列表
     */
    public List<AssetAffiliateDto> bindSoftwareList(Pagination pagination){
        //获取资产绑定设备列表
        List<AssetAffiliate> assetAffiliateList = assetAffiliateMapper.bindSoftwareList(pagination);
        if(assetAffiliateList.size()==0){
            return new ArrayList<AssetAffiliateDto>();
        }
        //组装软件id，名称 map
        Set<Integer> assetIds=assetAffiliateList.stream().map((item)->{
            return item.getAssetId();
        }).collect(Collectors.toSet());
        //根据资产Id,获取对应的资产名称列表
        List<FixedAsset> fixedAssetList = fixedAssetMapper.findFixedAssetById(assetIds);
        Map<Integer,String> fixedMap=fixedAssetList.stream().collect(Collectors.toMap(FixedAsset::getId,FixedAsset::getName));
        //组装设备id,名称 map
        Set<Integer> assetAffiliates=assetAffiliateList.stream().map((item)->{
            return item.getAssetAffiliate();
        }).collect(Collectors.toSet());
        List<FixedAsset> affiliateList= fixedAssetMapper.findFixedAssetById(assetAffiliates);
        Map<Integer,String> deviceMap = affiliateList.stream().collect(Collectors.toMap(FixedAsset::getId,FixedAsset::getName));
        List<AssetAffiliateDto> assetAffiliateLists = new ArrayList<>();
        for (AssetAffiliate a : assetAffiliateList) {
            AssetAffiliateDto assetAffiliateDto = AssetAffiliateDto.builder()
                    .id(a.getAssetId())
                    .keyId(a.getId())
                    .assetId(fixedMap.get(a.getAssetId()))
                    .assetAffiliate(deviceMap.get(a.getAssetAffiliate()))
                    .createdAt(a.getCreatedAt())
                    .updatedAt(a.getUpdatedAt())
                    .status(a.getStatus())
                    .build();
            assetAffiliateLists.add(assetAffiliateDto);
        }
        return assetAffiliateLists;
    }

    /**
     * 软件分类模糊查找,返回列表
     * */
    public List<SoftwarePropertyResponse> findFixedAsset(FindSoftwareFixedAssetRequest findSoftwareFixedAssetRequest,Pagination pagination){
        //获取模糊查找返回的列表
        List<FixedAsset> fixedAssetList = fixedAssetMapper.findFixedAsset(findSoftwareFixedAssetRequest.getAssetCategoryId(), findSoftwareFixedAssetRequest.getSearch(), pagination);
       return  getSoftwarePropertyResponse(fixedAssetList);
    }

    /**
     * 返回模糊查找总个数
     * */
    public  int findFixedAssetCount(FindSoftwareFixedAssetRequest findSoftwareFixedAssetRequest,Pagination pagination){
        return fixedAssetMapper.findFixedAssetCount(findSoftwareFixedAssetRequest.getAssetCategoryId(), findSoftwareFixedAssetRequest.getSearch(), pagination);
    }

    /**
     * 批量删除软件资产列表
     * */
    public void deleteSoftwareListByIds( SoftwareListDeleteRequest softwareListDeleteRequest){
        fixedAssetMapper.deleteSoftwareListByIds(softwareListDeleteRequest.getList());
    }

    /**
     * 批量删除软件归属列表
     * */
    public void deleteSoftwareBindListByIds( SoftwareListDeleteRequest softwareListDeleteRequest){
        assetAffiliateMapper.deleteSoftwareBindListByIds(softwareListDeleteRequest.getList());
    }

    /**
     * 批量删除软件分类列表
     * */
    public void deleteSoftwareCategoryListByIds( SoftwareListDeleteRequest softwareListDeleteRequest){
        assetCategoryMapper.deleteSoftwareCategoryListByIds(softwareListDeleteRequest.getList());
    }

    /**
     * 软件绑定模糊查找
     * */
    public FindBindSoftwareDto findByName(String name,Pagination pagination){

        if(name==null){
            return  FindBindSoftwareDto.builder()
                    .assetAffiliateLists(new ArrayList<AssetAffiliateDto>())
                    .pagination(pagination)
                    .build();

        }
        List<AssetAffiliate> byName = assetAffiliateMapper.findByName(name, pagination);
        Map<String,Object> result = new HashMap<>();
        //获取资产绑定设备模糊查找列表
        List<AssetAffiliate> assetAffiliateList = assetAffiliateMapper.bindSoftwareList(pagination);
        //组装软件id，名称 map
        Set<Integer> assetIds=assetAffiliateList.stream().map((item)->{
            return item.getAssetId();
        }).collect(Collectors.toSet());
        //根据资产Id,获取对应的资产名称列表
        List<FixedAsset> fixedAssetList = fixedAssetMapper.findFixedAssetById(assetIds);
        Map<Integer,String> fixedMap=fixedAssetList.stream().collect(Collectors.toMap(FixedAsset::getId,FixedAsset::getName));
        Map<String, List<FixedAsset>> softwareMap = fixedAssetList.stream().collect(Collectors.groupingBy(FixedAsset::getName));
        Map<String,List<Integer>> softwareResult = new HashMap<>();
        for (String s : softwareMap.keySet()) {
            List<Integer> collect = softwareMap.get(s).stream().map(FixedAsset::getId).collect(Collectors.toList());
            softwareResult.put(s,collect);
        }
        //组装设备id,名称 map
        Set<Integer> assetAffiliates=assetAffiliateList.stream().map((item)->{
            return item.getAssetAffiliate();
        }).collect(Collectors.toSet());
        List<FixedAsset> affiliateList= fixedAssetMapper.findFixedAssetById(assetAffiliates);
        Map<Integer,String> deviceMap = affiliateList.stream().collect(Collectors.toMap(FixedAsset::getId,FixedAsset::getName));
        Map<String, List<FixedAsset>> deviceMapFlag = affiliateList.stream().collect(Collectors.groupingBy(FixedAsset::getName));
        Map<String,List<Integer>> deviceResult = new HashMap<>();
        for (String s : deviceMapFlag.keySet()) {
            List<Integer> collect = deviceMapFlag.get(s).stream().map(FixedAsset::getId).collect(Collectors.toList());
            deviceResult.put(s,collect);
        }
        List<AssetAffiliateDto> assetAffiliateLists = new ArrayList<>();
        if(byName.size()!=0){
            for (AssetAffiliate a : byName) {
                AssetAffiliateDto assetAffiliateDto = AssetAffiliateDto.builder()
                        .id(a.getAssetId())
                        .keyId(a.getId())
                        .assetId(fixedMap.get(a.getAssetId()))
                        .assetAffiliate(deviceMap.get(a.getAssetAffiliate()))
                        .createdAt(a.getCreatedAt())
                        .updatedAt(a.getUpdatedAt())
                        .status(a.getStatus())
                        .build();
                assetAffiliateLists.add(assetAffiliateDto);
            }
        }
        //名字完全匹配
        //通过asset_id list 循环查找表格
        if(softwareResult.get(name)!=null){
            List<AssetAffiliate> listByAssetIds = assetAffiliateMapper.findListByAssetIds(softwareResult.get(name), pagination);
            for (AssetAffiliate l : listByAssetIds) {
                AssetAffiliateDto assetAffiliateDto = AssetAffiliateDto.builder()
                        .id(l.getAssetId())
                        .keyId(l.getId())
                        .assetId(fixedMap.get(l.getAssetId()))
                        .assetAffiliate(deviceMap.get(l.getAssetAffiliate()))
                        .createdAt(l.getCreatedAt())
                        .updatedAt(l.getUpdatedAt())
                        .status(l.getStatus())
                        .build();
                assetAffiliateLists.add(assetAffiliateDto);
            }
        }

        //通过 asset_affiliate list 循环查找表格
        if(deviceResult.get(name)!=null){
            List<AssetAffiliate> listByAssetAffiliates = assetAffiliateMapper.findListByAssetAffiliates(deviceResult.get(name), pagination);
            for (AssetAffiliate l : listByAssetAffiliates) {
                AssetAffiliateDto assetAffiliateDto = AssetAffiliateDto.builder()
                        .id(l.getAssetId())
                        .assetId(fixedMap.get(l.getAssetId()))
                        .assetAffiliate(deviceMap.get(l.getAssetAffiliate()))
                        .createdAt(l.getCreatedAt())
                        .updatedAt(l.getUpdatedAt())
                        .status(l.getStatus())
                        .build();
                assetAffiliateLists.add(assetAffiliateDto);
            }
        }
        pagination.setTotal(assetAffiliateLists.size());
        return FindBindSoftwareDto.builder()
                .assetAffiliateLists(assetAffiliateLists)
                .pagination(pagination)
                .build();
    }


    /**
     * 更新过保时间
     * */
    public void updateExpiredDate(){
        fixedAssetMapper.updateExpiredDate();
    }

    /**
     * 即将过保软件总数
     * */
    public int willPast(){
      return   fixedAssetMapper.willPast();
    }

    /**
     * 已过保软件总数
     * */
    public int past(){
        return   fixedAssetMapper.past();
    }

    /**
     * 软件全年价值趋势
     * */
    public Map<String,BigDecimal> allYearValue(Integer year){
        //获取当年全部数据
        List<FixedAsset> softwareValues = fixedAssetMapper.allYearValue(year);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
        //把当年数据组装成只含有年月
        List<SoftwareValue> monthValues = new ArrayList<>();
        for (FixedAsset s : softwareValues) {
            SoftwareValue value = SoftwareValue.builder()
                    .price(s.getPrice())
                    .createdAt(simpleDateFormat.format(s.getCreatedAt()))
                    .build();
            monthValues.add(value);
        }
        //按照createdAt分组，组成map
        Map<String, List<SoftwareValue>> monthMap = monthValues.stream().collect(Collectors.groupingBy(SoftwareValue::getCreatedAt));
        Map<String,BigDecimal> priceByMonth = new HashMap<>();
        for (String s : monthMap.keySet()) {
            BigDecimal bigDecimal = monthMap.get(s).stream().map(SoftwareValue::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            priceByMonth.put(s,bigDecimal);
        }
        return priceByMonth;
    }

    /**
     * 添加软件绑定记录
     */
    public void addSoftwareBind(Integer FixedAssetId,Integer deviceId){
        fixedAssetTrackMapper.addSoftwareBind(FixedAssetId,deviceId);
    }
    /**
     * 添加软件取消绑定记录
     */
    public void addSoftwareCancelBind(Integer FixedAssetId,Integer deviceId){
        fixedAssetTrackMapper.addSoftwareCancelBind(FixedAssetId,deviceId);
    }

    /**
     * 获取软件归属表记录
     * */
    public List<SoftwareTrackResponse> getTrack(Integer id,Pagination pagination){
        List<SoftwareTrackRequest> trackList = fixedAssetTrackMapper.getTrackList(id,pagination);
        //组装资产表 map<id,name>
        List<FixedAsset> allFixedAssetList = fixedAssetMapper.getAllFixedAssetList();
        Map<Integer, String> assetMap = allFixedAssetList.stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        List<SoftwareTrackResponse> softwareTrackList = new ArrayList<>();
        for (SoftwareTrackRequest t : trackList) {
            SoftwareTrackResponse softwareTrackResponse = SoftwareTrackResponse.builder()
                    .fixedAssetId(assetMap.get(t.getFixedAssetId()))
                    .deviceId(assetMap.get(t.getDeviceId()))
                    .lendDescription(t.getLendDescription())
                    .createdAt(t.getCreatedAt())
                    .build();
            softwareTrackList.add(softwareTrackResponse);
        }
        return softwareTrackList;
    }
    /**
     * 获取归属列表总条数
     * */
    public int getTrackCount(Integer id){
        return fixedAssetTrackMapper.getTrackCount(id);
    }

    /**
     * 找出所有分类列表
     * */
    public List<TreeNode> getAll(){
       return  assetCategoryMapper.getAll();
    }

    /**
     * 递归找子集
     * */
    public TreeNode findChildren(TreeNode treeNode,List<TreeNode> treeNodes){
        for (TreeNode it : treeNodes){
            if(treeNode.getId()==it.getParentId()){
                if(treeNode.getChildren()==null){
                    treeNode.setChildren(new ArrayList<>());
                }
                it.setTitle(it.getName());
                it.setValue(it.getId());
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }

    public List<TreeNode> buildByRecursive (List<TreeNode> treeNodes){
        List<TreeNode> trees = new ArrayList<>();
        for (TreeNode treeNode : treeNodes) {
            if( treeNode.getParentId()==3){
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    public List<TreeNode> test(){
        List<TreeNode> trees = buildByRecursive(getAll());
        List<TreeNode> list=new ArrayList<>();
        for (TreeNode treeNode : trees) {
            TreeNode treeNode1 = TreeNode.builder()
                    .id(treeNode.getId())
                    .value(treeNode.getId())
                    .parentId(treeNode.getParentId())
                    .children(treeNode.getChildren())
                    .title(treeNode.getName())
                    .name(treeNode.getName())
                    .build();
            list.add(treeNode1);
        }
        return list;
    }

    /**
     * 是否有该软件分类
     * */
    public AssetCategory isCategory(Integer id){
        return  assetCategoryMapper.isCategory(id);
    }

    /**
     * 是否有该购买途径
     * */
    public BuyRouter isBuyRouter(Integer id){
       return  buyRouterMapper.findDetailById(id);
    }

    /**
     * 是否有该厂商
     * */
    public Vendor isVendor(Integer id){
        return vendorMapper.isVendor(id);
    }

    /**
     * 是否有版本发行方式
     * */
    public Issue isIssue(Integer id){
        return issueMapper.isIssue(id);
    }

    /**
     * 资产表中对应分类id设置为 0
     * */
    public void edit(Integer id){
        fixedAssetMapper.edit(id);
    }
}
