package com.lz.manage.model.vo.studyRoom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.manage.model.domain.StudyRoom;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 自习室信息Vo对象 tb_study_room
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class StudyRoomVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 教室图片
     */
    private String image;

    /**
     * 描述
     */
    private String description;

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
     * @param studyRoom StudyRoom实体对象
     * @return StudyRoomVo
     */
    public static StudyRoomVo objToVo(StudyRoom studyRoom) {
        if (studyRoom == null) {
            return null;
        }
        StudyRoomVo studyRoomVo = new StudyRoomVo();
        BeanUtils.copyProperties(studyRoom, studyRoomVo);
        return studyRoomVo;
    }
}
