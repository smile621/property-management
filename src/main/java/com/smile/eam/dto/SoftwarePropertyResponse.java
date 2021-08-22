package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class SoftwarePropertyResponse{
    private Integer id;
    private String name;//软件名称
    private String assetCategoryId;//软件所属分类
    private BigDecimal price;//软件价格
    private String buyRouterId;//购买途径
    private String description;//资产描述
    private String vendorId;//厂商id
    private String status;//软件状态0闲置  1归属
    private String version;//软件版本
    private String issueId;//发行版本方式id
    private Integer warrantyNumber;//授权数量
    private Date startAt;//开始使用时间
    private Date endAt;//使用结束时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyAt;//购买时间
    private Long expiredDate;//过保日期
    private String userId;//借用人，归属人
    private List<String> deviceId;//归属设备id
    private List<Integer> deviceIds;//设备id
}
