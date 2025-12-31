package com.lz.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.common.core.domain.entity.SysUser;
import com.lz.common.exception.ServiceException;
import com.lz.common.utils.DateUtils;
import com.lz.common.utils.StringUtils;
import com.lz.common.utils.bean.BeanValidators;
import com.lz.common.utils.spring.SpringUtils;
import com.lz.manage.mapper.NotificationMapper;
import com.lz.manage.model.domain.Notification;
import com.lz.manage.model.dto.notification.NotificationQuery;
import com.lz.manage.model.vo.notification.NotificationVo;
import com.lz.manage.service.INotificationService;
import com.lz.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通知Service业务层处理
 *
 * @author YY
 * @date 2025-12-31
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    /**
     * 导入用户数据校验器
     */
    private static Validator validator;

    @Resource
    private NotificationMapper notificationMapper;

    @Resource
    private ISysUserService sysUserService;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码

    /**
     * 查询通知
     *
     * @param id 通知主键
     * @return 通知
     */
    @Override
    public Notification selectNotificationById(Long id) {
        return notificationMapper.selectNotificationById(id);
    }

    /**
     * 查询通知列表
     *
     * @param notification 通知
     * @return 通知
     */
    @Override
    public List<Notification> selectNotificationList(Notification notification) {
        List<Notification> notifications = notificationMapper.selectNotificationList(notification);
        for (Notification info : notifications) {
            SysUser sysUser = sysUserService.selectUserById(info.getUserId());
            if (StringUtils.isNotNull(sysUser)) {
                info.setUserName(sysUser.getUserName());
            }
        }
        return notifications;
    }

    /**
     * 新增通知
     *
     * @param notification 通知
     * @return 结果
     */
    @Override
    public int insertNotification(Notification notification) {
        notification.setCreateTime(DateUtils.getNowDate());
        return notificationMapper.insertNotification(notification);
    }

    /**
     * 修改通知
     *
     * @param notification 通知
     * @return 结果
     */
    @Override
    public int updateNotification(Notification notification) {
        return notificationMapper.updateNotification(notification);
    }

    /**
     * 批量删除通知
     *
     * @param ids 需要删除的通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationByIds(Long[] ids) {
        return notificationMapper.deleteNotificationByIds(ids);
    }

    /**
     * 删除通知信息
     *
     * @param id 通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationById(Long id) {
        return notificationMapper.deleteNotificationById(id);
    }

    //endregion
    @Override
    public QueryWrapper<Notification> getQueryWrapper(NotificationQuery notificationQuery) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = notificationQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = notificationQuery.getId();
        queryWrapper.eq(StringUtils.isNotNull(id), "id", id);

        Long userId = notificationQuery.getUserId();
        queryWrapper.eq(StringUtils.isNotNull(userId), "user_id", userId);

        String titile = notificationQuery.getTitile();
        queryWrapper.like(StringUtils.isNotEmpty(titile), "titile", titile);

        String readFlag = notificationQuery.getReadFlag();
        queryWrapper.eq(StringUtils.isNotEmpty(readFlag), "read_flag", readFlag);

        Date createTime = notificationQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime")) && StringUtils.isNotNull(params.get("endCreateTime")), "create_time", params.get("beginCreateTime"), params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<NotificationVo> convertVoList(List<Notification> notificationList) {
        if (StringUtils.isEmpty(notificationList)) {
            return Collections.emptyList();
        }
        return notificationList.stream().map(NotificationVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入通知数据
     *
     * @param notificationList 通知数据列表
     * @param isUpdateSupport  是否更新支持，如果已存在，则进行更新数据
     * @param operName         操作用户
     * @return 结果
     */
    @Override
    public String importNotificationData(List<Notification> notificationList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(notificationList) || notificationList.size() == 0) {
            throw new ServiceException("导入通知数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Notification notification : notificationList) {
            try {
                // 验证是否存在这个通知
                Long id = notification.getId();
                Notification notificationExist = null;
                if (StringUtils.isNotNull(id)) {
                    notificationExist = notificationMapper.selectNotificationById(id);
                }
                if (StringUtils.isNull(notificationExist)) {
                    BeanValidators.validateWithException(validator, notification);
                    notification.setCreateTime(DateUtils.getNowDate());
                    notificationMapper.insertNotification(notification);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、通知 " + idStr + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, notification);
                    notificationMapper.updateNotification(notification);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、通知 " + id.toString() + " 更新成功");
                } else {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、通知 " + idStr + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                Long id = notification.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、通知 " + idStr + " 导入失败：";
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

}
