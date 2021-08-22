package com.smile.eam.dto;

import com.smile.eam.common.MD5Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NowUserUpdateDto {

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "姓名不能为空")
    private String nickname;

    @NotNull(message = "旧密码不能为空")
    private String oldPassword;

    @NotNull(message = "新密码不能为空")
    private String newPassword;

    public void setNewPassword(String password,String passwordHash) {
        if ("".equals(oldPassword) || oldPassword == null) {
            this.newPassword = passwordHash;
        } else {
            MD5Util md = new MD5Util();
            this.newPassword = md.getMD5String(password);
        }
    }

    public void setOldPassword(String password,String passwordHash) {
        if ("".equals(oldPassword) || oldPassword == null) {
            this.oldPassword = passwordHash;
        } else {
            MD5Util md = new MD5Util();
            this.oldPassword= md.getMD5String(password);
        }
    }

}
