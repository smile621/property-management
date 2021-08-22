package com.smile.eam.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    private int id;                         //资产编号
    private String name;                    //资产名称
    private String avatar;                  //头像
    private int assetCategoryId;            //资产分类id
    private int userId;                     //归属人
    private int lendUserId;                 //借用人
    private int vendorId;                   //厂商id
    private BigDecimal price;               //价格
    private int buyRouterId;                //购入途径ID
    private String description;             //描述
    private String ip;                      //ip地址
    private String mac;                     //mac地址
    private String specification;           //规格
    private int depreciateId;               //折旧模板id
    private String version;                 //版本
    private int issueId;                    //发行方式ID
    private int warrantyNumber;             //授权数量
    private Date startAt;                   //开始使用日期
    private Date endAt;                     //结束使用日期
    private Date buyAt;                     //购入日期
    private Date expiredAt;                 //过保日期
    private Date createdAt;                 //创建日期
    private Date updatedAt;                 //更新日期
    private int status;                     //状态
    private int malfunctionStatus;          //故障状态

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
}
