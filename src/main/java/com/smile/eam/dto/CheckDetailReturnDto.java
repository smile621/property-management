package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckDetailReturnDto {

    private int id;
    private int checkId;
    private int assetId;
    private String assetName;
    private String checkUserName;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private String description;
    private String CheckUserNickname;
    private BigDecimal price;
}
