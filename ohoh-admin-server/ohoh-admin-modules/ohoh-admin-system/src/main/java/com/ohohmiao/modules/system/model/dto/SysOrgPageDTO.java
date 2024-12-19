package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织机构查询条件
 *
 * @author ohohmiao
 * @date 2023/4/12 22:17
 */
@ApiModel("组织机构分页查询条件")
@Getter
@Setter
public class SysOrgPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "父节点id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String orgName;

}
