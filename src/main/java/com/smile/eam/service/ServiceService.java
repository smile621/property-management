package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.mapper.BuyRouterMapper;
import com.smile.eam.mapper.ServiceMapper;
import com.smile.eam.mapper.VendorMapper;
import com.smile.eam.dto.*;
import com.smile.eam.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceService {

    @Resource
    ServiceMapper serviceMapper;
    @Resource
    BuyRouterMapper buyRouterMapper;
    @Resource
    VendorMapper vendorMapper;

    /**
     * 服务记录清单列表获取API
     */
    public List<ServiceListDto> getList(Pagination pagination) {
        //先查asset_category
        List<FixedAsset> serviceList = serviceMapper.getList(pagination);
        pagination.setTotal(serviceMapper.getCount());

        return getListdemo(serviceList, pagination);
    }

    /**
     * 新增服务记录
     */
    public int create(ServiceCreateDto serviceCreateDto) {
        if (serviceMapper.findByName(serviceCreateDto.getName()) != null) {
            return -1;
        }
        int row = serviceMapper.create(serviceCreateDto);
        return row;
    }

    /**
     * 服务记录清单编辑API
     */
    public int update(ServiceUpdateDto serviceUpdateDto) {
        if (serviceMapper.findByName(serviceUpdateDto.getName()) != null
                && Integer.parseInt(serviceMapper.findByName(serviceUpdateDto.getName())) != serviceUpdateDto.getId()) {
            return -1;
        }
        int row = serviceMapper.update(serviceUpdateDto);
        return row;
    }

    /**
     * 查看服务
     */
    public ServiceDetailDto detail(int id) {

        FixedAsset assetService = serviceMapper.getDetail(id);

        //根据资产ID  查找 service_affiliate  绑定设备ID
        List<Integer> affiliates = serviceMapper.getAffiliates(assetService.getId());

        //根据 service_affiliate 查找 name  fixed_asset
        String buyRouter = "";
        if (assetService.getBuyRouterId() != 0) {
            buyRouter = buyRouterMapper.findDetailById(assetService.getBuyRouterId()).getName();
        }
        String vendorName = "";
        if (assetService.getVendorId() != 0) {
            VendorDto oneById = vendorMapper.findOneById(assetService.getVendorId());
            if (oneById != null) {
                vendorName = oneById.getName();
            }
        }
        Map<Integer, String> fixedAssetMap = new HashMap<>();
        if (affiliates.size() != 0) {
            fixedAssetMap = serviceMapper.getFixedAssetByIds(affiliates).stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        }
        List<String> deviceNames = new ArrayList<>();
        for (Integer affiliate : affiliates) {
            deviceNames.add(fixedAssetMap.get(affiliate));
        }
        ServiceDetailDto serviceDetailDto = ServiceDetailDto.builder()
                .buyAt(assetService.getBuyAt())
                .buyRouterId(assetService.getBuyRouterId())
                .buyRouter(buyRouter)
                .description(assetService.getDescription())
                .deviceNames(deviceNames)
                .expiredAt(assetService.getExpiredAt())
                .id(assetService.getId())
                .name(assetService.getName())
                .price(assetService.getPrice().toString())
                .vendorId(assetService.getVendorId())
                .vendor(vendorName)
                .status(assetService.getStatus())
                .build();
        return serviceDetailDto;
    }

    /**
     * 服务清单模糊搜索API
     */
    public List<ServiceListDto> getSearchList(Pagination pagination, ServiceSearchDto serviceSearchDto) {

        //先查asset_category
        pagination.setTotal(serviceMapper.getSearchCount(serviceSearchDto));
        List<FixedAsset> serviceList = serviceMapper.getSearchList(pagination, serviceSearchDto);

        return getListdemo(serviceList, pagination);
    }

    /**
     *
     */
    public List<ServiceListDto> getListdemo(List<FixedAsset> serviceList, Pagination pagination) {

        //服务列表
        List<ServiceListDto> serviceListDto = new ArrayList<>();

        //筛选出服务列表的服务id
        List<Integer> ids = serviceList.stream().map(FixedAsset::getId).collect(Collectors.toList());

        //服务id(set)
        Set<Integer> idsSet = new HashSet<>(ids);

        //根据服务id列表获取购买途径id列表
        Set<Integer> buyRouterIds = new HashSet<>();
        if (!ids.isEmpty()) {
            buyRouterIds = new HashSet<>(serviceMapper.findBuyRouterIds(ids));
        }

        //获取厂商id——名MAP
        Set<Integer> vendorSet = serviceList.stream().map(FixedAsset::getVendorId).collect(Collectors.toSet());
        Map<Integer, String> vendorMap = new HashMap<>();
        if (!vendorSet.isEmpty()) {
            vendorMap = vendorMapper.findVendorById(vendorSet).stream().collect(Collectors.toMap(Vendor::getId, Vendor::getName));
        }

        //组装id——购买途径Map
        Map<Integer, String> buyRouterMap = new HashMap<>();
        if (buyRouterIds.size() != 0) {
            buyRouterMap = buyRouterMapper.findBuyRouterById(buyRouterIds).stream().collect(Collectors.toMap(BuyRouter::getId, BuyRouter::getName));
        }

        Set<Integer> deviceIdsSet = new HashSet<>();
        Map<Integer, List<ServiceAffiliate>> deviceGroupByServiceId = new HashMap<>();
        if (!idsSet.isEmpty()) {

            //根据服务ids获取绑定的设备ids( Set )
            deviceIdsSet = serviceMapper.findAssetAffiliateById(idsSet).stream().map(ServiceAffiliate::getAssetAffiliate).collect(Collectors.toSet());

            //根据服务id列表分类  获取归属设备id列表
            deviceGroupByServiceId = serviceMapper.findAssetAffiliateById(idsSet).stream().collect(Collectors.groupingBy(ServiceAffiliate::getAssetId));
        }

        //通过资产ID查找资产名//组装   id——设备名   Map
        Map<Integer, String> deviceIdName = new HashMap<>();
        if (!deviceIdsSet.isEmpty()) {
            deviceIdName = serviceMapper.findByIds(deviceIdsSet, pagination).stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        }
        Map<Integer, List<String>> serviceDeviceNameMap = new HashMap<>();

        //Map<Integer,List<Integer>>
        Map<Integer, List<Integer>> serviceDeviceMap = new HashMap<>();
        for (Integer serviceId : idsSet) {
            if (deviceGroupByServiceId.get(serviceId) != null) {
                List<Integer> temp = deviceGroupByServiceId.get(serviceId).stream().map(ServiceAffiliate::getAssetAffiliate).collect(Collectors.toList());
                serviceDeviceMap.put(serviceId, temp);
                List<String> temp2 = new ArrayList<>();
                for (Integer deviceId : temp) {
                    temp2.add(deviceIdName.get(deviceId));
                }
                serviceDeviceNameMap.put(serviceId, temp2);
            }
        }

        for (FixedAsset serviceDto : serviceList) {
            ServiceListDto services = ServiceListDto.builder()
                    .id(serviceDto.getId())
                    .name(serviceDto.getName())
                    .description(serviceDto.getDescription())
                    .status(serviceDto.getStatus())
                    .createdAt(serviceDto.getCreatedAt())
                    .updatedAt(serviceDto.getUpdatedAt())
                    .buyRouterId(serviceDto.getBuyRouterId())
                    .buyRouter(buyRouterMap.get(serviceDto.getBuyRouterId()))
                    .deviceNames(serviceDeviceNameMap.get(serviceDto.getId()))
                    .startAt(serviceDto.getStartAt())
                    .endAt(serviceDto.getEndAt())
                    .price(serviceDto.getPrice().toString())
                    .vendorId(serviceDto.getVendorId())
                    .vendor(vendorMap.get(serviceDto.getVendorId()))
                    .build();
            serviceListDto.add(services);
        }
        return serviceListDto;
    }

    /**
     * 删除API
     */
    public void delete(int id) {
        serviceMapper.delete(id);
        serviceMapper.deleteAffiliate(id);
        serviceMapper.deleteAffiliate2(id);
        serviceMapper.deleteError2(id);
    }

    /**
     * 批量删除API
     */
    public void deleteQuery(ServiceDeleteQueryDto serviceDeleteQueryDto) {
        serviceMapper.deleteQuery(serviceDeleteQueryDto.getIds());
    }

    /**
     * 状态变化
     */
    public void status(int code, int id) {
        serviceMapper.changeStatus(code, id);
    }

    /**
     * 设备绑定
     */
    public void bind(ServiceBindDto serviceBindDto) {
        serviceMapper.bind(serviceBindDto);
    }

    /**
     * 创建异常
     */
    public void createError(ServiceErrorDto serviceErrorDto) {
        serviceMapper.createError(serviceErrorDto);
    }

    /**
     * 按归属有无获取服务列表
     */
    public List<ServiceBindListDto> getAffiliateList(Pagination pagination) {

        List<ServiceBindListDto> serviceBindListDtos = new ArrayList<>();

        //查service_affiliate表
        Set<Integer> idsSet = serviceMapper.getAllIds().stream().collect(Collectors.toSet());
        pagination.setTotal(idsSet.size());
        List<FixedAsset> assets = new ArrayList<>();
        Set<Integer> deviceIdsSet = new HashSet<>();
        Map<Integer, List<ServiceAffiliate>> deviceGroupByServiceId = new HashMap<>();
        if (!idsSet.isEmpty()) {
            deviceIdsSet = serviceMapper.findAssetAffiliateById(idsSet).stream().map(ServiceAffiliate::getAssetAffiliate).collect(Collectors.toSet());
            assets = serviceMapper.findByIds(idsSet, pagination);

            //根据服务id列表分类  获取归属设备id列表
            deviceGroupByServiceId = serviceMapper.findAssetAffiliateById(idsSet).stream().collect(Collectors.groupingBy(ServiceAffiliate::getAssetId));
        }

        //通过资产ID查找资产名//组装   id——设备名   Map
        Map<Integer, String> deviceIdName = new HashMap<>();
        if (!deviceIdsSet.isEmpty()) {
            deviceIdName = serviceMapper.findByIds(deviceIdsSet, pagination).stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        }
        Map<Integer, List<String>> serviceDeviceNameMap = new HashMap<>();

        //Map<Integer,List<Integer>>
        Map<Integer, List<Integer>> serviceDeviceMap = new HashMap<>();
        System.out.println(deviceGroupByServiceId);
        for (Integer serviceId : idsSet) {
            if (deviceGroupByServiceId.get(serviceId) != null) {
                List<Integer> temp = deviceGroupByServiceId.get(serviceId).stream().map(ServiceAffiliate::getAssetAffiliate).collect(Collectors.toList());
                System.out.println(temp);
                serviceDeviceMap.put(serviceId, temp);
                List<String> temp2 = new ArrayList<>();
                for (Integer deviceId : temp) {
                    temp2.add(deviceIdName.get(deviceId));
                }
                serviceDeviceNameMap.put(serviceId, temp2);
            }
        }
        for (FixedAsset asset : assets) {
            ServiceBindListDto serviceBindListDto = ServiceBindListDto.builder()
                    .id(asset.getId())
                    .name(asset.getName())
                    .affiliates(serviceDeviceNameMap.get(asset.getId()))
                    .createdAt(asset.getCreatedAt())
                    .updatedAt(asset.getUpdatedAt())
                    .build();
            serviceBindListDtos.add(serviceBindListDto);
        }
        return serviceBindListDtos;
    }

    /**
     * 模糊搜索
     */
    public List<ServiceBindListDto> searchAffiliateList(ServiceAffiliateSearchDto serviceAffiliateSearchDto, Pagination pagination) {

        List<ServiceBindListDto> serviceBindListDtos = new ArrayList<>();

        //查service_affiliate表
        Set<Integer> idsSet = serviceMapper.getAllSearchIds(serviceAffiliateSearchDto, pagination).stream().collect(Collectors.toSet());
        pagination.setTotal(idsSet.size());
        List<FixedAsset> assets = new ArrayList<>();
        Set<Integer> deviceIdsSet = new HashSet<>();
        Map<Integer, List<ServiceAffiliate>> deviceGroupByServiceId = new HashMap<>();
        if (!idsSet.isEmpty()) {
            deviceIdsSet = serviceMapper.findAssetAffiliateById(idsSet).stream().map(ServiceAffiliate::getAssetAffiliate).collect(Collectors.toSet());
            assets = serviceMapper.findByIds(idsSet, pagination);

            //根据服务id列表分类  获取归属设备id列表
            deviceGroupByServiceId = serviceMapper.findAssetAffiliateById(idsSet).stream().collect(Collectors.groupingBy(ServiceAffiliate::getAssetId));
        }

        //通过资产ID查找资产名//组装   id——设备名   Map
        Map<Integer, String> deviceIdName = new HashMap<>();
        if (!deviceIdsSet.isEmpty()) {
            deviceIdName = serviceMapper.findByIds(deviceIdsSet, pagination).stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        }
        Map<Integer, List<String>> serviceDeviceNameMap = new HashMap<>();

        //Map<Integer,List<Integer>>
        Map<Integer, List<Integer>> serviceDeviceMap = new HashMap<>();
        for (Integer serviceId : idsSet) {
            if (deviceGroupByServiceId.get(serviceId) != null) {
                List<Integer> temp = deviceGroupByServiceId.get(serviceId).stream().map(ServiceAffiliate::getAssetAffiliate).collect(Collectors.toList());
                System.out.println(temp);
                serviceDeviceMap.put(serviceId, temp);
                List<String> temp2 = new ArrayList<>();
                for (Integer deviceId : temp) {
                    temp2.add(deviceIdName.get(deviceId));
                }
                serviceDeviceNameMap.put(serviceId, temp2);
            }
        }
        for (FixedAsset asset : assets) {
            ServiceBindListDto serviceBindListDto = ServiceBindListDto.builder()
                    .id(asset.getId())
                    .name(asset.getName())
                    .affiliates(serviceDeviceNameMap.get(asset.getId()))
                    .createdAt(asset.getCreatedAt())
                    .updatedAt(asset.getUpdatedAt())
                    .build();
            serviceBindListDtos.add(serviceBindListDto);
        }
        return serviceBindListDtos;
    }

    /**
     * 解除归属
     */
    public void deleteAffiliate(int id) {
        serviceMapper.deleteAffiliate(id);
    }

    /**
     * 异常列表
     */
    public List<ServiceErrorListDto> getErrorList(Pagination pagination) {

        List<ServiceError> allError = serviceMapper.getAllError(pagination);
        List<ServiceErrorListDto> list = new ArrayList<>();

        //故障服务id
        List<Integer> errorServiceIds = allError.stream().map(ServiceError::getAssetId).collect(Collectors.toList());
        pagination.setTotal(errorServiceIds.size());
        Map<Integer, String> errorServiceMap = new HashMap<>();
        if (!errorServiceIds.isEmpty()) {
            errorServiceMap = serviceMapper.getFixedAssetByIds(errorServiceIds).stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        }
        for (ServiceError item : allError) {
            ServiceErrorListDto serviceErrorListDto = ServiceErrorListDto.builder()
                    .id(item.getId())
                    .serviceId(item.getAssetId())
                    .name(errorServiceMap.get(item.getAssetId()))
                    .description(item.getDescription())
                    .startAt(item.getStartAt())
                    .endAt(item.getEndAt())
                    .errorStatus(item.getErrorStatus())
                    .build();
            list.add(serviceErrorListDto);
        }
        return list;
    }

    /**
     * 异常处理
     */
    public void deleteError(ServiceErrorResolveDto serviceErrorResolveDto) {
        serviceMapper.deleteError(serviceErrorResolveDto);
    }

    /**
     * 异常模糊搜索
     */
    public List<ServiceErrorListDto> searchErrorList(ServiceAffiliateSearchDto serviceAffiliateSearchDto, Pagination pagination) {

        List<ServiceError> allError = serviceMapper.searchError(serviceAffiliateSearchDto, pagination);
        List<ServiceErrorListDto> list = new ArrayList<>();

        //故障服务id
        List<Integer> errorServiceIds = allError.stream().map(ServiceError::getAssetId).collect(Collectors.toList());
        pagination.setTotal(errorServiceIds.size());
        Map<Integer, String> errorServiceMap = new HashMap<>();
        if (!errorServiceIds.isEmpty()) {
            errorServiceMap = serviceMapper.getFixedAssetByIds(errorServiceIds).stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        }
        for (ServiceError item : allError) {
            ServiceErrorListDto serviceErrorListDto = ServiceErrorListDto.builder()
                    .id(item.getId())
                    .serviceId(item.getAssetId())
                    .name(errorServiceMap.get(item.getAssetId()))
                    .description(item.getDescription())
                    .startAt(item.getStartAt())
                    .endAt(item.getEndAt())
                    .errorStatus(item.getErrorStatus())
                    .build();
            list.add(serviceErrorListDto);
        }
        return list;
    }


}
