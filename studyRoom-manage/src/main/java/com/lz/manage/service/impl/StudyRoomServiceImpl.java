package com.lz.manage.service.impl;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lz.common.utils.StringUtils;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lz.common.utils.DateUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lz.manage.mapper.StudyRoomMapper;
import com.lz.manage.model.domain.StudyRoom;
import com.lz.manage.service.IStudyRoomService;
import com.lz.manage.model.dto.studyRoom.StudyRoomQuery;
import com.lz.manage.model.vo.studyRoom.StudyRoomVo;

/**
 * 自习室信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-31
 */
@Service
public class StudyRoomServiceImpl extends ServiceImpl<StudyRoomMapper, StudyRoom> implements IStudyRoomService
{
    private static final Logger log = LoggerFactory.getLogger(StudyRoomServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private StudyRoomMapper studyRoomMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码
    /**
     * 查询自习室信息
     *
     * @param id 自习室信息主键
     * @return 自习室信息
     */
    @Override
    public StudyRoom selectStudyRoomById(Long id)
    {
        return studyRoomMapper.selectStudyRoomById(id);
    }

    /**
     * 查询自习室信息列表
     *
     * @param studyRoom 自习室信息
     * @return 自习室信息
     */
    @Override
    public List<StudyRoom> selectStudyRoomList(StudyRoom studyRoom)
    {
        return studyRoomMapper.selectStudyRoomList(studyRoom);
    }

    /**
     * 新增自习室信息
     *
     * @param studyRoom 自习室信息
     * @return 结果
     */
    @Override
    public int insertStudyRoom(StudyRoom studyRoom)
    {
        studyRoom.setCreateTime(DateUtils.getNowDate());
        return studyRoomMapper.insertStudyRoom(studyRoom);
    }

    /**
     * 修改自习室信息
     *
     * @param studyRoom 自习室信息
     * @return 结果
     */
    @Override
    public int updateStudyRoom(StudyRoom studyRoom)
    {
        studyRoom.setUpdateTime(DateUtils.getNowDate());
        return studyRoomMapper.updateStudyRoom(studyRoom);
    }

    /**
     * 批量删除自习室信息
     *
     * @param ids 需要删除的自习室信息主键
     * @return 结果
     */
    @Override
    public int deleteStudyRoomByIds(Long[] ids)
    {
        return studyRoomMapper.deleteStudyRoomByIds(ids);
    }

    /**
     * 删除自习室信息信息
     *
     * @param id 自习室信息主键
     * @return 结果
     */
    @Override
    public int deleteStudyRoomById(Long id)
    {
        return studyRoomMapper.deleteStudyRoomById(id);
    }
    //endregion
    @Override
    public QueryWrapper<StudyRoom> getQueryWrapper(StudyRoomQuery studyRoomQuery){
        QueryWrapper<StudyRoom> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = studyRoomQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = studyRoomQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        String name = studyRoomQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name) ,"name",name);

        String status = studyRoomQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status) ,"status",status);

        Date createTime = studyRoomQuery.getCreateTime();
        queryWrapper.eq( StringUtils.isNotNull(createTime),"create_time",createTime);

        return queryWrapper;
    }

    @Override
    public List<StudyRoomVo> convertVoList(List<StudyRoom> studyRoomList) {
        if (StringUtils.isEmpty(studyRoomList)) {
            return Collections.emptyList();
        }
        return studyRoomList.stream().map(StudyRoomVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入自习室信息数据
     *
     * @param studyRoomList 自习室信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importStudyRoomData(List<StudyRoom> studyRoomList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(studyRoomList) || studyRoomList.size() == 0)
        {
            throw new ServiceException("导入自习室信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (StudyRoom studyRoom : studyRoomList)
        {
            try
            {
                // 验证是否存在这个自习室信息
                Long id = studyRoom.getId();
                StudyRoom studyRoomExist = null;
                if (StringUtils.isNotNull(id))
                {
                    studyRoomExist = studyRoomMapper.selectStudyRoomById(id);
                }
                if (StringUtils.isNull(studyRoomExist))
                {
                    BeanValidators.validateWithException(validator, studyRoom);
                    studyRoom.setCreateTime(DateUtils.getNowDate());
                    studyRoomMapper.insertStudyRoom(studyRoom);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、自习室信息 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, studyRoom);
                    studyRoom.setUpdateTime(DateUtils.getNowDate());
                    studyRoomMapper.updateStudyRoom(studyRoom);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、自习室信息 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、自习室信息 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = studyRoom.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、自习室信息 " + idStr + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}
