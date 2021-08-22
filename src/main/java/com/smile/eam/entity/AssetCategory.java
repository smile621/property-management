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
public class AssetCategory {

    //软件分类
    private int id;
    private int parentId;//软件父级id
    private String name;//软件名称
    private String description;//软件描述
    private Date createdAt;
    private Date updatedAt;
    private int status;
    private int flag;

    /**
     * 状态默认值
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

    public String getStatusAlias() {
        switch (status) {
            case STATUS_NORMAL:
                return "正常";
            case STATUS_LEISURE:
                return "闲置";
            case STATUS_AFFILIATION:
                return "归属";
            case STATUS_BORROW :
                return "借用";
            case STATUS_MALFUNCTION:
                return "故障";
            case STATUS_PENDING:
                return "待处理";
            case STATUS_BEING_PROCESSED:
                return "处理中";
            case STATUS_FIGURE_OUT:
                return "处理完成";
            case STATUS_NONE_CHECK:
                return "未盘点";
            case STATUS_ACCOMPLISH_CHECK:
                return "已盘点";
            case STATUS_LOSE:
                return "丢失";
            case STATUS_DISCONTINUE:
                return "中止";
            case STATUS_INT:
                return "耗材入库";
            case STATUS_OUT:
                return "耗材出库";
            case STATUS_DELETE:
                return "删除";
            default:
                return "未知[" + status + "]";
        }
    }

    public String getCategoryStatusAlias(){
        switch(flag){
            case CATEGORY_ID_DEVICE:
                return "设备";
            case CATEGORY_ID_PART:
                return "配件";
            case CATEGORY_ID_SOFTWARE:
                return "软件";
            case CATEGORY_ID_SERVICE:
                return "服务";
            case CATEGORY_ID_CONSUMABLE:
                return "耗材";
            default:
                return "未知[" + status + "]";
        }
    }
}
