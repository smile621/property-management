package com.smile.eam.controller;

import com.smile.eam.common.JsonResult;
import com.smile.eam.entity.HomeReturn;
import com.smile.eam.service.HomeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

@RestController
@CrossOrigin
public class HomeController {

    @Resource
    HomeService homeService;

    /**
     * 获取主页
     */
    @GetMapping("/api/getHome")
    public JsonResult<HomeReturn> getHome() throws ParseException {
        JsonResult<HomeReturn> jsonResult = new JsonResult<>();
        HomeReturn homeReturn = homeService.getHome();
        jsonResult.setData(homeReturn);
        return jsonResult ;
    }

}
