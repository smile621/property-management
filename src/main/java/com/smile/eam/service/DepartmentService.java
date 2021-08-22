package com.smile.eam.service;


import com.smile.eam.common.Pagination;
import com.smile.eam.dto.DepartmentCreateDto;
import com.smile.eam.dto.DepartmentDetailDto;
import com.smile.eam.dto.DepartmentDto;
import com.smile.eam.dto.DepartmentUpdateDto;
import com.smile.eam.mapper.DepartmentMapper;
import com.smile.eam.mapper.UserMapper;
import com.smile.eam.entity.AdminDepartment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    UserMapper userMapper;

    /**
     * 获取部门列表
     */
    public List<DepartmentDto> getDepartmentList(Pagination pagination) {

        //页码,总条数
        pagination.setTotal(departmentMapper.departmentCount());

        //父级部门列表
        List<AdminDepartment> departmentList = departmentMapper.findAllDepartment(pagination);

        //父级部门id列表
        Set<Integer> collect = departmentList.stream().map(AdminDepartment::getId).collect(Collectors.toSet());

        //父级部门id和名字对应关系
        Map<Integer, String> collect2 = departmentList.stream().collect(Collectors.toMap(AdminDepartment::getId, AdminDepartment::getName));

        //子级部门列表
        List<AdminDepartment> sonDepartment = new ArrayList<>();
        if (!collect.isEmpty()) {
            sonDepartment = departmentMapper.findSonDepartment(collect);
        }

        //通过父级进行分类
        Map<Integer, List<AdminDepartment>> collect1 = sonDepartment.stream().collect(Collectors.groupingBy(AdminDepartment::getParentId));
        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        for (Integer integer : collect) {

            //空指针异常处理
            List<Integer> collect3 = new ArrayList<>();
            List<String> collect4 = new ArrayList<>();
            if (collect1.get(integer) != null) {
                collect3 = collect1.get(integer).stream().map(AdminDepartment::getId).collect(Collectors.toList());
                collect4 = collect1.get(integer).stream().map(AdminDepartment::getName).collect(Collectors.toList());
            }

            DepartmentDto departmentDto = DepartmentDto.builder()
                    .id(integer)
                    .name(collect2.get(integer))
                    .idList(collect3)
                    .nameList(collect4)
                    .build();
            departmentDtoList.add(departmentDto);
        }

        return departmentDtoList;
    }

    /**
     * 新增部门
     */
    public int createDepartment(DepartmentCreateDto departmentCreateDto) {

        if (departmentMapper.findDepartmentIdByName(departmentCreateDto.getName()) != null) {
            return -1;
        }
        return departmentMapper.createDepartment(departmentCreateDto);
    }

    /**
     * 编辑部门
     */
    public int updateDepartment(DepartmentUpdateDto departmentUpdateDto) {

        //改部门名先判断部门名是否已存在
        if (departmentMapper.findDepartmentIdByName(departmentUpdateDto.getDepartmentName()) != null &&
                Integer.parseInt(departmentMapper.findDepartmentIdByName(departmentUpdateDto.getDepartmentName())) != departmentUpdateDto.getDepartmentId()) {
            return -1;
        }
        return departmentMapper.updateDepartment(departmentUpdateDto);
    }

    /**
     * 删除部门
     */
    public void deleteDepartment(int departmentId) {
        departmentMapper.deleteDepartment(departmentId);
        departmentMapper.deleteUserDepartment(departmentId);
    }

    /**
     * 部门详情
     */
    public DepartmentDetailDto detailDepartment(int departmentId) {

        AdminDepartment detail = departmentMapper.getDetail(departmentId);

        int parentId = detail.getParentId();
        String parentName = "";
        if (parentId != 0) {
            parentName = departmentMapper.findDepartmentById(parentId);
        }

        int principalPersonId = detail.getPrincipalPersonId();
        String principalPersonName = "";
        if (principalPersonId != 0) {
            principalPersonName = userMapper.findUserById(principalPersonId).getUsername();
        }

        DepartmentDetailDto dto = DepartmentDetailDto.builder()
                .name(detail.getName())
                .id(detail.getId())
                .principalPerson(principalPersonName)
                .parentId(parentId)
                .parentName(parentName)
                .principalPersonId(principalPersonId)
                .build();

        return dto;
    }
}
