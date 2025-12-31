package com.lz.manage.model.dto.studyRoom;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.lz.manage.model.domain.StudyRoom;
/**
 * 自习室信息Vo对象 tb_study_room
 *
 * @author YY
 * @date 2025-12-31
 */
@Data
public class StudyRoomEdit implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 名称 */
    private String name;

    /** 状态 */
    private String status;

    /** 教室图片 */
    private String image;

    /** 描述 */
    private String description;

    /** 备注 */
    private String remark;

    /**
     * 对象转封装类
     *
     * @param studyRoomEdit 编辑对象
     * @return StudyRoom
     */
    public static StudyRoom editToObj(StudyRoomEdit studyRoomEdit) {
        if (studyRoomEdit == null) {
            return null;
        }
        StudyRoom studyRoom = new StudyRoom();
        BeanUtils.copyProperties(studyRoomEdit, studyRoom);
        return studyRoom;
    }
}
