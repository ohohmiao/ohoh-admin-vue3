package com.ohohmiao.modules.system.model.vo;

import com.ohohmiao.framework.common.model.vo.CommonTreeVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单
 *
 * @author ohohmiao
 * @date 2023-08-23 10:42
 */
@Getter
@Setter
public class SysMenuVO extends CommonTreeVO {

    private String resId;

    private String resName;

    private String resCode;

    private Integer resType;

    private String resIcon;

    private Integer menuType;

    private String resPath;

    private Integer hideFlag;

    private Integer fullscreenFlag;

}
