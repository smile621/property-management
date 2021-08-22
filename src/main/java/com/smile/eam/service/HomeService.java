package com.smile.eam.service;

import com.smile.eam.common.UserContext;
import com.smile.eam.mapper.CategoryMapper;
import com.smile.eam.mapper.HomeMapper;
import com.smile.eam.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class HomeService {

    @Resource
    HomeMapper homeMapper;

    @Resource
    CategoryMapper categoryMapper;

    /**
     * 获取主页信息
     */
    public HomeReturn getHome() throws ParseException {

        AdminUser myUser = UserContext.getUser();
        BigDecimal myAsset = new BigDecimal("0.00");
        //找到归属于我的设备ID
        List<FixedAsset> myDevices = homeMapper.findAssetByUserId(myUser.getId(), Asset.STATUS_DELETE);
        List<Integer> myDeviceIds = new ArrayList<>();
        for (FixedAsset myDevice : myDevices) {
            myDeviceIds.add(myDevice.getId());
            myAsset = myAsset.add(myDevice.getPrice());
        }
        if (myDeviceIds.size()!=0){
        //通过设备id列表查出所有子设备 //我的资产总价格 myAsset
        List<FixedAsset> myDeviceSons = homeMapper.findAssetByDeviceIds(myDeviceIds, Asset.STATUS_DELETE);
        if (myDeviceSons.size()!=0){
            for (FixedAsset myDeviceSon : myDeviceSons) {
                myAsset = myAsset.add(myDeviceSon.getPrice());
            }
        }
        }

        //获取我的代办进行中的个数 //我的代办个数 myTodo
        int myTodo = 0;
        myTodo = homeMapper.findTodoStatus(Asset.STATUS_FIGURE_OUT, Asset.STATUS_DELETE,myUser.getId());

        //获取我的故障处理个数 //我需处理的故障个数 myMalfunction
        int myMalfunction = 0;
        myMalfunction = homeMapper.findMalfunction(myUser.getId(), Asset.STATUS_BEING_PROCESSED);

        //获取服务异常处理个数 //服务异常个数 myService
        int myService = 0;
        myService = homeMapper.findServiceErrorNumber(Asset.CATEGORY_ID_SERVICE, Asset.STATUS_LEISURE);


        //获取设备总价值
        BigDecimal DevicePrice = new BigDecimal("0.00");
        List<Integer> DeviceCategorySonList = findCategorySon(Asset.CATEGORY_ID_DEVICE);
        DeviceCategorySonList.add(Asset.CATEGORY_ID_DEVICE);
        //设备类子类 //DeviceCategorySonList

        //查询所有子类资产的所有价格
        List<BigDecimal> DevicePriceList = homeMapper.findPriceListByCategoryId(DeviceCategorySonList, Asset.STATUS_DELETE);
        for (BigDecimal s : DevicePriceList) {
            DevicePrice = DevicePrice.add(s);
        }
        //设备总价格 //DevicePrice

        //获取配件总价值
        BigDecimal PartPrice = new BigDecimal("0.00");
        List<Integer> PartCategorySonList = findCategorySon(Asset.CATEGORY_ID_PART);
        PartCategorySonList.add(Asset.CATEGORY_ID_PART);
        //配件类子类 //PartCategorySonList

        //查询所有子类资产的所有价格
        List<BigDecimal> PartPriceList = homeMapper.findPriceListByCategoryId(PartCategorySonList, Asset.STATUS_DELETE);
        for (BigDecimal s : PartPriceList) {
            PartPrice = PartPrice.add(s);
        }
        //配件总价格 //PartPrice

        //获取软件总价值
        BigDecimal SoftwarePrice = new BigDecimal("0.00");
        List<Integer> SoftwareCategorySonList = findCategorySon(Asset.CATEGORY_ID_SOFTWARE);
        SoftwareCategorySonList.add(Asset.CATEGORY_ID_SOFTWARE);
        //软件类子类 //SoftwareCategorySonList

        //查询所有子类资产的所有价格
        List<BigDecimal> SoftwarePriceList = homeMapper.findPriceListByCategoryId(SoftwareCategorySonList, Asset.STATUS_DELETE);
        for (BigDecimal s : SoftwarePriceList) {
            SoftwarePrice = SoftwarePrice.add(s);
        }
        //软件总价格 //SoftwarePrice


        //获取服务总价值
        BigDecimal ServicePrice = new BigDecimal("0.00");
        List<Integer> ServiceCategorySonList = findCategorySon(Asset.CATEGORY_ID_SERVICE);
        ServiceCategorySonList.add(Asset.CATEGORY_ID_SERVICE);
        //服务类子类 //ServiceCategorySonList

        //查询所有子类资产的所有价格
        List<BigDecimal> ServicePriceList = homeMapper.findPriceListByCategoryId(ServiceCategorySonList, Asset.STATUS_DELETE);
        for (BigDecimal s : ServicePriceList) {
            ServicePrice = ServicePrice.add(s);
        }
        //服务总价格 //ServicePrice

        //资产总价值
        BigDecimal AssetTotal = new BigDecimal("0.00");
        AssetTotal = AssetTotal.add(ServicePrice).add(SoftwarePrice).add(PartPrice).add(DevicePrice);
        //资产总价值 //AssetTotal

        //查询最最小时间和最大时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date maxTime = homeMapper.findMaxDate();
        Date minTime = homeMapper.findMinDate();

        List<PriceDate> priceDateAll = new ArrayList<>();
        List<PriceDate> priceDateDevice = new ArrayList<>();
        List<PriceDate> priceDatePart = new ArrayList<>();
        List<PriceDate> priceDateSoftware = new ArrayList<>();
        List<PriceDate> priceDateService = new ArrayList<>();

        if (maxTime != null && minTime != null){
        //查询所有的资产价格和时间
        priceDateAll = homeMapper.findPriceDateAll(Asset.STATUS_DELETE);
        //priceDateAll+查询所有钱 //priceDateAll
        priceDateAll = findPriceDateList(priceDateAll,maxTime,minTime);
        //全部的统计表 //priceDateAll

        //查询设备所有配件和时间
        priceDateDevice = homeMapper.findPriceDateByCategoryList(DeviceCategorySonList, Asset.STATUS_DELETE);
        priceDateDevice = findPriceDateList(priceDateDevice,maxTime,minTime);
        //设备的统计表 //priceDateDevice

        //查询所有配件的价格和时间
        priceDatePart = homeMapper.findPriceDateByCategoryList(PartCategorySonList, Asset.STATUS_DELETE);
        priceDatePart = findPriceDateList(priceDatePart,maxTime,minTime);
        //配件的统计表 //priceDatePart

        //查询所有软件的价格和时间
        priceDateSoftware = homeMapper.findPriceDateByCategoryList(SoftwareCategorySonList, Asset.STATUS_DELETE);
        priceDateSoftware = findPriceDateList(priceDateSoftware,maxTime,minTime);
        //软件的统计表 //priceDateSoftware

        //查询所有服务的价格和时间
        priceDateService = homeMapper.findPriceDateByCategoryList(ServiceCategorySonList, Asset.STATUS_DELETE);
        priceDateService = findPriceDateList(priceDateService,maxTime,minTime);
        //服务的统计表 //priceDateService
        }
        //查询故障

        Date MinTimeMalfunction = homeMapper.findMinDateByMalfunction();
        Date MaxTimeMalfunction = homeMapper.findMaxDateByMalfunction();
        Date MalfunctionItem =  MinTimeMalfunction;
        List<Date> dates = homeMapper.findMalfunctionCreatedAll();
        List<MalfunctionStatistics> malfunctionStatistics = new ArrayList<>();

        if (MinTimeMalfunction != null && MaxTimeMalfunction != null) {
            for (int i = 0; i <= getMonth(MinTimeMalfunction, MaxTimeMalfunction); i++) {
                MalfunctionStatistics malfunctionStatistics1 = new MalfunctionStatistics();
                malfunctionStatistics1.setNumber(0);
                for (Date date : dates) {
                    if (format.format(date).equals(format.format(MalfunctionItem))) {
                        malfunctionStatistics1.setNumber(malfunctionStatistics1.getNumber() + 1);
                        malfunctionStatistics1.setCreatedAt(format.format(MalfunctionItem));
                    }
                }
                malfunctionStatistics.add(malfunctionStatistics1);
                MalfunctionItem = format.parse(todayAfterSomeMonths(1, MalfunctionItem));
            }
        }

        //查询服务列表
        List<ServiceAffiliate> serviceAffiliate = homeMapper.findServiceAffiliate(Asset.STATUS_DELETE);

        //已经归属的服务id
        Set<Integer> serviceIds = new HashSet<>();
        for (ServiceAffiliate affiliate : serviceAffiliate) {
            serviceIds.add(affiliate.getAssetId());
        }

        //已经绑定服务的设备id
        Set<Integer> deviceIds = new HashSet<>();
        for (ServiceAffiliate affiliate : serviceAffiliate) {
            deviceIds.add(affiliate.getAssetAffiliate());
        }

        //通过软件id查出软件信息
        List<ServiceHome> serviceAsset = new ArrayList<>();
        if (serviceIds.size()!=0){
            serviceAsset = homeMapper.findAsset(serviceIds, Asset.STATUS_DELETE);
        }

        //组装一个服务资产id和更新时间的Map
        Map<Integer,Date> serviceUpdateAt = new HashMap<>();
        //组装一个服务资产id和状态的Map
        Map<Integer,Integer> serviceStatusMap = new HashMap<>();
        //组装一个服务资产id和服务资产名称的Map
        Map<Integer,String> serviceMap = new HashMap<>();
        for (ServiceHome serviceHome : serviceAsset) {
            serviceMap.put(serviceHome.getId(),serviceHome.getName());
            serviceStatusMap.put(serviceHome.getId(),serviceHome.getStatus());
            serviceUpdateAt.put(serviceHome.getId(),serviceHome.getUpdatedAt());
        }

        //通过资产id查出资产名
        List<ServiceHome> deviceAsset = new ArrayList<>();
        if (deviceIds.size()!=0){
            deviceAsset = homeMapper.findAsset(deviceIds, Asset.STATUS_DELETE);
        }

        //组装一个设备资产id和设备资产名称的Map
        Map<Integer,String> deviceMap = new HashMap<>();
        for (ServiceHome serviceHome : deviceAsset) {
            deviceMap.put(serviceHome.getId(),serviceHome.getName());
        }

        //组装一个异常表Map
        Map<Integer,String> statusMap = new HashMap<>();
        statusMap.put(Asset.STATUS_NORMAL,"正常");
        statusMap.put(Asset.STATUS_AFFILIATION,"归属");
        statusMap.put(Asset.STATUS_PENDING,"待处理");
        statusMap.put(Asset.STATUS_DISCONTINUE,"暂停");
        statusMap.put(Asset.STATUS_MALFUNCTION,"故障");

        //组装
        List<ServiceHomeReturn> serviceHomeReturns = new ArrayList<>();
        for (ServiceAffiliate affiliate : serviceAffiliate) {
            ServiceHomeReturn serviceHomeReturn = new ServiceHomeReturn();
            serviceHomeReturn.setDeviceName(deviceMap.get(affiliate.getAssetAffiliate()));
            serviceHomeReturn.setServiceName(serviceMap.get(affiliate.getAssetId()));
            serviceHomeReturn.setStatus(statusMap.get(serviceStatusMap.get(affiliate.getAssetId())));
            serviceHomeReturn.setStartAt(affiliate.getCreatedAt());
            serviceHomeReturn.setRecoverAt(serviceUpdateAt.get(affiliate.getAssetId()));
            serviceHomeReturns.add(serviceHomeReturn);
        }

        //服务状态列表
        HomeReturn homeReturn = new HomeReturn();
        homeReturn.setMyAsset(myAsset);
        homeReturn.setMyTodo(myTodo);
        homeReturn.setAssetMalfunction(myMalfunction);
        homeReturn.setServiceError(myService);
        homeReturn.setTotalValue(AssetTotal);
        homeReturn.setDeviceValue(DevicePrice);
        homeReturn.setPartValue(PartPrice);
        homeReturn.setServiceValue(ServicePrice);
        homeReturn.setSoftwareValue(SoftwarePrice);
        homeReturn.setTotalValueList(priceDateAll);
        homeReturn.setDeviceValueList(priceDateDevice);
        homeReturn.setPartValueList(priceDatePart);
        homeReturn.setServiceValueList(priceDateService);
        homeReturn.setSoftwareValueList(priceDateSoftware);
        homeReturn.setMalfunctionStatisticsList(malfunctionStatistics);
        homeReturn.setServiceList(serviceHomeReturns);
        return homeReturn;

    }

    //通过开始时间，结束时间，资产列表获得统计图数据流
    public List<PriceDate> findPriceDateList(List<PriceDate> PriceDateList,Date maxTime,Date minTime) throws ParseException {

        List<PriceDate> priceDates = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date item = minTime;
        for (int i = 0 ; i <= getMonth(minTime,maxTime) ; i++){
            PriceDate priceDateItem = new PriceDate();
            priceDateItem.setCreatedAt(format.format(item));
            priceDateItem.setPrice(new BigDecimal("0.00"));
            for (PriceDate priceDate : PriceDateList) {
                if (format.format(format.parse(priceDate.getCreatedAt())).equals(format.format(format.parse(priceDateItem.getCreatedAt())))){
                    priceDateItem.setPrice(priceDateItem.getPrice().add(priceDate.getPrice()));

                }
            }
            priceDates.add(priceDateItem);
            item = format.parse(todayAfterSomeMonths(1,item));
        }
        return priceDates;
    }

    //将当前Date的月份加一
    private String todayAfterSomeMonths(int n , Date dateIn) {
        Date d = dateIn;
        LocalDateTime localDateTime = d.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().plusMonths(n);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    //计算两个Date之间相差几个月
    public  int getMonth(Date start, Date end) {
        if (start.after(end)) {
            Date t = start;
            start = end;
            end = t;
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);
        int year = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1) && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1) && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    //分类递归函数
    public void findCategorySonMap(List<Category> categoryList, List<Category> categoryItem) {

        for (Category category : categoryItem) {
            categoryList.add(category);
            List<Category> categories = categoryMapper.findCategorySonList(category.getId());
            findCategorySonMap(categoryList, categories);
        }
    }

    //查看某个分类下所有的子类
    public List<Integer> findCategorySon(int CategoryId){
        //新建一个List，把递归之后的每一个category存到里面
        List<Category> CategoryList = new ArrayList<>();
        //查配件分类下的第一级
        List<Category> CategoryList2 = categoryMapper.findCategorySonList(CategoryId);
        //调用递归
        findCategorySonMap(CategoryList,CategoryList2);
        List<Integer> CategorySonList = new ArrayList<>();
        for (Category category : CategoryList) {
            CategorySonList.add(category.getId());
        }
        return CategorySonList;
    }

}
