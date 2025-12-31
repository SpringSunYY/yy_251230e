package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.Appointment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 预约信息Mapper接口
 * 
 * @author YY
 * @date 2025-12-31
 */
public interface AppointmentMapper extends BaseMapper<Appointment>
{
    /**
     * 查询预约信息
     * 
     * @param id 预约信息主键
     * @return 预约信息
     */
    public Appointment selectAppointmentById(Long id);

    /**
     * 查询预约信息列表
     * 
     * @param appointment 预约信息
     * @return 预约信息集合
     */
    public List<Appointment> selectAppointmentList(Appointment appointment);

    /**
     * 新增预约信息
     * 
     * @param appointment 预约信息
     * @return 结果
     */
    public int insertAppointment(Appointment appointment);

    /**
     * 修改预约信息
     * 
     * @param appointment 预约信息
     * @return 结果
     */
    public int updateAppointment(Appointment appointment);

    /**
     * 删除预约信息
     * 
     * @param id 预约信息主键
     * @return 结果
     */
    public int deleteAppointmentById(Long id);

    /**
     * 批量删除预约信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppointmentByIds(Long[] ids);
}
