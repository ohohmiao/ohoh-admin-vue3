package com.ohohmiao.modules.system;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohohmiao.BaseTestCase;
import com.ohohmiao.framework.mybatis.page.CommonPageRequest;
import com.ohohmiao.modules.system.mapper.SysRoleMapper;
import com.ohohmiao.modules.system.model.vo.SysRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import javax.annotation.Resource;

import java.util.List;

/**
 * 角色测试用例
 * @author ohohmiao
 * @date 2023/4/15 12:38
 */
@Slf4j
public class SysRoleTestCase extends BaseTestCase {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Test
    public void pageByWrapper(){
        QueryWrapper<SysRoleVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("o.ORG_ID");
        queryWrapper.eq("r.ORG_ID", "1646530342812688386");
        Page<SysRoleVO> result = sysRoleMapper.pageByWrapper(CommonPageRequest.defaultPage(), queryWrapper);
        List<SysRoleVO> records = result.getRecords();
        log.info(result.getTotal() + "#" + records.size());
        for(SysRoleVO sysRoleVO : records){
            log.info(StrUtil.format("{}#{}", sysRoleVO.getRoleName(), sysRoleVO.getOrgId()));
        }
    }

}
