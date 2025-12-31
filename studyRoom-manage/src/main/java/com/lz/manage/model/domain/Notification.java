package com.lz.manage.model.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.lz.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * 通知对象 tb_notification
 *
 * @author YY
 * @date 2025-12-31
 */
@TableName("tb_notification")
@Data
public class Notification implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 通知编号 */
    @Excel(name = "通知编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userId;

    /** 通知标题 */
    @Excel(name = "通知标题")
    private String titile;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String content;

    /** 是否已读 */
    @Excel(name = "是否已读", dictType = "sys_yes_no")
    private String readFlag;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createTime;

    /** 读取时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "读取时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date readTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}
