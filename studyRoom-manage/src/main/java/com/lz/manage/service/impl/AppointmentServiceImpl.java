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
import com.lz.manage.mapper.AppointmentMapper;
import com.lz.manage.model.domain.Appointment;
import com.lz.manage.service.IAppointmentService;
import com.lz.manage.model.dto.appointment.AppointmentQuery;
import com.lz.manage.model.vo.appointment.AppointmentVo;

/**
 * 预约信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-31
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService
{
    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private AppointmentMapper appointmentMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码
    /**
     * 查询预约信息
     *
     * @param id 预约信息主键
     * @return 预约信息
     */
    @Override
    public Appointment selectAppointmentById(Long id)
    {
        return appointmentMapper.selectAppointmentById(id);
    }

    /**
     * 查询预约信息列表
     *
     * @param appointment 预约信息
     * @return 预约信息
     */
    @Override
    public List<Appointment> selectAppointmentList(Appointment appointment)
    {
        return appointmentMapper.selectAppointmentList(appointment);
    }

    /**
     * 新增预约信息
     *
     * @param appointment 预约信息
     * @return 结果
     */
    @Override
    public int insertAppointment(Appointment appointment)
    {
        appointment.setCreateTime(DateUtils.getNowDate());
        return appointmentMapper.insertAppointment(appointment);
    }

    /**
     * 修改预约信息
     *
     * @param appointment 预约信息
     * @return 结果
     */
    @Override
    public int updateAppointment(Appointment appointment)
    {
        appointment.setUpdateTime(DateUtils.getNowDate());
        return appointmentMapper.updateAppointment(appointment);
    }

    /**
     * 批量删除预约信息
     *
     * @param ids 需要删除的预约信息主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentByIds(Long[] ids)
    {
        return appointmentMapper.deleteAppointmentByIds(ids);
    }

    /**
     * 删除预约信息信息
     *
     * @param id 预约信息主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentById(Long id)
    {
        return appointmentMapper.deleteAppointmentById(id);
    }
    //endregion
    @Override
    public QueryWrapper<Appointment> getQueryWrapper(AppointmentQuery appointmentQuery){
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = appointmentQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = appointmentQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        Date appointmentTime = appointmentQuery.getAppointmentTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginAppointmentTime"))&&StringUtils.isNotNull(params.get("endAppointmentTime")),"appointment_time",params.get("beginAppointmentTime"),params.get("endAppointmentTime"));

        String status = appointmentQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status) ,"status",status);

        Long userId = appointmentQuery.getUserId();
        queryWrapper.eq( StringUtils.isNotNull(userId),"user_id",userId);

        Date createTime = appointmentQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<AppointmentVo> convertVoList(List<Appointment> appointmentList) {
        if (StringUtils.isEmpty(appointmentList)) {
            return Collections.emptyList();
        }
        return appointmentList.stream().map(AppointmentVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入预约信息数据
     *
     * @param appointmentList 预约信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importAppointmentData(List<Appointment> appointmentList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(appointmentList) || appointmentList.size() == 0)
        {
            throw new ServiceException("导入预约信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Appointment appointment : appointmentList)
        {
            try
            {
                // 验证是否存在这个预约信息
                Long id = appointment.getId();
                Appointment appointmentExist = null;
                if (StringUtils.isNotNull(id))
                {
                    appointmentExist = appointmentMapper.selectAppointmentById(id);
                }
                if (StringUtils.isNull(appointmentExist))
                {
                    BeanValidators.validateWithException(validator, appointment);
                    appointment.setCreateTime(DateUtils.getNowDate());
                    appointmentMapper.insertAppointment(appointment);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、预约信息 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, appointment);
                    appointment.setUpdateTime(DateUtils.getNowDate());
                    appointmentMapper.updateAppointment(appointment);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、预约信息 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、预约信息 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = appointment.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、预约信息 " + idStr + " 导入失败：";
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
