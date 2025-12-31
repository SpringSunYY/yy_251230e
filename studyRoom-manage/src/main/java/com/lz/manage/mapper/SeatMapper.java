package com.lz.manage.mapper;

import java.util.List;
import com.lz.manage.model.domain.Seat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 座位信息Mapper接口
 *
 * @author YY
 * @date 2025-12-31
 */
public interface SeatMapper extends BaseMapper<Seat>
{
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
     * 删除座位信息
     *
     * @param id 座位信息主键
     * @return 结果
     */
    public int deleteSeatById(Long id);

    /**
     * 批量删除座位信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSeatByIds(Long[] ids);
}
