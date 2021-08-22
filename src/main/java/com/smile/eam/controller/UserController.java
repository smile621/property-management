package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.common.Pagination;
import com.smile.eam.service.UserService;
import com.smile.eam.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "用户相关接口")
@RestController
@CrossOrigin
public class UserController {

    @Resource
    UserService userService;

    /**
     *  获取我是谁
     */
    @ApiOperation("获取我是谁API")
    @GetMapping("/api/whoami")
    public JsonResult<whoamiReturnDto> whoami() {
        JsonResult<whoamiReturnDto> jsonResult = new JsonResult<>();
        jsonResult.setData(userService.whoami());
        return jsonResult;
    }

    /**
     *  编辑个人信息
     */
    @ApiOperation("编辑个人信息API")
    @PostMapping("/api/user/update")
    public JsonResult<String> updateMe(@Valid NowUserUpdateDto nowUserUpdateDto){
        boolean code = userService.updateMe(nowUserUpdateDto);
        if(code==false){
            return new JsonResult<>(false,"密码错误");
        }
        return new JsonResult<>();
    }

    /**
     * 获取用户列表
     */
    @ApiOperation("获取用户列表API")
    @GetMapping("/api/organization/getUserList")
    public JsonResult<UserResponseListDto> getUserList(Pagination pagination) {
        List<UserResponseDto> userResponseDtoList = userService.getUserResponse(pagination);
        UserResponseListDto data = UserResponseListDto.builder()
                .pagination(pagination)
                .userResponseDtoList(userResponseDtoList)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 用户列表模糊搜索
     */
    @ApiOperation("用户模糊搜索API")
    @GetMapping("/api/organization/searchUserList")
    public JsonResult<UserResponseListDto> searchUserList(@Valid UserSearchDto userSearchDto, Pagination pagination) {
        List<UserResponseDto> userResponseDtoList = userService.getUserResponse(userSearchDto, pagination);
        UserResponseListDto data = UserResponseListDto.builder()
                .pagination(pagination)
                .userResponseDtoList(userResponseDtoList)
                .build();
        return new JsonResult<>(data);
    }

    /**
     * 新增用户
     */
    @ApiOperation("用户新增API")
    @PostMapping("/api/organization/createUser")
    public JsonResult<String> createUser(@RequestBody @Valid UserCreateDto user) {
        int row = userService.createUser(user);
        if (row == -1) {
            return new JsonResult<>(false, "用户名已存在");
        } else {
            return new JsonResult<>();
        }
    }

    /**
     * 编辑用户
     */
    @ApiOperation("用户编辑API")
    @PostMapping("/api/organization/updateUser")
    public JsonResult<String> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        int row = userService.updateUser(userUpdateDto);
        if (row == -1) {
            return new JsonResult<>(false, "用户名已存在");
        }else {
            return new JsonResult<>();
        }
    }

    /**
     * 用户详情
     */
    @ApiOperation("用户详情显示API")
    @GetMapping("/api/organization/detailUser")
    public JsonResult<UserResponseDto> detailUser(int userId) {
        UserResponseDto userResponseDto = userService.detailUser(userId);
        return new JsonResult<>(userResponseDto);
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户API")
    @PostMapping("/api/organization/deleteUser")
    public JsonResult<String> deleteUser(int userId) {
        userService.deleteUser(userId);
        return new JsonResult<>();
    }

    /**
     * 批量删除用户
     */
    @ApiOperation("批量删除用户API")
    @PostMapping("/api/organization/deleteUsers")
    public JsonResult<String> deleteUsers(@RequestBody @Valid UserDeleteQueryDto userDeleteQueryDto) {
        userService.deleteUsers(userDeleteQueryDto);
        return new JsonResult<>();
    }
}
