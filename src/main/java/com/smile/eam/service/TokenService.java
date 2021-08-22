package com.smile.eam.service;

import com.smile.eam.mapper.TokenMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TokenService {
    @Resource
    TokenMapper tokenMapper;
   /**
    * 删除token
    * */
   public Boolean deleteToken(String token){
       int t = tokenMapper.deleteToken(token);
       if(t<1){
           return false;
       }
       return true;
   }

}
