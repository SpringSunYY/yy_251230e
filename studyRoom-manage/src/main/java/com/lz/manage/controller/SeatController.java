package com.lz.manage.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.enums.BusinessType;
import com.lz.manage.model.domain.Seat;
import com.lz.manage.model.vo.seat.SeatVo;
import com.lz.manage.model.dto.seat.SeatQuery;
import com.lz.manage.model.dto.seat.SeatInsert;
import com.lz.manage.model.dto.seat.SeatEdit;
import com.lz.manage.service.ISeatService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 座位信息Controller
 *
 * @author YY
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/manage/seat")
public class SeatController extends BaseController
{
    @Resource
    private ISeatService seatService;

    /**
     * 查询座位信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:seat:list')")
    @GetMapping("/list")
    public TableDataInfo list(SeatQuery seatQuery)
    {
        Seat seat = SeatQuery.queryToObj(seatQuery);
        startPage();
        List<Seat> list = seatService.selectSeatList(seat);
        List<SeatVo> listVo= list.stream().map(SeatVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出座位信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:seat:export')")
    @Log(title = "座位信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SeatQuery seatQuery)
    {
        Seat seat = SeatQuery.queryToObj(seatQuery);
        List<Seat> list = seatService.selectSeatList(seat);
        ExcelUtil<Seat> util = new ExcelUtil<Seat>(Seat.class);
        util.exportExcel(response, list, "座位信息数据");
    }

    /**
     * 获取座位信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:seat:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        Seat seat = seatService.selectSeatById(id);
        return success(SeatVo.objToVo(seat));
    }

    /**
     * 新增座位信息
     */
    @PreAuthorize("@ss.hasPermi('manage:seat:add')")
    @Log(title = "座位信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SeatInsert seatInsert)
    {
        Seat seat = SeatInsert.insertToObj(seatInsert);
        return toAjax(seatService.insertSeat(seat));
    }

    /**
     * 修改座位信息
     */
    @PreAuthorize("@ss.hasPermi('manage:seat:edit')")
    @Log(title = "座位信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SeatEdit seatEdit)
    {
        Seat seat = SeatEdit.editToObj(seatEdit);
        return toAjax(seatService.updateSeat(seat));
    }

    /**
     * 删除座位信息
     */
    @PreAuthorize("@ss.hasPermi('manage:seat:remove')")
    @Log(title = "座位信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(seatService.deleteSeatByIds(ids));
    }

    /**
     * 导入座位信息数据
     */
    @PreAuthorize("@ss.hasPermi('manage:seat:import')")
    @Log(title = "座位信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<Seat> util = new ExcelUtil<Seat>(Seat.class);
        List<Seat> seatList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = seatService.importSeatData(seatList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载座位信息导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Seat> util = new ExcelUtil<Seat>(Seat.class);
        util.importTemplateExcel(response, "座位信息数据");
    }
}
