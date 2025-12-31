package com.lz.manage.model.dto.seat;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.Seat;
/**
 * 座位信息Vo对象 tb_seat
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class SeatEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 教室 */
    private Long roomId;

    /** 名称 */
    private String name;

    /** 状态 */
    private String status;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param seatEdit 编辑对象
     * @return Seat
     */
    public static Seat editToObj(SeatEdit seatEdit) {
        if (seatEdit == null) {
            return null;
        }
        Seat seat = new Seat();
        BeanUtils.copyProperties(seatEdit, seat);
        return seat;
    }
}
