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
public class CheckCreateDto {

    private int categoryId;
    private int userId;
    private String predictAt;
}
