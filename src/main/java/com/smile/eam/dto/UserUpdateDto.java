package com.smile.eam.dto;

import com.smile.eam.common.MD5Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDto {

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$", message = "用户名以字母开头,可包含数字或下划线,长度4到20")
    private String username;

    @NotNull(message = "昵称不能为空")
    private String nickname;

    @NotNull(message = "性别不能为空")
    private String sex;

    @NotNull(message = "部门不能为空")
    private Integer departmentId;

    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^$|^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\s\\S]{8,16}$", message = "密码必须包含大小写和至少一个数字,8-16个字符")
    private String password;

    private String passwordHash;

    @NotNull(message = "角色不能为空")
    private List<Integer> roleList;

    @NotNull(message = "职位不能为空")
    private String job;

    @NotNull(message = "手机不能为空")
    @Pattern(regexp = "^$|^1[3-9][0-9]{9}$", message = "请输入正确的手机号")
    private String phone;

    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^$|^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "请输入正确格式的邮箱")
    private String email;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    public void setPasswordHash(String password,String passwordHash) {
        if ("".equals(password) || password == null) {
            this.passwordHash = passwordHash;
        } else {
            MD5Util md = new MD5Util();
            this.passwordHash = md.getMD5String(password);
        }
    }

}
