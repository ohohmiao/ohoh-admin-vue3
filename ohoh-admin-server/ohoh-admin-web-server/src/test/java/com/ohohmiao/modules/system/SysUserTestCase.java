package com.ohohmiao.modules.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ohohmiao.BaseTestCase;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import com.ohohmiao.modules.system.model.entity.SysUser;
import com.ohohmiao.modules.system.enums.SysUserPropEnum;
import com.ohohmiao.modules.system.mapper.SysUserMapper;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import com.ohohmiao.modules.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import javax.annotation.Resource;

import java.util.List;

/**
 * 用户相关测试用例
 *
 * @author ohohmiao
 * @date 2023-03-30 16:56
 */
@Slf4j
public class SysUserTestCase extends BaseTestCase {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysUserService sysUserService;

    @Test
    public void autoFillInsert(){
        SysUser sysUser = new SysUser();
        sysUser.setUserId("1");
        sysUserService.saveOrUpdate(sysUser);
    }

    @Test
    public void autoFillUpdate(){
        SysUser sysUser = sysUserMapper.selectById("1");
        sysUser.setUserName("嘻嘻嘻");
        sysUserService.saveOrUpdate(sysUser);
    }

    @Test
    public void listByWrapper(){
        QueryWrapper<SysUserVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("p.USER_ID");
        queryWrapper.isNotNull("o.ORG_ID");
        queryWrapper.eq("p.PROP_TABLENAME", SysUserPropEnum.SYSORG.getValue());
        queryWrapper.eq("u.DELETE_FLAG", CommonWhetherEnum.NO.getCode());
        List<SysUserVO> users = sysUserMapper.listByWrapper(queryWrapper);
        for(SysUserVO user: users){
            log.info(user.getUserName());
        }
    }

}
