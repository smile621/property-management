package com.smile.eam.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FixedAsset {
    private int id;//资产编号
    private String name;//资产名称
    private String avatar;//头像
    private int assetCategoryId;    //资产分类id
    private String assetCategory;//资产分类名称
    private int userId;
    private int deviceId;
    private int lendUserId;//借用人
    private int vendorId;//厂商id
    private String vendor;//厂商
    private BigDecimal price;
    private int buyRouterId;//购买途径
    private String buyRouter;//购买途径
    private String description;
    private String ip;
    private String mac;
    private String specification;
    private String version;//版本
    private int issueId;//发行方式
    private String issue;
    private int warrantyNumber;
    private Date startAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buyAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiredAt;
    private Long expiredDate;//过保时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    private int status;
    private int flag;//分类状态值
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
