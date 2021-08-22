package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.dto.UserLoginRequest;
import com.smile.eam.service.TokenService;
import com.smile.eam.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class TokenController {
    @Resource
    UserService userService;
    @Resource
    TokenService tokenService;

    /**
     * 登录接口
     */
    @ApiOperation("登陆接口")
    @PostMapping("/api/user/login")
    public JsonResult create(@Valid UserLoginRequest userLoginRequest) {

        Map<String, String> result = new HashMap<>();
        //判断用户名，密码是否存在
        Boolean flag = userService.findUserByUsernameAndPassword(userLoginRequest.getUsername(), userLoginRequest.getPasswordHash());
        if (flag) {
            //用户名存在，添加token
            String token = userService.addToken(userLoginRequest.getUsername(), userLoginRequest.getPasswordHash());
            result.put("token", token);
            return new JsonResult(result);
        }
        return new JsonResult("ERROR", "用户名或密码不匹配");
    }

    /**
     * 退出登录
     * */
    @ApiOperation("退出登陆接口")
    @PostMapping("/api/user/delete")
    public JsonResult deleteUser(String token){
        //删除token对应表
        Boolean t = tokenService.deleteToken(token);
        if(!t){
            return new JsonResult("ERROR","退出失败");
        }
        return new JsonResult("退出成功");
    }

}
