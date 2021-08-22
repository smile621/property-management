package com.smile.eam.controller;


import com.smile.eam.dto.PermissionListReturnDto;
import com.smile.eam.service.PermissionService;
import com.smile.eam.common.JsonResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
public class PermissionController {

    @Resource
    PermissionService permissionService;


    /**
     * 拉去权限列表
     */
    @GetMapping("/api/permission/getList")
    public JsonResult<List<PermissionListReturnDto>> getList(){
        JsonResult<List<PermissionListReturnDto>> jsonResult = new JsonResult<>();
        List<PermissionListReturnDto> adminPermissions = permissionService.getList();
        jsonResult.setData(adminPermissions);
        return jsonResult;
    }

}
