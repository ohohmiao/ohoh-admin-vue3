package com.ohohmiao.modules.workflow.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 流程定义类别
 *
 * @author ohohmiao
 * @date 2024-12-02 09:46
 */
@Getter
@Setter
public class FlowDefTypeVO extends CommonTreeVO {

    private String deftypeId;

    private String deftypeName;

    private String deftypeCode;

    private String deftypeRemark;

}
