package com.smile.eam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.smile.eam.common.Pagination;
import com.smile.eam.entity.VendorDetail;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorSearchResponseDto {

    List<VendorDetail> vendorList;
    Pagination pagination;

}
