package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.security.enums.AuthConstEnum;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 登录用户对象
 *
 * @author ohohmiao
 * @date 2023/4/5 17:04
 */
@ApiModel("登录用户对象")
@Getter
@Setter
public class SysLoginUser extends StpLoginUser {

    @Override
    public Boolean isSuperAdmin() {
        // 更改是否超管判断逻辑，修改此处
        return this.getUserAccount().equals(AuthConstEnum.SUPERADMIN_ACCOUNT.getValue());
    }

}
