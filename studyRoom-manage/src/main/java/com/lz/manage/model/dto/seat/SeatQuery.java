package com.lz.manage.model.dto.seat;

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
import com.lz.manage.model.domain.Seat;
/**
 * 座位信息Query对象 tb_seat
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class SeatQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 名称 */
    private String name;

    /** 状态 */
    private String status;

    /** 教室 */
    private Long roomId;

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
     * @param seatQuery 查询对象
     * @return Seat
     */
    public static Seat queryToObj(SeatQuery seatQuery) {
        if (seatQuery == null) {
            return null;
        }
        Seat seat = new Seat();
        BeanUtils.copyProperties(seatQuery, seat);
        return seat;
    }
}
