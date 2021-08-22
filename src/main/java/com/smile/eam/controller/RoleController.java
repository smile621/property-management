package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.service.RoleService;
import com.smile.eam.dto.*;
import com.smile.eam.entity.AdminRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "角色相关接口")
@RestController
@CrossOrigin
public class RoleController {

    @Resource
    RoleService roleService;

    /**
     * 新增角色
     */
    @ApiOperation("新增角色API")
    @PostMapping("/api/organization/createRole")
    public JsonResult<String> createRole(@RequestBody @Valid RoleCreateDto roleCreateDto) {
        int row = roleService.createRole(roleCreateDto);
        if (row == -1) {
            return new JsonResult<>(false, "角色名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 获取角色列表以及角色模糊搜索
     */
    @ApiOperation("角色名模糊搜索API")
    @GetMapping("/api/organization/searchRoleList")
    public JsonResult<AdminRoleDto> searchRoleList(RoleSearchDto roleSearchDto, Pagination pagination) {
        List<AdminRole> adminRoles = roleService.searchRoleList(roleSearchDto, pagination);
        AdminRoleDto dto = AdminRoleDto.builder()
                .adminRoles(adminRoles)
                .pagination(pagination)
                .build();
        return new JsonResult<>(dto);
    }

    /**
     * 角色编辑
     */
    @ApiOperation("角色编辑API")
    @PostMapping("/api/organization/updateRole")
    public JsonResult<String> updateRole(@RequestBody @Valid RoleUpdateDto roleUpdateDto) {
        roleService.updateRole(roleUpdateDto);
        return new JsonResult<>();
    }

    /**
     * 角色删除
     */
    @ApiOperation("角色删除API")
    @PostMapping("/api/organization/deleteRole")
    public JsonResult<String> deleteRole(int roleId) {
        roleService.deleteRole(roleId);
        return new JsonResult<>();
    }

    /**
     * 角色详情显示
     */
    @ApiOperation("角色详情API")
    @GetMapping("/api/organization/roleDetails")
    public JsonResult<AdminRoleDetailDto> getRoleDetails(int roleId) {
        return new JsonResult<>(roleService.getRoleDetails(roleId));
    }

    /**
     * 角色批量删除
     */
    @ApiOperation("角色批量删除API")
    @PostMapping("/api/organization/deleteQuery")
    public JsonResult<String> deleteQuery(@RequestBody @Valid RoleDeleteQueryDto roleDeleteQueryDto) {
        roleService.deleteQuery(roleDeleteQueryDto);
        return new JsonResult<>();
    }
}
