package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
@Data
public class SoftwarePropertyCreateRequest{
    @NotNull(message ="请输入软件名称[name]")
    private String name;//软件名称
    private String avatar;//头像
    @ApiModelProperty("软件所属分类")
    @NotNull(message = "请选择软件所属分类[assetCategoryId]")
    private  Integer assetCategoryId;//软件所属分类
//    @NotNull(message = "请输入软件价格[price]")
    @Pattern(regexp = "^\\d+(\\.\\d+)?" ,message ="请输入正确的价格[price]" )
    private String price;//软件价格
    @ApiModelProperty("购买途径")
    @NotNull(message = "请输入购买途径[buyRouterId]")
    private Integer buyRouterId;//购买途径

    private String description;//资产描述
    @ApiModelProperty("厂商id")
    @NotNull(message = "请传入厂商id[vendorId]")
    private Integer vendorId;//厂商id
//    private String softwareStatus;//软件状态0闲置  1归属
    @ApiModelProperty("软件版本")
    @NotNull(message = "请输入软件版本[version]")
    private String version;//软件版本
    @ApiModelProperty("发行版本方式")
    @NotNull(message = "请选择发行版本方式[issueId]")
    private Integer issueId;//发行版本方式id
    @ApiModelProperty("购买数量,不要填")
    private  static Integer warrantyNumber=1;//授权数量
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startAt;//开始使用时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endAt;//使用结束时间
//    @NotNull(message = "请选择购买时间[buyAt]")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyAt;
    @NotNull(message = "请选择到期时间[expiredAt]")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiredAt;
    private Long expiredDate;//过保时间
}
