package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareTrackRequest {
    private int fixedAssetId;//软件id
    private int deviceId;//设备id
    private String lendDescription;//绑定记录
    private String createdAt;//开始时间

}
