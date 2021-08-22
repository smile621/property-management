package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.common.UserContext;
import com.smile.eam.dto.CheckCreateDto;
import com.smile.eam.dto.CheckDetailReturnDto;
import com.smile.eam.dto.CheckListReturnDto;
import com.smile.eam.dto.checkDetailAllReturn;
import com.smile.eam.mapper.CategoryMapper;
import com.smile.eam.mapper.CheckMapper;
import com.smile.eam.mapper.UserMapper;
import com.smile.eam.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

@Service
public class CheckService {

    @Resource
    UserMapper userMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    CheckMapper checkMapper;


    /**
     * 新建盘点项目
     */
    //
    public Boolean createCheck(CheckCreateDto checkCreateDto) {

        if (checkCreateDto.getCategoryId() == Asset.CATEGORY_ID_DEVICE || checkCreateDto.getCategoryId() == Asset.CATEGORY_ID_PART || checkCreateDto.getCategoryId() == Asset.CATEGORY_ID_SOFTWARE) {

            //验证Userid是否存在
            if (userMapper.findUserById(checkCreateDto.getUserId()) == null) {
                throw new RuntimeException("用户不存在");
            }

            //验证分类是否正在盘点
            if (checkMapper.findCheckExist(checkCreateDto.getCategoryId()) == 1) {
                throw new RuntimeException("当前存在项目未结束");
            }

            //获取某分类下面的全部子节点

            //新建一个List，把递归之后的每一个category存到里面
            List<Category> categoryList = new ArrayList<>();
            //查配件分类下的第一级
            List<Category> categoryListFirstSon = categoryMapper.findCategorySonList(checkCreateDto.getCategoryId());
            //调用递归
            findCategorySonMap(categoryList, categoryListFirstSon);

            List<String> categoryLists = new ArrayList<>();
            for (Category category : categoryList) {
                categoryLists.add(String.valueOf(category.getId()));
            }
            categoryLists.add(String.valueOf(checkCreateDto.getCategoryId()));
            //通过全部子节点找到所有对应的资产装到List里面，状态正常的

            //通过分类id去资产表中找状态正常的资产有多少个
            List<FixedAsset> winCountCategory = checkMapper.findCategoryAsset(categoryLists);

            //在表里新建项目
            BigDecimal item = new BigDecimal(0.00);
            checkMapper.createCheck(checkCreateDto.getUserId(), checkCreateDto.getCategoryId(), checkCreateDto.getPredictAt(), winCountCategory.size(),item,item);
            //获得最新主键
            int checkId = checkMapper.findCheckKey();

            for (FixedAsset fixedAsset : winCountCategory) {
                //在盘点详情表里添加相对应的资产数据
                checkMapper.createCheckDetail(checkId, fixedAsset.getId(), fixedAsset.getAssetCategoryId(), Check.STATUS_NONE_CHECK, fixedAsset.getName(),fixedAsset.getPrice());
                //向盘点记录表中添加相对应的资产数据
            }

            return true;
        } else {
            throw new RuntimeException("不能添加的分类");
        }
    }

    /**
     * 分类递归函数
     */
    public void findCategorySonMap(List<Category> categoryList, List<Category> categoryItem) {

        for (Category category : categoryItem) {
            categoryList.add(category);
            List<Category> categories = categoryMapper.findCategorySonList(category.getId());
            findCategorySonMap(categoryList, categories);
        }
    }

    /**
     * 拉取盘点项目列表详情
     */
    public checkDetailAllReturn getCheckDetail(int checkId, Pagination pagination) {

        List<CheckDetail> checkDetails = checkMapper.findDetailByCheckId(checkId);
        List<CheckDetailReturnDto> checkDetailReturns = new ArrayList<>();

        //去盘点表里查出所有的盘点信息
        List<Check> checks = checkMapper.findCheckAll(pagination);

        //组装状态Map
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(Asset.STATUS_BEING_PROCESSED, "处理中");
        statusMap.put(Asset.STATUS_FIGURE_OUT, "处理完成");
        statusMap.put(Asset.STATUS_NONE_CHECK, "未盘点");
        statusMap.put(Asset.STATUS_ACCOMPLISH_CHECK, "正常");
        statusMap.put(Asset.STATUS_DISCONTINUE, "中止");
        statusMap.put(Asset.STATUS_LOSE, "丢失");

        //组装负责人Map
        Map<Integer, String> checkIdUserId = new HashMap<>();
        Set<Integer> userIdSet = new HashSet<>();
        for (Check check : checks) {
            userIdSet.add(check.getDutyId());
            checkIdUserId.put(check.getId(), String.valueOf(check.getDutyId()));
        }
        List<AdminUser> userList = userMapper.findUserListById(userIdSet);
        Map<Integer, String> userIdName = new HashMap<>();
        for (AdminUser user : userList) {
            userIdName.put(user.getId(), user.getNickname());
        }
        for (Integer integer : checkIdUserId.keySet()) {
            checkIdUserId.put(integer, userIdName.get(Integer.parseInt(checkIdUserId.get(integer))));
        }
        Map<Integer, String> IdUserMap = new HashMap<>();
        List<AdminUser> userAllList = checkMapper.findUserAll();
        for (AdminUser user : userAllList) {
            IdUserMap.put(user.getId(), user.getNickname());
        }

        if (checkDetails.size() != 0) {
            for (CheckDetail checkDetail : checkDetails) {
                CheckDetailReturnDto checkDetailReturn = new CheckDetailReturnDto();
                checkDetailReturn.setId(checkDetail.getId());
                checkDetailReturn.setCheckId(checkDetail.getCheckId());
                checkDetailReturn.setCheckUserName(checkIdUserId.get(checkId));
                checkDetailReturn.setAssetId(checkDetail.getAssetId());
                checkDetailReturn.setAssetName(checkDetail.getAssetName());
                checkDetailReturn.setCreatedAt(checkDetail.getCreatedAt());
                checkDetailReturn.setUpdatedAt(checkDetail.getUpdatedAt());
                checkDetailReturn.setStatus(statusMap.get(checkDetail.getStatus()));
                checkDetailReturn.setDescription(checkDetail.getDescription());
                checkDetailReturn.setCheckUserNickname(IdUserMap.get(checkDetail.getCheckUserId()));
                checkDetailReturn.setPrice(checkDetail.getPrice());
                checkDetailReturns.add(checkDetailReturn);
            }
        }

        //组装分类Map
        List<Category> category = checkMapper.findCategoryAll();
        Map<Integer, String> categoryMap = new HashMap<>();
        for (Category categoryItem : category) {
            categoryMap.put(categoryItem.getId(), categoryItem.getName());
        }

        //查看盘点表里某一条数据

        checkDetailAllReturn checkDetailAllReturn = new checkDetailAllReturn();
        Check check = checkMapper.findCheckByIdOne(checkId);
        checkDetailAllReturn.setCheckId(check.getId());
        checkDetailAllReturn.setCategory(categoryMap.get(check.getAssetCategoryId()));
        checkDetailAllReturn.setDuty(checkIdUserId.get(checkId));
        checkDetailAllReturn.setStatus(statusMap.get(check.getStatus()));
        checkDetailAllReturn.setTotalCount(check.getTotalCount());
        checkDetailAllReturn.setWinCount(check.getWinCount());
        checkDetailAllReturn.setLossCount(check.getLossCount());
        checkDetailAllReturn.setWaitCount(check.getWaitCount());
        checkDetailAllReturn.setLossPrice(check.getLossPrice());
        checkDetailAllReturn.setFindPrice(check.getFindPrice());
        if (check.getCreatedAt()!=null){
            checkDetailAllReturn.setCreatedAt(DateFormat.getDateInstance().format(check.getCreatedAt()));
        }
        if (check.getPredictAt()!=null){
            checkDetailAllReturn.setPredictAt(DateFormat.getDateInstance().format(check.getPredictAt()));
        }
       if (check.getEndAt()!=null){
           checkDetailAllReturn.setEndAt(DateFormat.getDateInstance().format(check.getEndAt()));
       }
        checkDetailAllReturn.setCheckDetailReturn(checkDetailReturns);

        return checkDetailAllReturn;

    }

    /**
     * 拉取盘点项目列表
     */
    public List<CheckListReturnDto> getCheckList(Pagination pagination, String title) {

        List<CheckListReturnDto> checkListReturnDtoList = new ArrayList<>();
        //去盘点表里查出所有的盘点信息
        List<Check> checks = checkMapper.findCheckAll(pagination);

        if (title != null && !title.equals("")) {

            Set<Integer> checkIdList = new HashSet<>();
            //模糊查询负责人名字
            List<AdminUser> userDims = userMapper.findUserDim(title);
            List<Integer> userIdList = new ArrayList<>();
            for (AdminUser userDim : userDims) {
                userIdList.add(userDim.getId());
            }

            //通过userid去查盘点信息
            if (userIdList.size() != 0) {
                List<Integer> checkIds1 = checkMapper.findCheckIdByUserId(userIdList);
                if (checkIds1.size() != 0) {
                    for (Integer integer : checkIds1) {
                        checkIdList.add(integer);
                    }
                }
            }

            //模糊查询分类
            List<Integer> categoryList = categoryMapper.findCategoryIdDim(title);
            //通过分类id去查盘点分类
            if (categoryList.size() != 0) {
                List<Integer> checkIds2 = checkMapper.findCheckIdByCategoryId(categoryList);
                if (checkIds2.size() != 0) {
                    for (Integer integer : checkIds2) {
                        checkIdList.add(integer);
                    }
                }
            }

            if (checkIdList.size() == 0){
                return null;
            }

            //通过盘点项目id找到对应的盘点项目
            checks.clear();
            List<Check> checksItem = checkMapper.findCheckById(checkIdList);
            for (Check check : checksItem) {
                checks.add(check);
            }

        }

        //去盘点表里查出状态为进行中的所有盘点信息
        List<Check> checkStatus = checkMapper.findCheckByStatus(Asset.STATUS_BEING_PROCESSED);

        List<Integer> checkStatusList = new ArrayList<>();
        for (Check status : checkStatus) {
            checkStatusList.add(status.getId());
        }

        //组装分类Map
        Map<Integer, String> categoryMap = new HashMap<>();
        Set<Integer> categoryIdSet = new HashSet<>();
        for (Check check : checks) {
            categoryIdSet.add(check.getAssetCategoryId());
        }

        List<Category> categoryList = new ArrayList<>();
        if (categoryIdSet.size() != 0) {
            categoryList = categoryMapper.findCategoriesByIds(categoryIdSet);
            for (Category category : categoryList) {
                categoryMap.put(category.getId(), category.getName());
            }
        }
        //组装负责人Map
        Map<Integer, String> userMap = new HashMap<>();
        Set<Integer> userIdSet = new HashSet<>();
        for (Check check : checks) {
            userIdSet.add(check.getDutyId());
        }
        List<AdminUser> userList = new ArrayList<>();
        if (userIdSet.size() != 0) {
            userList = userMapper.findUserListById(userIdSet);
            for (AdminUser user : userList) {
                userMap.put(user.getId(), user.getNickname());
            }
        }
        //组装状态Map
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(Asset.STATUS_BEING_PROCESSED, "处理中");
        statusMap.put(Asset.STATUS_FIGURE_OUT, "处理完成");
        statusMap.put(Asset.STATUS_NONE_CHECK, "未盘点");
        statusMap.put(Asset.STATUS_ACCOMPLISH_CHECK, "以盘点");
        statusMap.put(Asset.STATUS_DISCONTINUE, "中止");

        for (Check item : checks) {

            CheckListReturnDto checkListReturnDto = new CheckListReturnDto();
            checkListReturnDto.setCheckId(item.getId());
            checkListReturnDto.setCategory(categoryMap.get(item.getAssetCategoryId()));
            checkListReturnDto.setDuty(userMap.get(item.getDutyId()));
            if (item.getCreatedAt()!=null){
                checkListReturnDto.setCreatedAt(DateFormat.getDateInstance().format(item.getCreatedAt()));
            }
            if (item.getPredictAt()!=null){
                checkListReturnDto.setPredictAt(DateFormat.getDateInstance().format(item.getPredictAt()));
            }
            if (item.getEndAt()!=null){
                checkListReturnDto.setEndAt(DateFormat.getDateInstance().format(item.getEndAt()));
            }
            checkListReturnDto.setStatus(statusMap.get(item.getStatus()));
            checkListReturnDto.setTotalCount(item.getTotalCount());
            checkListReturnDto.setWinCount(item.getWinCount());
            checkListReturnDto.setLossCount(item.getLossCount());
            checkListReturnDto.setWaitCount(item.getWaitCount());
            checkListReturnDto.setLossPrice(item.getLossPrice());
            checkListReturnDto.setFindPrice(item.getFindPrice());
            checkListReturnDtoList.add(checkListReturnDto);
        }

        return checkListReturnDtoList;
    }

    /**
     * 更新盘点资产状态
     */
    public int updateCheckStatus(int assetId, int checkId, String description, int status) {
        if (description == null) {
            description = "";
        }
        //验证盘点项目是否存在
        if (checkMapper.findCheckIdExist(checkId) == 0) {
            throw new RuntimeException("不存在的盘点项目ID");
        }

        //验证盘点项目详情中是否存在此资产id
        if (checkMapper.findCheckDetailIdExist(checkId, assetId) == 0) {
            throw new RuntimeException("不存在的盘点项目ID");
        }

        //验证更新状态是否正确
        if (!(status == Asset.STATUS_ACCOMPLISH_CHECK || status == Asset.STATUS_LOSE)) {
            throw new RuntimeException("不存在的更新状态");
        }

        //更改当前项目盘点数
        BigDecimal priceItem = checkMapper.findCheckDetailPrice(checkId,assetId);
        //盘到
        if (status == Asset.STATUS_ACCOMPLISH_CHECK) {
            //将盘盈数量加一，未盘数量减一
            checkMapper.updateCheckDetailCountUp(checkId,priceItem);

        }
        //丢失
        if (status == Asset.STATUS_LOSE) {
            //将盘亏数量加一，未盘数量减一
            checkMapper.updateCheckDetailCountDown(checkId,priceItem);
        }

        //验证项目是否第一次开始，如果是第一次，然后修改状态为盘点中
        //查看该项目下面时候仅有传入值一个盘点项目，如果时则把项目改成处理完成
        List<Integer> noneCheck = checkMapper.findCheckDetailNumber(checkId, Asset.STATUS_NONE_CHECK);
        if (noneCheck.size() == 1) {
            for (Integer integer : noneCheck) {
                if (integer == assetId) {
                    //将该项目状态更改为处理完成
                    checkMapper.updateCheckStatusOne(checkId, new Date(), Asset.STATUS_FIGURE_OUT);
                }
            }
        }
        int itemNumber = checkMapper.findCheckDetailNumberAll(checkId);
        if (noneCheck.size() == itemNumber) {
            //将该项目状态更改为处理中
            checkMapper.updateCheckStatusOne(checkId, null, Asset.STATUS_BEING_PROCESSED);
        }

        //将状态改为以盘点
        return checkMapper.updateCheckDetail(checkId, assetId, description, UserContext.getUser().getId(), status);

    }

    /**
     * 拉取盘点记录列表
     */
    public List<CheckDetailReturnDto> getCheckTrack() {
        List<CheckDetailReturnDto> checkDetailReturns = new ArrayList<>();
        List<CheckDetail> checkDetails = checkMapper.findCheckDetailAll();

        //组装状态Map
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(Asset.STATUS_ACCOMPLISH_CHECK, "盘盈");
        statusMap.put(Asset.STATUS_LOSE, "盘亏");
        statusMap.put(Asset.STATUS_NONE_CHECK, "未盘点");

        //组装盘点人员Map
        //通过所有的盘点项目id查找盘点人员
        Map<Integer, String> dutyMap = new HashMap<>();
        Map<Integer, String> checkIdDuty = new HashMap<>();
        Set<Integer> checkIds = checkMapper.findCheckIdByDetail();
        Set<Integer> dutyIds = checkMapper.findDutyIdByCheckIds(checkIds);
        List<AdminUser> userList = userMapper.findUserListById(dutyIds);
        for (AdminUser user : userList) {
            dutyMap.put(user.getId(), user.getNickname());
        }

        List<Check> checks = checkMapper.findCheck();
        for (Check check : checks) {
            checkIdDuty.put(check.getId(), dutyMap.get(check.getDutyId()));
        }

        Map<Integer, String> IdUserMap = new HashMap<>();
        List<AdminUser> userAllList = checkMapper.findUserAll();
        for (AdminUser user : userAllList) {
            IdUserMap.put(user.getId(), user.getNickname());
        }

        for (CheckDetail checkDetail : checkDetails) {
            CheckDetailReturnDto checkDetailReturn = new CheckDetailReturnDto();
            checkDetailReturn.setId(checkDetail.getId());
            checkDetailReturn.setCheckId(checkDetail.getCheckId());
            checkDetailReturn.setAssetName(checkDetail.getAssetName());
            checkDetailReturn.setStatus(statusMap.get(checkDetail.getStatus()));
            checkDetailReturn.setCheckUserName(dutyMap.get(checkDetail.getCheckId()));
            checkDetailReturn.setCheckUserName(checkIdDuty.get(checkDetail.getCheckId()));
            checkDetailReturn.setCreatedAt(checkDetail.getCreatedAt());
            checkDetailReturn.setUpdatedAt(checkDetail.getUpdatedAt());
            checkDetailReturn.setAssetId(checkDetail.getAssetId());
            checkDetailReturn.setCheckUserNickname(IdUserMap.get(checkDetail.getCheckUserId()));
            checkDetailReturns.add(checkDetailReturn);
        }

        return checkDetailReturns;
    }

    /**
     * 盘点完成
     */
    public int performCheck(int checkId) {
        //验证盘点项目id是否存在
        //更改盘点项目为完成
        return checkMapper.updateCheckStatus(checkId, Asset.STATUS_FIGURE_OUT);
    }

    /**
     * 盘点中止
     */
    public int discontinueCheck(int checkId) {
        //验证盘点项目id是否存在
        //更改盘点项目为中止STATUS_DISCONTINUE
        return checkMapper.updateCheckStatus(checkId, Asset.STATUS_DISCONTINUE);
    }

    /**
     * 查看user列表
     */
    public List<AdminUser> getUserList() {
        List<AdminUser> userList = checkMapper.findUserAll();
        return userList;
    }

}
