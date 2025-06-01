package com.ohohmiao.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ohohmiao.framework.common.model.vo.CommonSelectVO;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.framework.log.annotation.CommonLog;
import com.ohohmiao.framework.common.model.dto.CommonIdListDTO;
import com.ohohmiao.framework.common.model.pojo.CommonResp;
import com.ohohmiao.framework.common.validation.group.CommonAddGroup;
import com.ohohmiao.framework.common.validation.group.CommonEditGroup;
import com.ohohmiao.modules.system.model.dto.SysDicAddOrEditDTO;
import com.ohohmiao.modules.system.model.dto.SysDicPageDTO;
import com.ohohmiao.modules.system.model.dto.SysDicSelectDTO;
import com.ohohmiao.modules.system.model.vo.SysDicVO;
import com.ohohmiao.modules.system.service.SysDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统字典controller
 *
 * @author ohohmiao
 * @date 2023-05-25 11:59
 */
@Api(tags = "系统字典")
@ApiSupport(order = 2)
@RestController
public class SysDicController {

    @Resource
    private SysDicService sysDicService;

    /**
     * 获取系统字典分页列表
     * @param sysDicPageDTO 字典分页dto
     * @return
     */
    @ApiOperation(value = "获取系统字典分页列表")
    @ApiOperationSupport(order = 1)
    @SaPcCheckPermission("/sysDic/page")
    @PostMapping("/sysDic/page")
    public CommonResp<Page<SysDicVO>> page(@RequestBody SysDicPageDTO sysDicPageDTO){
        return CommonResp.data(sysDicService.listByPage(sysDicPageDTO));
    }

    /**
     * 新增系统字典
     * @param sysDicAddOrEditDTO 新增字典dto
     * @return
     */
    @ApiOperation(value = "新增系统字典")
    @ApiOperationSupport(order = 2)
    @CommonLog("新增系统字典")
    @SaPcCheckPermission("/sysDic/add")
    @PostMapping("/sysDic/add")
    public CommonResp<String> add(
            @RequestBody @Validated(CommonAddGroup.class) SysDicAddOrEditDTO sysDicAddOrEditDTO){
        sysDicService.add(sysDicAddOrEditDTO);
        return CommonResp.success("保存成功");
    }

    /**
     * 修改系统字典
     * @param sysDicAddOrEditDTO 修改字典dto
     * @return
     */
    @ApiOperation(value = "修改系统字典")
    @ApiOperationSupport(order = 3)
    @CommonLog("修改系统字典")
    @SaPcCheckPermission("/sysDic/edit")
    @PostMapping("/sysDic/edit")
    public CommonResp<String> edit(
            @RequestBody @Validated(CommonEditGroup.class) SysDicAddOrEditDTO sysDicAddOrEditDTO){
        sysDicService.edit(sysDicAddOrEditDTO);
        return CommonResp.success("修改成功");
    }

    /**
     * 删除系统字典
     * @param idListDTO id集合dto
     * @return
     */
    @ApiOperation(value = "删除系统字典")
    @ApiOperationSupport(order = 4)
    @CommonLog("删除系统字典")
    @SaPcCheckPermission("/sysDic/delete")
    @PostMapping("/sysDic/delete")
    public CommonResp<String> delete(@RequestBody @Validated CommonIdListDTO idListDTO){
        sysDicService.delete(idListDTO);
        return CommonResp.success("删除成功");
    }

    /**
     * 获取某类别字典的下拉框数据
     * @param selectDTO
     * @return
     */
    @ApiOperation(value = "获取某类别字典的下拉框数据")
    @ApiOperationSupport(order = 5)
    @SaPcCheckPermission("/sysDic/select")
    @PostMapping("/sysDic/select")
    public CommonResp<List<CommonSelectVO>> select(@RequestBody @Validated SysDicSelectDTO selectDTO){
        return CommonResp.data(sysDicService.select(selectDTO.getDictypeCode()));
    }

}
