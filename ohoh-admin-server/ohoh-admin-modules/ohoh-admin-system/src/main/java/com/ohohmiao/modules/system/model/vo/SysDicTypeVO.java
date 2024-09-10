package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典类别
 *
 * @author ohohmiao
 * @date 2023-05-25 14:12
 */
@Getter
@Setter
public class SysDicTypeVO extends CommonTreeVO {

    private String dictypeId;

    private String dictypeName;

    private String dictypeCode;

    private String dictypeRemark;

}
