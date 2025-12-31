package com.lz.manage.controller;

import com.lz.common.annotation.Log;
import com.lz.common.core.controller.BaseController;
import com.lz.common.core.domain.AjaxResult;
import com.lz.common.core.page.TableDataInfo;
import com.lz.common.enums.BusinessType;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.manage.model.domain.Appointment;
import com.lz.manage.model.dto.appointment.AppointmentEdit;
import com.lz.manage.model.dto.appointment.AppointmentInsert;
import com.lz.manage.model.dto.appointment.AppointmentQuery;
import com.lz.manage.model.vo.appointment.AppointmentVo;
import com.lz.manage.service.IAppointmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 预约信息Controller
 *
 * @author YY
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/manage/appointment")
public class AppointmentController extends BaseController {
    @Resource
    private IAppointmentService appointmentService;

    /**
     * 查询预约信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:appointment:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppointmentQuery appointmentQuery) {
        Appointment appointment = AppointmentQuery.queryToObj(appointmentQuery);
        startPage();
        List<Appointment> list = appointmentService.selectAppointmentList(appointment);
        List<AppointmentVo> listVo = list.stream().map(AppointmentVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出预约信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:appointment:export')")
    @Log(title = "预约信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppointmentQuery appointmentQuery) {
        Appointment appointment = AppointmentQuery.queryToObj(appointmentQuery);
        List<Appointment> list = appointmentService.selectAppointmentList(appointment);
        ExcelUtil<Appointment> util = new ExcelUtil<Appointment>(Appointment.class);
        util.exportExcel(response, list, "预约信息数据");
    }

    /**
     * 获取预约信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:appointment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        Appointment appointment = appointmentService.selectAppointmentById(id);
        return success(AppointmentVo.objToVo(appointment));
    }

    /**
     * 新增预约信息
     */
    @PreAuthorize("@ss.hasPermi('manage:appointment:add')")
    @Log(title = "预约信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Validated AppointmentInsert appointmentInsert) {
        Appointment appointment = AppointmentInsert.insertToObj(appointmentInsert);
        return toAjax(appointmentService.insertAppointment(appointment));
    }

    /**
     * 修改预约信息
     */
    @PreAuthorize("@ss.hasPermi('manage:appointment:edit')")
    @Log(title = "预约信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppointmentEdit appointmentEdit) {
        Appointment appointment = AppointmentEdit.editToObj(appointmentEdit);
        return toAjax(appointmentService.updateAppointment(appointment));
    }

    /**
     * 删除预约信息
     */
    @PreAuthorize("@ss.hasPermi('manage:appointment:remove')")
    @Log(title = "预约信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(appointmentService.deleteAppointmentByIds(ids));
    }

    /**
     * 导入预约信息数据
     */
    @PreAuthorize("@ss.hasPermi('manage:appointment:import')")
    @Log(title = "预约信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Appointment> util = new ExcelUtil<Appointment>(Appointment.class);
        List<Appointment> appointmentList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = appointmentService.importAppointmentData(appointmentList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载预约信息导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Appointment> util = new ExcelUtil<Appointment>(Appointment.class);
        util.importTemplateExcel(response, "预约信息数据");
    }
}
