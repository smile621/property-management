package com.smile.eam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ServiceDetailDto {

    private Integer id;
    private Date expiredAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Integer status;
    private String name;
    private String description;
    private String price;
    private Date buyAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Integer buyRouterId;
    private String buyRouter;
    private Integer vendorId;
    private String vendor;
    private List<String> deviceNames;      //归属设备名

}
