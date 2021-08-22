package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepreciationDetailItem {

    private int id;
    private String templateName;
    private int depreciationId;
    private int period;
    private int measure;
    private String rate;
    private String createdAt;
    private String updatedAt;
    private int status;

}
