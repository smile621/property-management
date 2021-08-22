package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoftwareTrackResponse {
    private String fixedAssetId;//软件id
    private String deviceId;//设备id
    private String lendDescription;//绑定记录
    private String createdAt;//开始时间
}
