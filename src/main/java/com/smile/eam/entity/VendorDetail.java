package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorDetail {
    private int id;
    private String name;
    private String description;
    private String location;
    private Date createdAt;
    private Date updatedAt;
}
