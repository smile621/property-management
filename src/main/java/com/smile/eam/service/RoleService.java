package com.smile.eam.service;

import com.smile.eam.common.Pagination;
import com.smile.eam.mapper.OrganizationMapper;
import com.smile.eam.mapper.RoleMapper;
import com.smile.eam.dto.*;
import com.smile.eam.entity.AdminRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleService {

    @Resource
    RoleMapper roleMapper;
    @Resource
    OrganizationMapper organizationMapper;

    /**
     * 新增角色  以及绑定角色权限
     */
    public int createRole(RoleCreateDto roleCreateDto) {

        if (roleMapper.findOneRoleIdByRoleName(roleCreateDto.getRoleName()) != null) {
            return -1;
        }

        int row = roleMapper.createRole(roleCreateDto);
        int roleId = Integer.parseInt(roleMapper.findOneRoleIdByRoleName(roleCreateDto.getRoleName()));

        //为角色添加权限
        if (!roleCreateDto.getPermissionList().isEmpty()) {
            organizationMapper.createRolePermission(roleId, roleCreateDto.getPermissionList());
        }

        return row;
    }

    /**
     * 角色模糊搜索
     */
    public List<AdminRole> searchRoleList(RoleSearchDto roleSearchDto, Pagination pagination) {

        pagination.setTotal(roleMapper.roleSearchCount(roleSearchDto));
        List<AdminRole> roleList = roleMapper.searchRoles(roleSearchDto, pagination);

        return roleList;
    }

    /**
     * 角色编辑
     */
    public void updateRole(RoleUpdateDto roleUpdateDto) {

        roleMapper.updateRoleName(roleUpdateDto);
        organizationMapper.deleteRolePermission(roleUpdateDto.getId());

        if (!roleUpdateDto.getPermissionList().isEmpty()) {
            organizationMapper.createRolePermission(roleUpdateDto.getId(), roleUpdateDto.getPermissionList());
        }

    }

    /**
     * 角色详情显示
     */
    public AdminRoleDetailDto getRoleDetails(int roleId) {

        AdminRole role = organizationMapper.findRoleDetailsById(roleId);
        List<Integer> permissionList = organizationMapper.searchPermission(roleId);

        AdminRoleDetailDto dto = AdminRoleDetailDto.builder()
                .adminRole(role)
                .permissionList(permissionList)
                .build();

        return dto;
    }

    /**
     * 角色删除
     */
    public void deleteRole(int roleId) {

        roleMapper.deleteRole(roleId);
        roleMapper.deleteUserRole(roleId);

    }

    /**
     * 角色批量删除
     */
    public void deleteQuery(RoleDeleteQueryDto roleDeleteQueryDto) {
        roleMapper.deleteQuery(roleDeleteQueryDto.getIds());
    }

}
