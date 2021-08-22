package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceListDto {

    private Integer id;
    private String name;
    private String description;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    private String price;
    private String vendor;
    private Integer vendorId;
    private Date startAt;
    private Date endAt;
    private Integer buyRouterId;    //购买方式ID
    private String buyRouter;       //购买方式
    private List<String> deviceNames;      //归属设备名
}
