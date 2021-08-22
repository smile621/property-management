package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.service.DepartmentService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "部门相关接口")
@RestController
@CrossOrigin
public class DepartmentController {

    @Resource
    DepartmentService departmentService;

    /**
     * 获取部门列表
     */
    @ApiOperation("获取部门列表API")
    @GetMapping("/api/organization/getDepartmentList")
    public JsonResult<AdminDepartmentListDto> getDepartmentList(Pagination pagination) {

        List<DepartmentDto> departmentList = departmentService.getDepartmentList(pagination);

        AdminDepartmentListDto dto = AdminDepartmentListDto.builder()
                .departmentDtos(departmentList)
                .pagination(pagination)
                .build();

        return new JsonResult<>(dto);
    }

    /**
     * 新增部门
     */
    @ApiOperation("新增部门API")
    @PostMapping("/api/organization/createDepartment")
    public JsonResult<String> createDepartment(@Valid DepartmentCreateDto departmentCreateDto) {

        int row = departmentService.createDepartment(departmentCreateDto);

        if (row == -1) {
            return new JsonResult<>(false, "部门名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 部门详情
     */
    @ApiOperation("部门详情API")
    @GetMapping("/api/organization/detailDepartment")
    public JsonResult<DepartmentDetailDto> detailDepartment(int departmentId) {

        DepartmentDetailDto dto = departmentService.detailDepartment(departmentId);

        return new JsonResult<>(dto);
    }

    /**
     * 编辑部门
     */
    @ApiOperation("编辑部门API")
    @PostMapping("/api/organization/updateDepartment")
    public JsonResult<String> updateDepartment(@Valid DepartmentUpdateDto departmentUpdateDto) {

        int row = departmentService.updateDepartment(departmentUpdateDto);

        if (row == -1) {
            return new JsonResult<>(false, "部门名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 删除部门
     */
    @ApiOperation("删除部门API")
    @PostMapping("/api/organization/deleteDepartment")
    public JsonResult<String> deleteDepartment(int departmentId) {

        departmentService.deleteDepartment(departmentId);

        return new JsonResult<>();
    }

}
