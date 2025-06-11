package com.ohohmiao.modules.system.provider;

import com.ohohmiao.modules.system.api.SysUserApi;
import com.ohohmiao.modules.system.enums.SysReferResTypeEnum;
import com.ohohmiao.modules.system.mapper.SysUserMapper;
import com.ohohmiao.modules.system.model.pojo.SysReferRes;
import com.ohohmiao.modules.system.model.vo.SysUserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统用户api实现
 *
 * @author ohohmiao
 * @date 2025-06-02 10:24
 */
@Service("sysUserApi")
public class SysUserApiProvider implements SysUserApi {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUserVO> listByReferRes(List<SysReferRes> referResList){
        Map<String, List<SysReferRes>> groupedByType = referResList.stream().collect(
                Collectors.groupingBy(SysReferRes::getReferRestype));
        // TODO 联系组
        return sysUserMapper.listByReferRes(
             groupedByType.get(SysReferResTypeEnum.USER.getCode()),
             groupedByType.get(SysReferResTypeEnum.ORG.getCode()),
             groupedByType.get(SysReferResTypeEnum.POSITION.getCode()),
             null);
    }

    @Override
    public List<SysUserVO> doFilterBySameOrg(List<String> sourceUserIdList, String targetOrgId){
        return sysUserMapper.doFilterBySameOrg(sourceUserIdList, targetOrgId);
    }

    @Override
    public List<SysUserVO> doFilterByOneLevelUpOrg(List<String> sourceUserIdList, String targetOrgId){
        return sysUserMapper.doFilterByOneLevelUpOrg(sourceUserIdList, targetOrgId);
    }

    @Override
    public List<SysUserVO> doFilterBySameAndChildOrg(List<String> sourceUserIdList, String targetOrgId){
        return sysUserMapper.doFilterBySameAndChildOrg(sourceUserIdList, targetOrgId);
    }

}
