package com.smile.eam.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Part {

    private int id;
    private int assetCategoryId;
    private int buyRouterId;
    private int vendorId;
    private String name;
    private String description;
    private String avatar;
    private String price;
    private String outPrice;
    private int depreciateId;
    private int status;
    private int malfunctionStatus;
    private int deviceId;
    private Date buyAt;
    private Date expiredAt;
    private Date startAt;
    private Date endAt;
    private Date createdAt;
    private Date updatedAt;

    public static final int STATUS_NORMAL = 0;      //正常
    public static final int STATUS_LEISURE = 1;     //闲置
    public static final int STATUS_AFFILIATION = 2;     //归属
    public static final int STATUS_MALFUNCTION = 4;     //故障
    public static final int STATUS_DELETE = 100;        //删除状态
    public static final int CATEGORY_ID_PART = 2;       //配件分类id
}
