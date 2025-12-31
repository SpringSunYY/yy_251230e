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
import com.lz.manage.model.domain.StudyRoom;
import com.lz.manage.model.vo.studyRoom.StudyRoomVo;
import com.lz.manage.model.dto.studyRoom.StudyRoomQuery;
import com.lz.manage.model.dto.studyRoom.StudyRoomInsert;
import com.lz.manage.model.dto.studyRoom.StudyRoomEdit;
import com.lz.manage.service.IStudyRoomService;
import com.lz.common.utils.poi.ExcelUtil;
import com.lz.common.core.page.TableDataInfo;

/**
 * 自习室信息Controller
 *
 * @author YY
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/manage/studyRoom")
public class StudyRoomController extends BaseController
{
    @Resource
    private IStudyRoomService studyRoomService;

    /**
     * 查询自习室信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:studyRoom:list')")
    @GetMapping("/list")
    public TableDataInfo list(StudyRoomQuery studyRoomQuery)
    {
        StudyRoom studyRoom = StudyRoomQuery.queryToObj(studyRoomQuery);
        startPage();
        List<StudyRoom> list = studyRoomService.selectStudyRoomList(studyRoom);
        List<StudyRoomVo> listVo= list.stream().map(StudyRoomVo::objToVo).collect(Collectors.toList());
        TableDataInfo table = getDataTable(list);
        table.setRows(listVo);
        return table;
    }

    /**
     * 导出自习室信息列表
     */
    @PreAuthorize("@ss.hasPermi('manage:studyRoom:export')")
    @Log(title = "自习室信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StudyRoomQuery studyRoomQuery)
    {
        StudyRoom studyRoom = StudyRoomQuery.queryToObj(studyRoomQuery);
        List<StudyRoom> list = studyRoomService.selectStudyRoomList(studyRoom);
        ExcelUtil<StudyRoom> util = new ExcelUtil<StudyRoom>(StudyRoom.class);
        util.exportExcel(response, list, "自习室信息数据");
    }

    /**
     * 获取自习室信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('manage:studyRoom:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        StudyRoom studyRoom = studyRoomService.selectStudyRoomById(id);
        return success(StudyRoomVo.objToVo(studyRoom));
    }

    /**
     * 新增自习室信息
     */
    @PreAuthorize("@ss.hasPermi('manage:studyRoom:add')")
    @Log(title = "自习室信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StudyRoomInsert studyRoomInsert)
    {
        StudyRoom studyRoom = StudyRoomInsert.insertToObj(studyRoomInsert);
        return toAjax(studyRoomService.insertStudyRoom(studyRoom));
    }

    /**
     * 修改自习室信息
     */
    @PreAuthorize("@ss.hasPermi('manage:studyRoom:edit')")
    @Log(title = "自习室信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StudyRoomEdit studyRoomEdit)
    {
        StudyRoom studyRoom = StudyRoomEdit.editToObj(studyRoomEdit);
        return toAjax(studyRoomService.updateStudyRoom(studyRoom));
    }

    /**
     * 删除自习室信息
     */
    @PreAuthorize("@ss.hasPermi('manage:studyRoom:remove')")
    @Log(title = "自习室信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(studyRoomService.deleteStudyRoomByIds(ids));
    }

    /**
     * 导入自习室信息数据
     */
    @PreAuthorize("@ss.hasPermi('manage:studyRoom:import')")
    @Log(title = "自习室信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<StudyRoom> util = new ExcelUtil<StudyRoom>(StudyRoom.class);
        List<StudyRoom> studyRoomList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = studyRoomService.importStudyRoomData(studyRoomList, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载自习室信息导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<StudyRoom> util = new ExcelUtil<StudyRoom>(StudyRoom.class);
        util.importTemplateExcel(response, "自习室信息数据");
    }
}
