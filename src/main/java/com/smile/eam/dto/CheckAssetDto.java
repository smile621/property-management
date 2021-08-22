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
public class CheckAssetDto {
    private int id;
    private String name;
    private String status;
    private String affiliationUser;
    private String affiliationDevice;
    private Date checkAt;
    private Date createAt;
}
