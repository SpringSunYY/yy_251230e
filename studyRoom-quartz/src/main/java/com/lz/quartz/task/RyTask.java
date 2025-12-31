package com.lz.quartz.task;

import com.lz.manage.service.IAppointmentService;
import org.springframework.stereotype.Component;
import com.lz.common.utils.StringUtils;

import javax.annotation.Resource;

/**
 * 定时任务调度测试
 *
 * @author YY
 */
@Component("ryTask")
public class RyTask
{

    @Resource
    private IAppointmentService appointmentService;
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    public void autoUpdateAppointment()
    {
        appointmentService.autoUpdateAppointment();
    	System.out.println("执行无参方法,自动更新预约信息");
    }
}
