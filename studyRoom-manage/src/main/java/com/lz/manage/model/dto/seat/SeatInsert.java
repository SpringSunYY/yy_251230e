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
public class SeatInsert implements Serializable
{
    private static final long serialVersionUID = 1L;

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
     * @param seatInsert 插入对象
     * @return SeatInsert
     */
    public static Seat insertToObj(SeatInsert seatInsert) {
        if (seatInsert == null) {
            return null;
        }
        Seat seat = new Seat();
        BeanUtils.copyProperties(seatInsert, seat);
        return seat;
    }
}
