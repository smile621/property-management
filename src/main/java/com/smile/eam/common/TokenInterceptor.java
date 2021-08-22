package com.smile.eam.common;

import com.smile.eam.entity.AdminUserToken;
import com.smile.eam.entity.AdminUser;
import com.smile.eam.mapper.TokenMapper;
import com.smile.eam.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    TokenMapper tokenMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        //从请求中拦截token
        String token = request.getParameter("token");
        System.out.println("token:"+token);
        AdminUserToken exist = tokenMapper.findOneByUserId(token);
        System.out.println("exist:"+exist);
        if (exist == null) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\": \"INVALID_TOKEN\",\n\"data\": \"null\",\n\"message\": \"无效TOKEN\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        //判断token是否过期
        if ((new Date().getTime() - exist.getCreatedAt().getTime()) > 7 * 24 * 60 * 60 * 1000) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\": \"INVALID_TOKEN\",\n\"data\": \"null\",\n\"message\": \"TOKEN过期\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        //通过exist对象的user_id去user表去查个人信息
        AdminUser user = userMapper.findUserById(exist.getUserId());
        System.out.println("user:"+user);
        if (user == null) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"code\": \"INVALID_TOKEN\",\n\"data\": \"null\",\n\"message\": \"无此人信息\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        //将用户的个人信息保存在上下文user对象里
        UserContext.setUser(user);
        return true;
    }
}
