package com.smile.eam.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int id;
    private String name;
    private int parentId;
    private List<Category> list=new ArrayList<>();
    private int status;

    public static final int STATUS_NORMAL = 0;              //正常
    public static final int STATUS_DELETE = 100;            //删除状态

    public static final int CATEGORY_ID_DEVICE = 1;         //设备默认初始值
    public static final int CATEGORY_ID_PART = 2;           //配件分类id
    public static final int CATEGORY_ID_SOFTWARE = 3;       //软件分类id
    public static final int CATEGORY_ID_SERVICE = 4;        //服务分类id
    public static final int CATEGORY_ID_CONSUMABLE = 5;     //耗材分类id

}
