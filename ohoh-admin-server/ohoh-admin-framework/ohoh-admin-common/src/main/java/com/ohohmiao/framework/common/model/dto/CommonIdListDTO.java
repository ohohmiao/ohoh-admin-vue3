package com.ohohmiao.framework.common.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 通用id集合传参
 *
 * @author ohohmiao
 * @date 2023-04-12 14:20
 */
@ApiModel("id集合传参")
@Getter
@Setter
public class CommonIdListDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotEmpty(message = "id不能为空")
    private List<String> id;

}
