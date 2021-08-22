package com.smile.eam.entity;

public class Asset {

    /**
    状态默认值
     */
    public static final int STATUS_NORMAL = 0;      //正常
    public static final int STATUS_LEISURE = 1;     //闲置
    public static final int STATUS_AFFILIATION = 2;     //归属
    public static final int STATUS_BORROW = 3;      //借用
    public static final int STATUS_MALFUNCTION = 4;     //故障
    public static final int STATUS_PENDING = 5;     //待处理
    public static final int STATUS_BEING_PROCESSED = 6;     //处理中
    public static final int STATUS_FIGURE_OUT = 7;      //处理完成
    public static final int STATUS_NONE_CHECK = 8;      //未盘点
    public static final int STATUS_ACCOMPLISH_CHECK = 9;        //以盘点
    public static final int STATUS_LOSE = 10;       //丢失
    public static final int STATUS_DISCONTINUE = 11;         //中止
    public static final int STATUS_INT = 12;        //耗材入库
    public static final int STATUS_OUT = 13;        //耗材出库
    public static final int STATUS_DELETE = 100;        //删除状态

    /**
    分类默认值
     */
    public static final int CATEGORY_ID_DEVICE = 1;     //设备分类id
    public static final int CATEGORY_ID_PART = 2;       //配件分类id
    public static final int CATEGORY_ID_SOFTWARE = 3;       //软件分类id
    public static final int CATEGORY_ID_SERVICE = 4;        //服务分类id
    public static final int CATEGORY_ID_CONSUMABLE = 5;         //耗材分类id
}
