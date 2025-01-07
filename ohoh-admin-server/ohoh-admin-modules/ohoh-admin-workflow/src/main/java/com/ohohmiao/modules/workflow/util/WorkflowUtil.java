package com.ohohmiao.modules.workflow.util;

import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;

/**
 * 工作流操作工具类
 *
 * @author ohohmiao
 * @date 2025-01-07 22:00
 */
public class WorkflowUtil {

    private static JSONPath BPMN_PROCESS_JSON_PATH = JSONPath.of("$.definitions.process");

    private static JSONPath START_EVENT_JSON_PATH = JSONPath.of("$.startEvent[?(@.nodetype=='start')]");

    public static Object getBpmnProcessNode(String json){
        JSONReader parser = JSONReader.of(json);
        return BPMN_PROCESS_JSON_PATH.extract(parser);
    }

    /**
     * 获取开始事件节点
     * @param defJson
     * @return
     */
    public static Object getStartEventNode(String defJson) {
        JSONReader parser = JSONReader.of(defJson);
        return START_EVENT_JSON_PATH.extract(parser);
    }

}
