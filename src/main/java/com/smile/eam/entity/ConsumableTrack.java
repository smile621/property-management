package com.smile.eam.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableTrack {

    @ApiModelProperty(example = "1",value = "ID")
    private int id;
    @ApiModelProperty(example = "11",value = "领用人ID")
    private int recipientId;                //领用人id
    @ApiModelProperty(example = "1",value = "耗材ID")
    private int consumableId;               //耗材id
    private String intDescription;          //入库描述
    private String outDescription;          //出库描述
    @ApiModelProperty(example = "1",value = "入库人ID")
    private int inWareHouseUserId;          //入库人id
    @ApiModelProperty(example = "1",value = "出库人ID")
    private int outWareHouseUserId;         //出库人id
    @ApiModelProperty(example = "1",value = "出库数")
    private int outNumber;                  //出库数
    @ApiModelProperty(example = "1",value = "入库数")
    private int inNumber;                   //入库数
    private String buyAt;                   //购入时间
    private String outWareHouseAt;          //出库时间
    private String updatedAt;               //更新时间
    private String createdAt;               //创建时间
    private String inWareHouseAt;           //入库时间
    @ApiModelProperty(example = "1",value = "状态")
    private int status;                     //状态

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
