package com.smile.eam.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminPermission {

    private int id;
    private String name;
    private String tag;
    private String code;
    private String createdAt;
    private String updatedAt;
    private int deleteId;

    public static final int STATUS_NORMAL = 0;      //正常
    public static final int STATUS_DELETE = 100;        //删除状态


}
