package com.lz.manage.model.dto.notification;

import java.util.Map;
import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import com.lz.manage.model.domain.Notification;
/**
 * 通知Query对象 tb_notification
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class NotificationQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 通知编号 */
    private Long id;

    /** 用户编号 */
    private Long userId;

    /** 通知标题 */
    private String titile;

    /** 是否已读 */
    private String readFlag;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    /**
     * 对象转封装类
     *
     * @param notificationQuery 查询对象
     * @return Notification
     */
    public static Notification queryToObj(NotificationQuery notificationQuery) {
        if (notificationQuery == null) {
            return null;
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationQuery, notification);
        return notification;
    }
}
