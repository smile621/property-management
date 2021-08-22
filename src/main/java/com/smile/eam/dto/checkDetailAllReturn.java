package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class checkDetailAllReturn {

    private int checkId;
    private String category;
    private String duty;
    private String status;
    private int totalCount;
    private int winCount;
    private int lossCount;
    private int waitCount;
    private String createdAt;
    private String predictAt;
    private String endAt;
    private BigDecimal lossPrice;
    private BigDecimal findPrice;
    private List<CheckDetailReturnDto> checkDetailReturn;
}
