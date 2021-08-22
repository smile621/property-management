package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceHomeReturn {

    private String deviceName;
    private String serviceName;
    private String status;
    private Date startAt;
    private Date recoverAt;

}
