package com.lz.manage.model.vo.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.Notification;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知Vo对象 tb_notification
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class NotificationVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 通知编号
     */
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;
    private String userName;

    /**
     * 通知标题
     */
    private String titile;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否已读
     */
    private String readFlag;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 读取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date readTime;


    /**
     * 对象转封装类
     *
     * @param notification Notification实体对象
     * @return NotificationVo
     */
    public static NotificationVo objToVo(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationVo notificationVo = new NotificationVo();
        BeanUtils.copyProperties(notification, notificationVo);
        return notificationVo;
    }
}
