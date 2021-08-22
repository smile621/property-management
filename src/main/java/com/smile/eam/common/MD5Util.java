package com.smile.eam.common;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;

@Data
@Service
public class MD5Util {

    public String getMD5String(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
