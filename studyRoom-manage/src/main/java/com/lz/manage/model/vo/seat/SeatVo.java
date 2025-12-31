package com.lz.manage.model.vo.seat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.Seat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 座位信息Vo对象 tb_seat
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class SeatVo implements Serializable {
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
     * 名称
     */
    private String name;

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
     * @param seat Seat实体对象
     * @return SeatVo
     */
    public static SeatVo objToVo(Seat seat) {
        if (seat == null) {
            return null;
        }
        SeatVo seatVo = new SeatVo();
        BeanUtils.copyProperties(seat, seatVo);
        return seatVo;
    }
}
