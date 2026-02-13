package com.ohohmiao.modules.workflow.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ohohmiao.framework.mybatis.model.entity.CommonEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程任务
 *
 * @author ohohmiao
 * @date 2025-06-15 17:48
 */
@Getter
@Setter
@TableName(value = "process_task")
public class ProcessTask extends CommonEntity {

    @TableId
    private String taskId;

}
