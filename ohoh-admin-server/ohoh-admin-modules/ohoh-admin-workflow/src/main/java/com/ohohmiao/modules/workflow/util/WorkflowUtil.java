package com.ohohmiao.modules.workflow.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作流操作工具类
 *
 * @author ohohmiao
 * @date 2025-01-07 22:00
 */
public class WorkflowUtil {

    /** 流程定义节点path **/
    private static JSONPath BPMN_PROCESS_JSON_PATH = JSONPath.of("$.definitions.process");

    /** 开始节点path **/
    private static JSONPath START_EVENT_JSON_PATH = JSONPath.of("$.startEvent[?(@.nodetype=='start')]");

    /**
     * 获取流程定义节点
     * @param json
     * @return
     */
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

    /**
     * 获取指定节点
     * @param defJson
     * @param nodeId
     * @return
     */
    public static Map getFlowNode(String defJson, String nodeId){
        Object node = JSONPath.extract(defJson, "$..[?(@.id == '" + nodeId + "')]");
        if(ObjectUtil.isNotNull(node)){
            return JSON.parseArray(node.toString(), Map.class).get(0);
        }else{
            return null;
        }
    }

    /**
     * 获取输入节点
     * @param defJson
     * @param incoming
     * @return
     */
    public static Map getInComingNode(String defJson, String incoming){
        Object node = JSONPath.extract(defJson, "$..[?(@.incoming == '" + incoming + "')]");
        if(ObjectUtil.isNotNull(node)){
            if(((JSONArray)node).isEmpty()){
                List<JSONObject> candidates = (List<JSONObject>)
                        JSONPath.extract(defJson, "$..[?(@.incoming)]");
                if(!candidates.isEmpty()){
                    node = candidates.stream().filter(obj -> {
                            List<String> incomingList = obj.getList("incoming", String.class);
                            return incomingList != null && incomingList.contains(incoming);
                        }).collect(Collectors.toList());
                }
            }
            return JSON.parseArray(node.toString(), Map.class).get(0);
        }else{
            return null;
        }
    }

    /**
     * 获取某节点的outgoings
     * @param node
     * @return
     */
    public static List<String> getOutGoings(Map node){
        Object outgoing = node.get("outgoing");
        List<String> outgoings = CollectionUtil.newArrayList();
        if(ObjectUtil.isNotNull(outgoing)){
            if(outgoing instanceof List){
                outgoings = JSON.parseArray(outgoing.toString(), String.class);
            }else{
                outgoings.add(outgoing.toString());
            }
        }
        return outgoings;
    }

    /**
     * 获取下一任务节点列表
     * @param defJson
     * @param outgoings
     * @param nodeList
     * @return
     */
    public static List<Map> getNextTaskNodes(String defJson, List<String> outgoings, List<Map> nodeList){
        if(nodeList == null){
            nodeList = CollectionUtil.newArrayList();
        }
        for(String outgoing: outgoings){
            Map incomingNode = getInComingNode(defJson, outgoing);
            if(incomingNode != null){
                String nodeType = (String)incomingNode.get("nodetype");
                if(!nodeType.equals("task")){
                    List<String> thizOutgoings = getOutGoings(incomingNode);
                    getNextTaskNodes(defJson, thizOutgoings, nodeList);
                }else{
                    nodeList.add(incomingNode);
                }
            }
        }
        return nodeList;
    }

    /**
     * 获取下一步任务节点
     * @param defJson
     * @param nodeId
     * @return
     */
    public static List<Map> getNextTaskNodes(String defJson, String nodeId){
        Map curNode = getFlowNode(defJson, nodeId);
        List<String> outgoings = getOutGoings(curNode);
        return getNextTaskNodes(defJson, outgoings, null);
    }

    /**
     * 获取第一个任务节点
     * @param defJson
     * @return
     */
    public static Map getFirstTaskNode(String defJson){
        Object startEventNode = getStartEventNode(defJson);
        JSONObject startEventNodeJson = JSON.parseObject(startEventNode.toString());
        String startEventNodeId = startEventNodeJson.getString("id");
        List<Map> nextTaskNodeList = getNextTaskNodes(defJson, startEventNodeId);
        return CollectionUtil.isNotEmpty(nextTaskNodeList)? nextTaskNodeList.get(0): null;
    }

}
