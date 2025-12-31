package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.Appointment;
import com.lz.manage.model.vo.appointment.AppointmentVo;
import com.lz.manage.model.dto.appointment.AppointmentQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 预约信息Service接口
 *
 * @author YY
 * @date 2025-12-31
 */
public interface IAppointmentService extends IService<Appointment>
{
    //region mybatis代码
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

    List<Appointment> getAppointments(Long seatId, String appointmentTimeStr);

    /**
     * 修改预约信息
     *
     * @param appointment 预约信息
     * @return 结果
     */
    public int updateAppointment(Appointment appointment);

    /**
     * 批量删除预约信息
     *
     * @param ids 需要删除的预约信息主键集合
     * @return 结果
     */
    public int deleteAppointmentByIds(Long[] ids);

    /**
     * 删除预约信息信息
     *
     * @param id 预约信息主键
     * @return 结果
     */
    public int deleteAppointmentById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param appointmentQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<Appointment> getQueryWrapper(AppointmentQuery appointmentQuery);

    /**
     * 转换vo
     *
     * @param appointmentList Appointment集合
     * @return AppointmentVO集合
     */
    List<AppointmentVo> convertVoList(List<Appointment> appointmentList);

    /**
     * 导入预约信息数据
     *
     * @param appointmentList 预约信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importAppointmentData(List<Appointment> appointmentList, Boolean isUpdateSupport, String operName);

    void autoUpdateAppointment();

}
