package com.ohohmiao.framework.mybatis.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import com.ohohmiao.framework.security.model.pojo.StpLoginUser;
import com.ohohmiao.framework.security.util.StpPCUtil;
import com.ohohmiao.framework.common.enums.CommonWhetherEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis-plus元对象处理器接口实现
 *
 * @author ohohmiao
 * @date 2023-03-30 16:13
 */
@Slf4j
@Component
public class PlatMetaObjectHandler implements MetaObjectHandler {

    /**
     * 伪删除标识
     */
    private static final String DELETE_FLAG = "deleteFlag";

    /**
     * 创建者id
     */
    private static final String CREATE_USERID = "createUserid";

    /**
     * 创建时间
     */
    private static final String CREATE_TIME = "createTime";

    /**
     * 更新者id
     */
    private static final String UPDATE_USERID = "updateUserid";

    /**
     * 更新时间
     */
    private static final String UPDATE_TIME = "updateTime";

    /**
     * 插入时自动填充
     * @param metaObject
     */
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof CommonEntity) {
                CommonEntity commonEntity = (CommonEntity) metaObject.getOriginalObject();
                Date now = ObjectUtil.isNotNull(commonEntity.getCreateTime()) ?
                        commonEntity.getCreateTime() : new Date();
                commonEntity.setDeleteFlag(CommonWhetherEnum.NO.getCode());
                commonEntity.setCreateTime(now);
                if (ObjectUtil.isNull(commonEntity.getCreateUserid())) {
                    StpLoginUser loginUser = getLoginUser();
                    if (ObjectUtil.isNotNull(loginUser)) {
                        commonEntity.setCreateUserid(loginUser.getUserId());
                    }
                }
            } else {
                this.strictInsertFill(metaObject, DELETE_FLAG, Integer.class, CommonWhetherEnum.NO.getCode());
                this.strictInsertFill(metaObject, CREATE_USERID, String.class, StpPCUtil.getLoginIdAsString());
                this.strictInsertFill(metaObject, CREATE_TIME, Date.class, new Date());
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        }
//        this.strictInsertFill(metaObject, DELETE_FLAG, Integer.class, CommonWhetherEnum.NO.getCode());
//        this.strictInsertFill(metaObject, CREATE_USERID, String.class, StpPCUtil.getLoginIdAsString());
//        this.strictInsertFill(metaObject, CREATE_TIME, Date.class, new Date());
    }

    /**
     * 更新时自动填充
     * @param metaObject
     */
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof CommonEntity) {
                CommonEntity commonEntity = (CommonEntity) metaObject.getOriginalObject();
                commonEntity.setUpdateTime(new Date());
                StpLoginUser loginUser = getLoginUser();
                if (ObjectUtil.isNotNull(loginUser)) {
                    commonEntity.setUpdateUserid(loginUser.getUserId());
                }
            } else {
                this.strictUpdateFill(metaObject, UPDATE_USERID, String.class, StpPCUtil.getLoginIdAsString());
                this.strictUpdateFill(metaObject, UPDATE_TIME, Date.class, new Date());
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e));
        }
//        setFieldValByName(UPDATE_USERID, StpPCUtil.getLoginIdAsString(), metaObject);
//        setFieldValByName(UPDATE_TIME, new Date(), metaObject);
    }

    /**
     * 获取登录用户
     * @return
     */
    private StpLoginUser getLoginUser() {
        StpLoginUser loginUser;
        try {
            loginUser = StpPCUtil.getLoginUser();
        } catch (Exception e) {
            return null;
        }
        return loginUser;
    }

}
