package com.ohohmiao.modules.system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.system.model.dto.SysRestDayAddOrDeleteDTO;
import com.ohohmiao.modules.system.model.dto.SysRestDayListDTO;
import com.ohohmiao.modules.system.model.vo.FullCalendarEventVO;
import com.ohohmiao.modules.system.service.SysRestDayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 节假日controller
 *
 * @author ohohmiao
 * @date 2024-12-18 14:31
 */
@Api(tags = "节假日")
@ApiSupport(order = 10)
@RestController
public class SysRestDayController {

    @Resource
    private SysRestDayService sysRestDayService;

    /**
     * 获取FullCalendar节假日列表
     * @param sysRestDayListDTO
     * @return
     */
    @ApiOperation(value = "获取FullCalendar节假日列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysRestDay/list4FC")
    @PostMapping("/sysRestDay/list4FC")
    public CommonResp<List<FullCalendarEventVO>> list4FC(@RequestBody @Validated SysRestDayListDTO sysRestDayListDTO){
        return CommonResp.data(sysRestDayService.list4FullCalendar(sysRestDayListDTO));
    }

    /**
     * 新增节假日
     * @param dto
     * @return
     */
    @ApiOperation(value = "新增节假日")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增节假日")
    @SaPcCheckPermission("/sysRestDay/add")
    @PostMapping("/sysRestDay/add")
    public CommonResp<String> add(@RequestBody @Validated SysRestDayAddOrDeleteDTO dto){
        sysRestDayService.add(dto.getDate());
        return CommonResp.success("保存成功");
    }

    /**
     * 删除节假日
     * @param dto
     * @return
     */
    @ApiOperation(value = "删除节假日")
    @ApiOperationSupport(order = 3)
    @CommonLog("删除节假日")
    @SaPcCheckPermission("/sysRestDay/delete")
    @PostMapping("/sysRestDay/delete")
    public CommonResp<String> delete(@RequestBody @Validated SysRestDayAddOrDeleteDTO dto){
        sysRestDayService.delete(dto.getDate());
        return CommonResp.success("删除成功");
    }

}
