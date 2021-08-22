package com.smile.eam.common;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CommonExceptionHandler {

    /**
     * 处理共同类异常
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Map<String, String> commonExceptionHandle(Throwable e) {
        e.printStackTrace();
        Map<String, String> res = new HashMap<>();
        res.put("code", "ERROR");
        res.put("message", e.getMessage());
        res.put("data", null);
        return res;
    }

    /**
     * 处理绑定类异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Map<String, String> bindExceptionHandle(BindException e) {

        e.printStackTrace();

        Map<String, String> res = new HashMap<>();
        res.put("code", "ERROR");
        res.put("message", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        res.put("data", null);

        return res;
    }

}
