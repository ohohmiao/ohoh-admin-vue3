package com.ohohmiao.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohohmiao.framework.security.annotation.SaPcCheckPermission;
import com.ohohmiao.modules.system.model.entity.SysResUrl;
import com.ohohmiao.modules.system.mapper.SysResUrlMapper;
import com.ohohmiao.modules.system.service.SysResUrlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源url Service实现类
 *
 * @author ohohmiao
 * @date 2023-04-06 14:23
 */
@Service
public class SysResUrlServiceImpl extends ServiceImpl<SysResUrlMapper, SysResUrl> implements SysResUrlService {

    @Override
    public Set<String> listByResIds(Collection<String> resIds){
        if(CollUtil.isEmpty(resIds)){
            return CollectionUtil.newHashSet();
        }
        LambdaQueryWrapper<SysResUrl> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysResUrl::getResId, resIds);
        List<SysResUrl> resUrls = this.list(lambdaQueryWrapper);
        if(CollUtil.isEmpty(resUrls)){
            return CollectionUtil.newHashSet();
        }
        return resUrls.stream().map(SysResUrl::getResUrl).collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByResId(String resId){
        LambdaQueryWrapper<SysResUrl> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(SysResUrl::getResId, resId);
        this.remove(deleteWrapper);
    }

    @Override
    public List<String> listUrlByResId(String resId){
        LambdaQueryWrapper<SysResUrl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysResUrl::getResId, resId);
        List<SysResUrl> resUrls = this.list(queryWrapper);
        return resUrls.stream().map(SysResUrl::getResUrl).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(String resId, List<String> urlList){
        this.deleteByResId(resId);
        if (CollUtil.isNotEmpty(urlList)) {
            List<SysResUrl> list = urlList.stream().map(url -> {
                SysResUrl sysResUrl = new SysResUrl();
                sysResUrl.setResId(resId);
                sysResUrl.setResUrl(url);
                return sysResUrl;
            }).collect(Collectors.toList());
            this.saveBatch(list);
        }
    }

    @Override
    public List<String> listSaPcPermissionUrl(){
        List<String> permissionList = CollectionUtil.newArrayList();
        SpringUtil.getApplicationContext().getBeansOfType(RequestMappingHandlerMapping.class).values()
                .forEach(requestMappingHandlerMapping -> requestMappingHandlerMapping.getHandlerMethods().forEach((key, value) -> {
                    SaPcCheckPermission saPcCheckPermission = value.getMethod().getAnnotation(SaPcCheckPermission.class);
                    if(ObjectUtil.isNotNull(saPcCheckPermission)){
                        PatternsRequestCondition patternsCondition = key.getPatternsCondition();
                        if(ObjectUtil.isNotNull(patternsCondition)){
                            permissionList.add(patternsCondition.getPatterns().iterator().next());
                        }
                    }
                }));
        return CollectionUtil.sortByPinyin(permissionList);
    }

}
