package com.smile.eam.dto;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceListDto extends BaseRowModel {

    @ExcelProperty(value = {"资产编号"}, index = 0)
    @ApiModelProperty(value = "资产编号",example = "1234",reference = "true")
    private int id;
    @ApiModelProperty(value = "设备名称",example = "数控",reference = "true")
    @NotNull(message = "设备名不能为空")
    @ExcelProperty(value = {"设备名称"}, index = 1)
    private String name;
    @ExcelProperty(value = {"资产分类编号"}, index = 2)
    @ApiModelProperty(value = "资产分类id",example = "12",reference = "ture")
    private int assetCategoryId;
    @ApiModelProperty(value = "资产分类名称",example = "设备1",reference = "true")
    @NotNull(message = "资产不能为空")
    @ExcelProperty(value = {"资产分类名称"}, index = 3)
    private String assetCategoryName;
    @ApiModelProperty(value = "厂商id",example = "12",reference = "ture")
    @ExcelProperty(value = {"厂商编号"}, index = 4)
    private int vendorId;
    @ApiModelProperty(value = "厂商名称",example = "小米",reference = "true")
    @ExcelProperty(value = {"产商名称"}, index = 5)
    @NotNull(message = "厂商名不能为空")
    private String vendorName;
    @ApiModelProperty(value = "购入途径id",example = "12",reference = "ture")
    @ExcelProperty(value = {"购入途径编号"}, index = 6)
    private int buyRouterId;
    @ApiModelProperty(value = "购入途径名称",example = "线下",reference = "false")
    @ExcelProperty(value = {"购入途径名称"}, index = 7)
    private String buyRouterName;
    @ApiModelProperty(value = "折旧模板id",example = "12",reference = "ture")
    @ExcelProperty(value = {"折旧模板编号"}, index = 8)
    private int depreciateId;
    @ApiModelProperty(value = "折旧名称",example = "最省钱",reference = "false")
    @ExcelProperty(value = {"折旧模板名称"}, index = 9)
    private String depreciateName;
    @ApiModelProperty(value = "归属人id",example = "12",reference = "ture")
    @ExcelProperty(value = {"归属人编号"}, index = 10)
    private int userId;
    @ApiModelProperty(value = "领用人名称",example = "小王",reference = "false")
    @ExcelProperty(value = {"归属人名称"}, index = 11)
    private String userName;
    @ApiModelProperty(value = "借用人id",example = "12",reference = "ture")
    @ExcelProperty(value = {"借用人编号"}, index = 12)
    private int lendUserId;
    @ApiModelProperty(value = "借用人名",example = "小王",reference = "false")
    @ExcelProperty(value = {"借用人名称"}, index = 13)
    private String lendUserName;
    @ApiModelProperty(value = "价格",example = "20",reference = "false")
    @ExcelProperty(value = {"价格"}, index = 14)
    private BigDecimal price;
    @ApiModelProperty(value = "描述",example = "啧啧啧",reference = "false")
    @ExcelProperty(value = {"描述"}, index = 15)
    private String description;
    @ApiModelProperty(value = "ip地址",example = "11.22.33.44",reference = "false")
    @ExcelProperty(value = {"IP"}, index = 16)
    private String ip;
    @ApiModelProperty(value = "物理地址",example = "x",reference = "false")
    @ExcelProperty(value = {"MAC"}, index = 17)
    private String mac;
    @ApiModelProperty(value = "购入日期",example = "2021-5-20",reference = "false")
    @ExcelProperty(value = {"购入日期"}, index = 18)
    private Date buyAt;
    @ApiModelProperty(value = "过保时间",example = "2021-5-20",reference = "false")
    @ExcelProperty(value = {"过保日期"}, index = 19)
    private Date expiredAt;
    @ApiModelProperty(value = "创建时间",example = "2021-5-20",reference = "false")
    @ExcelProperty(value = {"创建时间"}, index = 20)
    private Date createdAt;
    @ApiModelProperty(value = "设备状态",example = "0",reference = "false")
    private int status;
    @ApiModelProperty(value = "故障状态",example = "4",reference = "false")
    private int malfunctionStatus;
}
