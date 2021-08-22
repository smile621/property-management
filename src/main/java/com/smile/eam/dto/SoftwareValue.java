package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NotNull
public class SoftwareValue {
    private BigDecimal price;
    private String createdAt;
    private static int ZERO=0;
}
