package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.StudyRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 自习室信息Mapper接口
 * 
 * @author YY
 * @date 2025-12-31
 */
public interface StudyRoomMapper extends BaseMapper<StudyRoom>
{
    /**
     * 查询自习室信息
     * 
     * @param id 自习室信息主键
     * @return 自习室信息
     */
    public StudyRoom selectStudyRoomById(Long id);

    /**
     * 查询自习室信息列表
     * 
     * @param studyRoom 自习室信息
     * @return 自习室信息集合
     */
    public List<StudyRoom> selectStudyRoomList(StudyRoom studyRoom);

    /**
     * 新增自习室信息
     * 
     * @param studyRoom 自习室信息
     * @return 结果
     */
    public int insertStudyRoom(StudyRoom studyRoom);

    /**
     * 修改自习室信息
     * 
     * @param studyRoom 自习室信息
     * @return 结果
     */
    public int updateStudyRoom(StudyRoom studyRoom);

    /**
     * 删除自习室信息
     * 
     * @param id 自习室信息主键
     * @return 结果
     */
    public int deleteStudyRoomById(Long id);

    /**
     * 批量删除自习室信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStudyRoomByIds(Long[] ids);
}
