package com.smile.eam.service;

import com.smile.eam.dto.PermissionListReturnDto;
import com.smile.eam.entity.AdminPermission;
import com.smile.eam.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {

    @Resource
    PermissionMapper permissionMapper;

    //查找权限列表
    public List<PermissionListReturnDto> getList(){

        List<PermissionListReturnDto> permissionListReturnDtos = new ArrayList<>();

        List<AdminPermission> permissionAll = permissionMapper.findPermissionAll();

        for (AdminPermission adminPermission : permissionAll) {
            PermissionListReturnDto permissionListReturnDto = new PermissionListReturnDto();
            permissionListReturnDto.setKey(adminPermission.getId());
            permissionListReturnDto.setTitle(adminPermission.getName());
            permissionListReturnDtos.add(permissionListReturnDto);
        }
        return permissionListReturnDtos;
    }

}
