package com.smile.eam.dto;

import com.smile.eam.common.MD5Util;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$", message = "用户名以字母开头,可包含数字或下划线,长度4到20")
    private String username;

    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\s\\S]{8,16}$", message = "密码必须包含大小写和至少一个数字,8-16个字符")
    private String password;

    @ApiModelProperty("密码MD5值，不用传")
    private String passwordHash;

    @ApiModelProperty("昵称")
    @NotNull(message = "昵称不能为空")
    private String nickname;

    @ApiModelProperty("性别")
    @NotNull(message = "性别不能为空")
    private String sex;

    @ApiModelProperty("头像")
    @NotNull(message = "头像不能为空")
    private String avatar;

    @ApiModelProperty("工作")
    @NotNull(message = "职位不能为空[job]")
    private String job;

    @ApiModelProperty("手机")
    @Pattern(regexp = "^$|^1[3-9][0-9]{9}$" , message = "请输入正确的手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    @Pattern(regexp ="^$|^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$" , message = "请输入正确格式的邮箱")
    private String email;

    @ApiModelProperty("部门Id")
    @NotNull(message = "部门不能为空")
    private Integer departmentId;

    @ApiModelProperty("角色Id列表")
    @NotNull(message = "角色不能为空")
    private List<Integer> roleList;

    public void setPasswordHash(String password) {
        MD5Util md = new MD5Util();
        this.passwordHash = md.getMD5String(password);
    }

}
