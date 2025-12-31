package com.lz.manage.service;

import java.util.List;
import com.lz.manage.model.domain.Seat;
import com.lz.manage.model.vo.seat.SeatVo;
import com.lz.manage.model.dto.seat.SeatQuery;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * 座位信息Service接口
 *
 * @author YY
 * @date 2025-12-31
 */
public interface ISeatService extends IService<Seat>
{
    //region mybatis代码
    /**
     * 查询座位信息
     *
     * @param id 座位信息主键
     * @return 座位信息
     */
    public Seat selectSeatById(Long id);

    /**
     * 查询座位信息列表
     *
     * @param seat 座位信息
     * @return 座位信息集合
     */
    public List<Seat> selectSeatList(Seat seat);

    /**
     * 新增座位信息
     *
     * @param seat 座位信息
     * @return 结果
     */
    public int insertSeat(Seat seat);

    /**
     * 修改座位信息
     *
     * @param seat 座位信息
     * @return 结果
     */
    public int updateSeat(Seat seat);

    /**
     * 批量删除座位信息
     *
     * @param ids 需要删除的座位信息主键集合
     * @return 结果
     */
    public int deleteSeatByIds(Long[] ids);

    /**
     * 删除座位信息信息
     *
     * @param id 座位信息主键
     * @return 结果
     */
    public int deleteSeatById(Long id);
    //endregion
    /**
     * 获取查询条件
     *
     * @param seatQuery 查询条件对象
     * @return 查询条件
     */
    QueryWrapper<Seat> getQueryWrapper(SeatQuery seatQuery);

    /**
     * 转换vo
     *
     * @param seatList Seat集合
     * @return SeatVO集合
     */
    List<SeatVo> convertVoList(List<Seat> seatList);

    /**
     * 导入座位信息数据
     *
     * @param seatList 座位信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importSeatData(List<Seat> seatList, Boolean isUpdateSupport, String operName);
}
