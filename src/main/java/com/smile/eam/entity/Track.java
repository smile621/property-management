package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {

        private int fixedAssetId;
        private int userId;
        private int deviceId;
        private int lendUserId;
        private Date bindingStartAt;
        private Date bindingEndAt;
        private String lendDescription;
        private Date lendStartAt;
        private Date lendPlanReturnAt;
        private Date lendReturnAt;
        private String lendEndDescription;
        private Date createdAt;
        private Date updatedAt;
        private int status;
}
