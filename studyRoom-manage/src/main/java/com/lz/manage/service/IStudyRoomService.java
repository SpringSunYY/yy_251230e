package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.StudyRoom;
import com.lz.manage.model.vo.studyRoom.StudyRoomVo;
import com.lz.manage.model.dto.studyRoom.StudyRoomQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 自习室信息Service接口
 *
 * @author YY
 * @date 2025-12-31
 */
public interface IStudyRoomService extends IService<StudyRoom>
{
    //region mybatis代码
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
     * 批量删除自习室信息
     *
     * @param ids 需要删除的自习室信息主键集合
     * @return 结果
     */
    public int deleteStudyRoomByIds(Long[] ids);

    /**
     * 删除自习室信息信息
     *
     * @param id 自习室信息主键
     * @return 结果
     */
    public int deleteStudyRoomById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param studyRoomQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<StudyRoom> getQueryWrapper(StudyRoomQuery studyRoomQuery);

    /**
     * 转换vo
     *
     * @param studyRoomList StudyRoom集合
     * @return StudyRoomVO集合
     */
    List<StudyRoomVo> convertVoList(List<StudyRoom> studyRoomList);

    /**
     * 导入自习室信息数据
     *
     * @param studyRoomList 自习室信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importStudyRoomData(List<StudyRoom> studyRoomList, Boolean isUpdateSupport, String operName);
}
