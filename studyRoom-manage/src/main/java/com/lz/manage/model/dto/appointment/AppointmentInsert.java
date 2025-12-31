package com.lz.manage.model.dto.appointment;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.Appointment;
/**
 * 预约信息Vo对象 tb_appointment
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class AppointmentInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 教室 */
    private Long roomId;

    /** 座位 */
    private Long seatId;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date appointmentTime;

    /** 状态 */
    private String status;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param appointmentInsert 插入对象
     * @return AppointmentInsert
     */
    public static Appointment insertToObj(AppointmentInsert appointmentInsert) {
        if (appointmentInsert == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentInsert, appointment);
        return appointment;
    }
}
