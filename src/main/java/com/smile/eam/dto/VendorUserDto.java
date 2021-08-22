package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorUserDto {

    private String name;//联系人姓名
    private String job;//联系人职位
    private String phone;//联系人电话
    @Email(message = "邮箱格式不正确，请重输")
    private String email;//联系人邮箱

}
