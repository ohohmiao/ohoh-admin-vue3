package com.ohohmiao.modules.system.model.dto;

import com.ohohmiao.framework.common.model.dto.CommonPageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位用户分页查询条件
 *
 * @author ohohmiao
 * @date 2023-07-12 17:17
 */
@ApiModel("岗位用户分页查询条件")
@Getter
@Setter
public class SysPositionUserPageDTO extends CommonPageDTO {

    @ApiModelProperty(value = "父节点id")
    private String parentId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

}
