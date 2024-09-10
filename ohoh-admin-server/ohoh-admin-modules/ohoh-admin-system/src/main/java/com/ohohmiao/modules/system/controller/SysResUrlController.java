package com.ohohmiao.modules.system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.common.model.dto.CommonIdDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.modules.system.service.SysResUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统资源url controller
 *
 * @author ohohmiao
 * @date 2023-08-18 16:10
 */
@Api(tags = "系统资源url")
@ApiSupport(order = 4)
@RestController
public class SysResUrlController {

    @Resource
    private SysResUrlService sysResUrlService;

    /**
     * 根据资源id，列出资源url
     * @param idDTO 
     * @return
     */
    @ApiOperation(value = "根据资源id，列出资源url")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysResUrl/listUrlByResId")
    @PostMapping("/sysResUrl/listUrlByResId")
    public CommonResp<List<String>> listUrlByResId(@RequestBody @Validated CommonIdDTO idDTO){
        return CommonResp.data(sysResUrlService.listUrlByResId(idDTO.getId()));
    }

    /**
     * 列出SaPcCheckPermission注解的权限url
     * @return
     */
    @ApiOperation(value = "列出SaPcCheckPermission注解的权限url")
    @ApiOperationSupport(order = 2)
    @SaPcCheckPermission("/sysResUrl/listSaPcPermissionUrl")
    @PostMapping("/sysResUrl/listSaPcPermissionUrl")
    public CommonResp<List<String>> listSaPcPermissionUrl(){
        return CommonResp.data(sysResUrlService.listSaPcPermissionUrl());
    }

}
