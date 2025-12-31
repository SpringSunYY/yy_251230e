package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.annotation.DataScope;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.SecurityUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.ThrowUtils;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.manage.mapper.AppointmentMapper;
import com.lz.manage.model.domain.Appointment;
import com.lz.manage.model.domain.Notification;
import com.lz.manage.model.domain.Seat;
import com.lz.manage.model.domain.StudyRoom;
import com.lz.manage.model.dto.appointment.AppointmentQuery;
import com.lz.manage.model.enums.AppointmentStatusEnum;
import com.lz.manage.model.enums.SeatStatusEnum;
import com.lz.manage.model.enums.StudyRoomStatusEnum;
import com.lz.manage.model.vo.appointment.AppointmentVo;
import com.lz.manage.service.IAppointmentService;
import com.lz.manage.service.INotificationService;
import com.lz.manage.service.ISeatService;
import com.lz.manage.service.IStudyRoomService;
import com.lz.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预约信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-31
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements IAppointmentService {
    private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    /**
     * 导入用户数据校验器
     */
    private static Validator validator;

    @Resource
    private AppointmentMapper appointmentMapper;

    @Resource
    private ISeatService seatService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IStudyRoomService studyRoomService;

    @Resource
    private INotificationService notificationService;

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
    public Appointment selectAppointmentById(Long id) {
        return appointmentMapper.selectAppointmentById(id);
    }

    /**
     * 查询预约信息列表
     *
     * @param appointment 预约信息
     * @return 预约信息
     */
    @DataScope(deptAlias = "tb_appointment", userAlias = "tb_appointment")
    @Override
    public List<Appointment> selectAppointmentList(Appointment appointment) {
        List<Appointment> appointments = appointmentMapper.selectAppointmentList(appointment);
        for (Appointment info : appointments) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
            StudyRoom studyRoom = studyRoomService.selectStudyRoomById(info.getRoomId());
            if (StringUtils.isNotNull(studyRoom)) {
                info.setRoomName(studyRoom.getName());
            }
            Seat seat = seatService.selectSeatById(info.getSeatId());
            if (StringUtils.isNotNull(seat)) {
                info.setSeatName(seat.getName());
            }
        }
        return appointments;
    }

    /**
     * 新增预约信息
     *
     * @param appointment 预约信息
     * @return 结果
     */
    @Override
    public int insertAppointment(Appointment appointment) {
        //如果预约时间小于当前时间
        Date nowDate = DateUtils.getNowDate();
        ThrowUtils.throwIf(appointment.getAppointmentTime().before(DateUtils.addDays(nowDate,-1)),
                new ServiceException("预约时间不能早于当前时间"));
        //首先查询教师状态是否开启
        StudyRoom studyRoom = studyRoomService.selectStudyRoomById(appointment.getRoomId());
        ThrowUtils.throwIf(StringUtils.isNull(studyRoom), new ServiceException("教室不存在"));
        ThrowUtils.throwIf(!studyRoom.getStatus().equals(StudyRoomStatusEnum.STUDY_ROOM_STATUS_0.getValue()),
                new ServiceException("教室已关闭"));
        //查询座位是否存在且空闲
        Seat seat = seatService.selectSeatById(appointment.getSeatId());
        ThrowUtils.throwIf(StringUtils.isNull(seat),
                new ServiceException("座位不存在"));
        //查询当天座位是否预约
        String appointmentTimeStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, appointment.getAppointmentTime());
        List<Appointment> list = getAppointments(seat.getId(), appointmentTimeStr);
        ThrowUtils.throwIf(StringUtils.isNotEmpty(list),
                new ServiceException("该座位已预约这一天已被预约"));
        //如果预约时间刚好是今天
        if (appointmentTimeStr
                .equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, nowDate))) {
            //更新座位已被预约
            seat.setStatus(SeatStatusEnum.SEAT_STATUS_1.getValue());
            seatService.updateById(seat);
        }
        appointment.setUserId(SecurityUtils.getUserId());
        appointment.setStatus(AppointmentStatusEnum.APPOINTMENT_STATUS_0.getValue());
        appointment.setCreateTime(nowDate);

        //预约成功，发送消息提醒
        Notification notification = new Notification();
        notification.setUserId(appointment.getUserId());
        notification.setTitile("教室预约成功");
        notification.setContent(StringUtils.format("您已成功预约教室：{}，座位：{}，请于{}前往教室，如果需要取消请在预约记录取消",
                studyRoom.getName(), seat.getName(), appointmentTimeStr));
        notificationService.insertNotification(notification);
        return appointmentMapper.insertAppointment(appointment);
    }

    @Override
    public List<Appointment> getAppointments(Long seatId, String appointmentTimeStr) {
        List<Appointment> list = this.list(new LambdaQueryWrapper<Appointment>()
                .eq(Appointment::getSeatId, seatId)
                .eq(Appointment::getStatus, AppointmentStatusEnum.APPOINTMENT_STATUS_0.getValue())
                .apply("DATE_FORMAT(appointment_time, '%Y-%m-%d') = {0}", appointmentTimeStr));
        return list;
    }

    /**
     * 修改预约信息
     *
     * @param appointment 预约信息
     * @return 结果
     */
    @Override
    public int updateAppointment(Appointment appointment) {
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
    public int deleteAppointmentByIds(Long[] ids) {
        return appointmentMapper.deleteAppointmentByIds(ids);
    }

    /**
     * 删除预约信息信息
     *
     * @param id 预约信息主键
     * @return 结果
     */
    @Override
    public int deleteAppointmentById(Long id) {
        return appointmentMapper.deleteAppointmentById(id);
    }

    //endregion
    @Override
    public QueryWrapper<Appointment> getQueryWrapper(AppointmentQuery appointmentQuery) {
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = appointmentQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = appointmentQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        Date appointmentTime = appointmentQuery.getAppointmentTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginAppointmentTime")) && StringUtils.isNotNull(params.get("endAppointmentTime")), "appointment_time", params.get("beginAppointmentTime"), params.get("endAppointmentTime"));

        String status = appointmentQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status), "status", status);

        Long userId = appointmentQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotNull(userId), "user_id", userId);

        Date createTime = appointmentQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

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
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importAppointmentData(List<Appointment> appointmentList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(appointmentList) || appointmentList.size() == 0) {
            throw new ServiceException("导入预约信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Appointment appointment : appointmentList) {
            try {
                // 验证是否存在这个预约信息
                Long id = appointment.getId();
                Appointment appointmentExist = null;
                if (StringUtils.isNotNull(id)) {
                    appointmentExist = appointmentMapper.selectAppointmentById(id);
                }
                if (StringUtils.isNull(appointmentExist)) {
                    BeanValidators.validateWithException(validator, appointment);
                    appointment.setCreateTime(DateUtils.getNowDate());
                    appointmentMapper.insertAppointment(appointment);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、预约信息 " + idStr + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, appointment);
                    appointment.setUpdateTime(DateUtils.getNowDate());
                    appointmentMapper.updateAppointment(appointment);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、预约信息 " + id.toString() + " 更新成功");
                } else {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、预约信息 " + idStr + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                Long id = appointment.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、预约信息 " + idStr + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public void autoUpdateAppointment() {
        //首先查询到所有的座位信息
        List<Seat> seats = seatService.list();
        Date nowDate = DateUtils.getNowDate();
        String dateToStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, nowDate);
        List<Notification> notifications = new ArrayList<>();
        for (Seat seat : seats) {
            List<Appointment> appointments = this.getAppointments(seat.getId(), dateToStr);
            if (StringUtils.isEmpty(appointments)) {
                seat.setStatus(SeatStatusEnum.SEAT_STATUS_0.getValue());
            } else {
                seat.setStatus(SeatStatusEnum.SEAT_STATUS_1.getValue());
                Notification notification = new Notification();
                notification.setUserId(appointments.get(0).getUserId());
                notification.setTitile("您预约的座位已到时间，请立即前往");
                notification.setContent(StringUtils.format("您预约的教室已到时间：请立即前往，如果需要取消请在预约记录取消"));
                notifications.add(notification);
            }
        }
        notificationService.saveBatch(notifications);
        seatService.updateBatchById(seats);
        //查询所有的以预约信息
        List<Appointment> appointments = this.list(new LambdaQueryWrapper<Appointment>()
                .eq(Appointment::getStatus, AppointmentStatusEnum.APPOINTMENT_STATUS_0.getValue()));
        for (Appointment appointment : appointments) {
            //判断预约时间是否已经过了
            Date date = DateUtils.addDays(appointment.getAppointmentTime(), 1);
            if (date.before(nowDate)) {
                appointment.setStatus(AppointmentStatusEnum.APPOINTMENT_STATUS_1.getValue());
            }
        }
        this.updateBatchById(appointments);
    }

}
