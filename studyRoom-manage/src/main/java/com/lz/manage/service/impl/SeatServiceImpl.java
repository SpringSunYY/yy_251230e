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
import com.lz.manage.mapper.SeatMapper;
import com.lz.manage.model.domain.Seat;
import com.lz.manage.service.ISeatService;
import com.lz.manage.model.dto.seat.SeatQuery;
import com.lz.manage.model.vo.seat.SeatVo;

/**
 * 座位信息Service业务层处理
 *
 * @author YY
 * @date 2025-12-31
 */
@Service
public class SeatServiceImpl extends ServiceImpl<SeatMapper, Seat> implements ISeatService
{
    private static final Logger log = LoggerFactory.getLogger(SeatServiceImpl.class);

    /** 导入用户数据校验器 */
    private static Validator validator;

    @Resource
    private SeatMapper seatMapper;

    {
        validator = SpringUtils.getBean(Validator.class);
    }

    //region mybatis代码
    /**
     * 查询座位信息
     *
     * @param id 座位信息主键
     * @return 座位信息
     */
    @Override
    public Seat selectSeatById(Long id)
    {
        return seatMapper.selectSeatById(id);
    }

    /**
     * 查询座位信息列表
     *
     * @param seat 座位信息
     * @return 座位信息
     */
    @Override
    public List<Seat> selectSeatList(Seat seat)
    {
        return seatMapper.selectSeatList(seat);
    }

    /**
     * 新增座位信息
     *
     * @param seat 座位信息
     * @return 结果
     */
    @Override
    public int insertSeat(Seat seat)
    {
        seat.setCreateTime(DateUtils.getNowDate());
        return seatMapper.insertSeat(seat);
    }

    /**
     * 修改座位信息
     *
     * @param seat 座位信息
     * @return 结果
     */
    @Override
    public int updateSeat(Seat seat)
    {
        seat.setUpdateTime(DateUtils.getNowDate());
        return seatMapper.updateSeat(seat);
    }

    /**
     * 批量删除座位信息
     *
     * @param ids 需要删除的座位信息主键
     * @return 结果
     */
    @Override
    public int deleteSeatByIds(Long[] ids)
    {
        return seatMapper.deleteSeatByIds(ids);
    }

    /**
     * 删除座位信息信息
     *
     * @param id 座位信息主键
     * @return 结果
     */
    @Override
    public int deleteSeatById(Long id)
    {
        return seatMapper.deleteSeatById(id);
    }
    //endregion
    @Override
    public QueryWrapper<Seat> getQueryWrapper(SeatQuery seatQuery){
        QueryWrapper<Seat> queryWrapper = new QueryWrapper<>();
        //如果不使用params可以删除
        Map<String, Object> params = seatQuery.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        Long id = seatQuery.getId();
        queryWrapper.eq( StringUtils.isNotNull(id),"id",id);

        String name = seatQuery.getName();
        queryWrapper.like(StringUtils.isNotEmpty(name) ,"name",name);

        String status = seatQuery.getStatus();
        queryWrapper.eq(StringUtils.isNotEmpty(status) ,"status",status);

        Date createTime = seatQuery.getCreateTime();
        queryWrapper.between(StringUtils.isNotNull(params.get("beginCreateTime"))&&StringUtils.isNotNull(params.get("endCreateTime")),"create_time",params.get("beginCreateTime"),params.get("endCreateTime"));

        return queryWrapper;
    }

    @Override
    public List<SeatVo> convertVoList(List<Seat> seatList) {
        if (StringUtils.isEmpty(seatList)) {
            return Collections.emptyList();
        }
        return seatList.stream().map(SeatVo::objToVo).collect(Collectors.toList());
    }

    /**
     * 导入座位信息数据
     *
     * @param seatList 座位信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importSeatData(List<Seat> seatList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(seatList) || seatList.size() == 0)
        {
            throw new ServiceException("导入座位信息数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Seat seat : seatList)
        {
            try
            {
                // 验证是否存在这个座位信息
                Long id = seat.getId();
                Seat seatExist = null;
                if (StringUtils.isNotNull(id))
                {
                    seatExist = seatMapper.selectSeatById(id);
                }
                if (StringUtils.isNull(seatExist))
                {
                    BeanValidators.validateWithException(validator, seat);
                    seat.setCreateTime(DateUtils.getNowDate());
                    seatMapper.insertSeat(seat);
                    successNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "新记录";
                    successMsg.append("<br/>" + successNum + "、座位信息 " + idStr + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, seat);
                    seat.setUpdateTime(DateUtils.getNowDate());
                    seatMapper.updateSeat(seat);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、座位信息 " + id.toString() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                    failureMsg.append("<br/>" + failureNum + "、座位信息 " + idStr + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                Long id = seat.getId();
                String idStr = StringUtils.isNotNull(id) ? id.toString() : "未知";
                String msg = "<br/>" + failureNum + "、座位信息 " + idStr + " 导入失败：";
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
