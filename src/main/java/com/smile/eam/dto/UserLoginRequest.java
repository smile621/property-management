package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotNull(message = "用户名不为空")
    @Pattern(regexp = "^\\w{4,18}+$",message = "请输入由4-18位字母、数字、下划线组成的用户名[username]")
    private String username;

    @NotNull(message = "密码不为空")
    @Pattern(regexp = "^\\w{6,18}+$",message = "请输入由6-18位字母、数字、下划线组成的密码[passwordHash]")
    private String passwordHash;
}
