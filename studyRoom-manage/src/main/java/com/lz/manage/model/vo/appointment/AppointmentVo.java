package com.lz.manage.model.vo.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.Appointment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约信息Vo对象 tb_appointment
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class AppointmentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 教室
     */
    private Long roomId;
    private String roomName;

    /**
     * 座位
     */
    private Long seatId;
    private String seatName;

    /**
     * 预约时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date appointmentTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private Long userId;
    private String userName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;


    /**
     * 对象转封装类
     *
     * @param appointment Appointment实体对象
     * @return AppointmentVo
     */
    public static AppointmentVo objToVo(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        AppointmentVo appointmentVo = new AppointmentVo();
        BeanUtils.copyProperties(appointment, appointmentVo);
        return appointmentVo;
    }
}
