package com.lz.manage.model.dto.studyRoom;

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
import com.lz.manage.model.domain.StudyRoom;
/**
 * 自习室信息Query对象 tb_study_room
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class StudyRoomQuery implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 名称 */
    private String name;

    /** 状态 */
    private String status;

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
     * @param studyRoomQuery 查询对象
     * @return StudyRoom
     */
    public static StudyRoom queryToObj(StudyRoomQuery studyRoomQuery) {
        if (studyRoomQuery == null) {
            return null;
        }
        StudyRoom studyRoom = new StudyRoom();
        BeanUtils.copyProperties(studyRoomQuery, studyRoom);
        return studyRoom;
    }
}
