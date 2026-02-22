package com.ohohmiao.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohohmiao.modules.workflow.model.entity.ProcessTask;
import com.ohohmiao.modules.workflow.model.vo.ProcessTaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程任务mapper
 *
 * @author ohohmiao
 * @date 2026-02-14 15:18
 */
@Mapper
public interface ProcessTaskMapper extends BaseMapper<ProcessTask> {

    /**
     * 列出串审环节的等待办理人
     * @param processId
     * @param taskId
     * @param taskState
     * @return
     */
    List<ProcessTaskVO> listMultiHandleNodeNextWaitingTasks(
            @Param("processId") String processId, @Param("taskId") String taskId, @Param("taskState") Integer taskState);

}
