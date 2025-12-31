package com.lz.manage.model.dto.notification;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.Notification;
/**
 * 通知Vo对象 tb_notification
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class NotificationInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 通知编号 */
    private Long id;

    /** 用户编号 */
    private Long userId;

    /** 通知标题 */
    private String titile;

    /** 通知内容 */
    private String content;

    /** 是否已读 */
    private String readFlag;

    /**
     * 对象转封装类
     *
     * @param notificationInsert 插入对象
     * @return NotificationInsert
     */
    public static Notification insertToObj(NotificationInsert notificationInsert) {
        if (notificationInsert == null) {
            return null;
        }
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationInsert, notification);
        return notification;
    }
}
