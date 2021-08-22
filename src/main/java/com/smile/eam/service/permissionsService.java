package com.smile.eam.service;

import com.smile.eam.common.UserContext;
import com.smile.eam.mapper.PermissionsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class permissionsService {

    @Resource
    PermissionsMapper permissionsMapper;

    public Boolean verifyPermissions(String tag,String code){

        //查看该用户是否绑定角色
        List<Integer> roleIds = permissionsMapper.findRoleByPermissionsId(UserContext.getUser().getId());

        if (roleIds.size() == 0){
            throw new RuntimeException("PermissionDenied");
        }

        //通过角色ID列表查看所具有的权限
        List<Integer> permissions = permissionsMapper.findPermissionsIdByRoleIds(roleIds);

        if (permissions.size() == 0){
            throw new RuntimeException("PermissionDenied");
        }

        //查看有无此权限
        if (permissionsMapper.findPermissions(permissions,tag,code) == 0){
            throw new RuntimeException("PermissionDenied");
        }

        return true;
    }

}
