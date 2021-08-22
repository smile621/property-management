package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.dto.DisposeMalfunctionRequest;
import com.smile.eam.dto.MalfunctionResponse;
import com.smile.eam.entity.FixedAsset;
import com.smile.eam.entity.Malfunction;
import com.smile.eam.mapper.FixedAssetMapper;
import com.smile.eam.mapper.MalfunctionMapper;
import com.smile.eam.mapper.StatusMapper;
import com.smile.eam.mapper.UserMapper;
import com.smile.eam.dto.FindByNameDto;
import com.smile.eam.entity.AdminUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MalfunctionService {
    @Resource
    MalfunctionMapper malfunctionMapper;
    @Resource
    FixedAssetMapper fixedAssetMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    StatusMapper statusMapper;

    /**
     *    获取故障总条数
     * */
    public int MalfunctionCount(){
        return malfunctionMapper.MalfunctionCount();
    }

    /**
     * 转换故障列表函数
     * */
    public List<MalfunctionResponse> getMalfunction(List<Malfunction> malfunctionList){
        if(malfunctionList.size()==0){
            return new ArrayList<>();
        }
        //获取设备配件id，名称列表,组装map
        List<FixedAsset> allFixedAssetList = fixedAssetMapper.getAllFixedAssetList();
        Map<Integer, String> deviceAndPartMap = allFixedAssetList.stream().collect(Collectors.toMap(FixedAsset::getId, FixedAsset::getName));
        //组装solveId,名称，map
        List<AdminUser> allUsersList = userMapper.findAllUsersList();
        Map<Integer, String> userMap = allUsersList.stream().collect(Collectors.toMap(AdminUser::getId, AdminUser::getUsername));
        List<MalfunctionResponse> malfunctionResponseList = new ArrayList<>();
        for (Malfunction malfunction : malfunctionList) {
            MalfunctionResponse malfunctionResponse = MalfunctionResponse.builder()
                    .id(malfunction.getId())
                    .fixedAssetId(malfunction.getFixedAssetId())
                    .fixedAsset(deviceAndPartMap.get((malfunction.getFixedAssetId())))
                    .solveId(userMap.get(malfunction.getSolveId()))
                    .malfunctionDescription(malfunction.getMalfunctionDescription())
                    .repairDescription(malfunction.getRepairDescription())
                    .createdAt(malfunction.getCreatedAt())
                    .updatedAt(malfunction.getUpdatedAt())
                    .status(Malfunction.builder().status(malfunction.getStatus()).build().getStatusAlias())
                    .build();
            malfunctionResponseList.add(malfunctionResponse);
        }
        return malfunctionResponseList;
    }

    /**
     * 获取故障列表
     * */
    public List<MalfunctionResponse> malfunctionList(Pagination pagination){
        List<Malfunction> malfunctionList = malfunctionMapper.malfunctionList(pagination);
       return  getMalfunction(malfunctionList);
    }

    /**
     * 查看该故障是否为已完成状态
     * */
    public Boolean isFinished(DisposeMalfunctionRequest disposeMalfunctionRequest){
        Malfunction malfunction = malfunctionMapper.isFinished(disposeMalfunctionRequest);
        if(malfunction==null){
            return false;
        }
        return true;
    }

    /**
     * 判断传入id是否存在
     * */
    public Malfunction isExistMalfunction(Integer id){
       return   malfunctionMapper.isExistMalfunction(id);
    }

    /**
     * 处理故障,返回处理条数
     * */
    public int disposeMalfunction(DisposeMalfunctionRequest disposeMalfunctionRequest,Integer solveId){
        return  malfunctionMapper.disposeMalfunction(disposeMalfunctionRequest, solveId);
    }
    /**
     * 修改资产状态为：0：正常
     * */
    public void updateAssetStatus0(Integer id){
        fixedAssetMapper.updateAssetStatus0(id);
    }
    /**
     * 修改资产状态为：6：处理中
     * */
    public void updateAssetStatus6(Integer id){
        fixedAssetMapper.updateAssetStatus6(id);
    }

    /**
     * 删除故障列表,返回是否成功
     * */
    public Boolean deleteMalfunction(Integer id){
        int malfunction = malfunctionMapper.deleteMalfunction(id);
        if(malfunction<1){
            return false;
        }
        return  true;
    }

    /**
     * 故障模糊搜索
     * */
    public FindByNameDto findByName(String name, Pagination pagination){
        //新建返回list
        List<MalfunctionResponse> malfunctionResponse = new ArrayList<>();
        //根据name，模糊查找故障列表
        List<Malfunction> byName = malfunctionMapper.findByName(name,pagination);
        //转成需要的数据返回
        List<MalfunctionResponse> malfunction = getMalfunction(byName);
        //将返回的数据装到返回List
        malfunctionResponse.addAll(malfunction);
        pagination.setTotal(malfunctionResponse.size());
        FindByNameDto data = FindByNameDto.builder()
                .malfunctionResponse(malfunctionResponse)
                .pagination(pagination)
                .build();
        //获取故障表中软件id并去重
        List<Malfunction> malfunctions = malfunctionMapper.malfunctionList(pagination);
        if(malfunctions.size()==0){
            return data;
        }
        Set<Integer> ids = malfunctions.stream().map((item) -> {
            return item.getFixedAssetId();
        }).collect(Collectors.toSet());
        //根据软件id获取所有符合的资产列表
        List<FixedAsset> allFixedAssetList = fixedAssetMapper.findFixedAssetById(ids);
        //组装资产表  名称 与 ids 的map
        Map<String, Integer> fixedMap = allFixedAssetList.stream().collect(Collectors.toMap(FixedAsset::getName, FixedAsset::getId));
        List<Malfunction> existMalfunction = malfunctionMapper.malfunctionByAssetId(fixedMap.get(name));
        if(existMalfunction.size() != 0){
            List<MalfunctionResponse> malfunction1 = getMalfunction(existMalfunction);
            malfunctionResponse.addAll(malfunction1);
            pagination.setTotal(malfunctionResponse.size());
            return  FindByNameDto.builder()
                    .malfunctionResponse(malfunctionResponse)
                    .pagination(pagination)
                    .build();
        }
        return data;
    }
}
