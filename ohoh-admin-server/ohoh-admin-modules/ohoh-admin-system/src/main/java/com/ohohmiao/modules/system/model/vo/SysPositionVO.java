package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位
 *
 * @author ohohmiao
 * @date 2023/6/22 16:56
 */
@Getter
@Setter
public class SysPositionVO extends CommonTreeVO {

    private String positionId;

    private String positionName;

    private String positionCode;

    private Integer positionLevel;

    private String positionRemark;

}
