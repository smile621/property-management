package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.entity.Vendor;
import com.smile.eam.entity.VendorDetail;
import com.smile.eam.entity.VendorUser;
import com.smile.eam.mapper.FixedAssetMapper;
import com.smile.eam.mapper.VendorMapper;
import com.smile.eam.dto.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VendorService {

    @Resource
    VendorMapper vendorMapper;
    @Resource
    FixedAssetMapper fixedAssetMapper;

    /**
     * 创建厂商
     * */
    public int createVendor(VendorCreateRequest request, String time){
        return vendorMapper.insertVendor(request,time);
    }

    /**
    创建插入厂商联系人信息
     */
    public boolean create(VendorUserDto request, int vendorId){
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rows = vendorMapper.insertVendorUser(vendorId,request,time);
        if(rows==0){
            return false;
        }
        return true;
    }

    /**
     * 删除厂商
     * */
    public boolean deleteVendor(int id){
        int row = vendorMapper.deleteVendor(id);
        //删除厂商需要删除资产表里绑定在该厂商的资产记录
        fixedAssetMapper.updateVendorIdWhenDelete(id);
        if(row==0){
            return false;
        }
        return true;
    }

    /**
     * 删除厂商联系人
     * */
    public boolean deleteUser(int id){
        //删除之前查询是否有厂商联系人
        List<VendorUser> users = vendorMapper.findVendorUserByVendorId( id);
        if(users.size()==0){
            //没有厂商联系人
            return false;
        }
        vendorMapper.deleteVendorUser(id);
        return true;
    }

    /**
     * 编辑厂商信息   编辑厂商联系人信息
     * */
    public Boolean editVendor(VendorDto vendorDto, VendorUserDto vendorUserDto){
        VendorUpdateDto vendorUpdateDto = new VendorUpdateDto();
        BeanUtils.copyProperties(vendorDto,vendorUpdateDto);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int rows = vendorMapper.editVendor(vendorUpdateDto,time);
        int col = vendorMapper.editVendorUser(vendorUserDto,time);
        if(rows==0||col==0){
            return false;
        }
        return true;
    }

    /**
     * 根据ID列表获取厂商信息     为null时则返回null
     * */
    public VendorDto getVendor(int vendorIds){
        return vendorMapper.findOneById(vendorIds);
    }

    public List<VendorUserDetailDto> getVendorUserDetail(int vendorId){

        List<VendorUser> vendorUsers = vendorMapper.findAllVendorUserById(vendorId);
        List<VendorUserDetailDto> vendorUserDetailDtoList = new ArrayList<>();

        for (VendorUser vendorUser:vendorUsers){
            VendorUserDetailDto vendorUserDetailDto = new VendorUserDetailDto();
            BeanUtils.copyProperties(vendorUser,vendorUserDetailDto);
            vendorUserDetailDtoList.add(vendorUserDetailDto);
        }

        return vendorUserDetailDtoList;
    }

    /**
     * 获取厂商列表  GET
     * */
    public List<Vendor> vendorList(){
        return vendorMapper.findAllVendor();
    }

    /**
     * 模糊搜索厂商   厂商名称
     * */
    public List<VendorDetail> searchVendorDetail(String name, Pagination pagination){
        List<VendorDetail> vendorList = vendorMapper.findVendorDetailByName(name, pagination);
        pagination.setTotal(vendorList.size());
        return vendorList;
    }

}
