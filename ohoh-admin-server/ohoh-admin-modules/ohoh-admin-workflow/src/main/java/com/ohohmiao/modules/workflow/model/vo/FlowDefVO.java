package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程定义
 *
 * @author ohohmiao
 * @date 2024-12-02 11:17
 */
@Getter
@Setter
public class FlowDefVO extends CommonVO {

    private String defId;

    private String deftypeId;

    private String defName;

    private String defCode;

    private Integer defVersion;

    private Integer defSort;

    private String defXml;

    private String defJson;

    private String defSvg;

}
