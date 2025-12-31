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
import com.lz.manage.model.domain.Notification;
import com.lz.manage.model.vo.notification.NotificationVo;
import com.lz.manage.model.dto.notification.NotificationQuery;
import com.lz.manage.model.dto.notification.NotificationInsert;
import com.lz.manage.model.dto.notification.NotificationEdit;
import com.lz.manage.service.INotificationService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 通知Controller
 *
 * @author YY
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/manage/notification")
public class NotificationController extends BaseController
{
    @Resource
    private INotificationService notificationService;

    /**
     * 查询通知列表
     */
    @PreAuthorize("@ss.hasPermi('manage:notification:list')")
    @GetMapping("/list")
    public TableDataInfo list(NotificationQuery notificationQuery)
    {
        Notification notification = NotificationQuery.queryToObj(notificationQuery);
        startPage();
        List<Notification> list = notificationService.selectNotificationList(notification);
        List<NotificationVo> listVo= list.stream().map(NotificationVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出通知列表
     */
    @PreAuthorize("@ss.hasPermi('manage:notification:export')")
    @Log(title = "通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NotificationQuery notificationQuery)
    {
        Notification notification = NotificationQuery.queryToObj(notificationQuery);
        List<Notification> list = notificationService.selectNotificationList(notification);
        ExcelUtil<Notification> util = new ExcelUtil<Notification>(Notification.class);
        util.exportExcel(response, list, "通知数据");
    }

    /**
     * 获取通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:notification:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        Notification notification = notificationService.selectNotificationById(id);
        return success(NotificationVo.objToVo(notification));
    }

    /**
     * 新增通知
     */
    @PreAuthorize("@ss.hasPermi('manage:notification:add')")
    @Log(title = "通知", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NotificationInsert notificationInsert)
    {
        Notification notification = NotificationInsert.insertToObj(notificationInsert);
        return toAjax(notificationService.insertNotification(notification));
    }

    /**
     * 修改通知
     */
    @PreAuthorize("@ss.hasPermi('manage:notification:edit')")
    @Log(title = "通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NotificationEdit notificationEdit)
    {
        Notification notification = NotificationEdit.editToObj(notificationEdit);
        return toAjax(notificationService.updateNotification(notification));
    }

    /**
     * 删除通知
     */
    @PreAuthorize("@ss.hasPermi('manage:notification:remove')")
    @Log(title = "通知", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(notificationService.deleteNotificationByIds(ids));
    }

    /**
     * 导入通知数据
     */
    @PreAuthorize("@ss.hasPermi('manage:notification:import')")
    @Log(title = "通知", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<Notification> util = new ExcelUtil<Notification>(Notification.class);
        List<Notification> notificationList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = notificationService.importNotificationData(notificationList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载通知导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Notification> util = new ExcelUtil<Notification>(Notification.class);
        util.importTemplateExcel(response, "通知数据");
    }
}
