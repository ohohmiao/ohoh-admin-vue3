package com.ohohmiao.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.modules.system.model.entity.SysLog;
import com.ohohmiao.modules.system.model.dto.SysLogPageDTO;
import com.ohohmiao.modules.system.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统日志controller
 *
 * @author ohohmiao
 * @date 2023-08-07 16:10
 */
@Api(tags = "系统日志")
@ApiSupport(order = 5)
@RestController
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    /**
     * 获取系统日志分页列表
     * @param sysLogPageDTO 分页dto
     * @return
     */
    @ApiOperation(value = "获取系统日志分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysLog/page")
    @PostMapping("/sysLog/page")
    public CommonResp<Page<SysLog>> page(@RequestBody SysLogPageDTO sysLogPageDTO){
        return CommonResp.data(sysLogService.listByPage(sysLogPageDTO));
    }

}
