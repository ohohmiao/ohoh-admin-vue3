package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统字典
 * 
 * @author ohohmiao
 * @date 2023-05-25 14:14
 */
@Getter
@Setter
public class SysDicVO extends CommonVO {
    
    private String dicId;

    private String dictypeId;

    private String dicName;

    private String dicCode;

    private Integer dicSort;

    private String extendField;

    private String dictypeCode;

    private String dictypeName;
    
}
