package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Api(tags = "文件上传相关接口")
@RestController
@CrossOrigin
public class UploadController {

    @Resource
    UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @ApiOperation("更改用户头像API")
    @PostMapping("/api/upload/avatar")
    public JsonResult image(@RequestParam("file") MultipartFile file , Integer userId) throws IOException {

        if(file.isEmpty()){
            return new JsonResult(false,"没有上传文件");
        }

        int index = file.getOriginalFilename().lastIndexOf(".");
        String extname = file.getOriginalFilename().substring(index+1).toLowerCase();

        String allowImgFormat = "png,jpg,jpeg,gif";
        if(!allowImgFormat.contains(extname)){
            return new JsonResult(false,"文件类型不被允许");
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/");
        String subPath = simpleDateFormat.format(new Date());
        String saveName =subPath + UUID.randomUUID().toString()+"."+extname;

        File dir = new File(uploadPath + subPath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        File save = new File(uploadPath + saveName);
        file.transferTo(save.getAbsoluteFile());
        int flag = userService.updateUserAvatar(userId,"/"+saveName);
        if(flag!=1)
            return new JsonResult(false,"头像更改失败，请重试");

        return new JsonResult();
    }
}

