package com.smile.eam.common;

import com.smile.eam.entity.AdminUser;

public class UserContext {
    //创建线程安全的数据传输
    private static final ThreadLocal<AdminUser> current = new ThreadLocal<>();

    public static void setUser(AdminUser user){
        current.set(user);
    }
    public static AdminUser getUser(){
        return current.get();
    }
}
